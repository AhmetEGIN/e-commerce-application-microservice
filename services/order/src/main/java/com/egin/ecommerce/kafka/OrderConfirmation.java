package com.egin.ecommerce.kafka;

import com.egin.ecommerce.customer.CustomerResponse;
import com.egin.ecommerce.order.PaymentMethod;
import com.egin.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
