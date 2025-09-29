package com.egin.ecommerce.order;

import com.egin.ecommerce.customer.CustomerClient;
import com.egin.ecommerce.exception.BusinessException;
import com.egin.ecommerce.kafka.OrderConfirmation;
import com.egin.ecommerce.kafka.OrderProducer;
import com.egin.ecommerce.product.ProductClient;
import com.egin.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;


    public Integer createOrder(@Valid OrderRequest orderRequest) {

        //check the customer -> OpenFeign
        var customer = this.customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order. No customer exists"));
        var purchasedProducts = this.productClient.purchaseProducts(orderRequest.products());

        // purchase the products
        var order = this.orderRepository.save(mapper.toOrder(orderRequest));

        for (PurchaseRequest request : orderRequest.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            request.productId(),
                            request.quantity()
                    )
            );
        }

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        orderRequest.reference(),
                        orderRequest.amount(),
                        orderRequest.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );


        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream().map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(int orderId) {
        return orderRepository.findById(orderId).map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided id: %d ", orderId)));
    }
}
