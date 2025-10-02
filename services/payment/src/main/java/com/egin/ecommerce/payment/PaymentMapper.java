package com.egin.ecommerce.payment;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {
    public Payment toPayment(@Valid PaymentRequest paymentRequest) {
        return Payment.builder()
                .id(paymentRequest.id())
                .amount(paymentRequest.amount())
                .paymentMethod(paymentRequest.paymentMethod())
                .orderId(paymentRequest.orderId())
                .build();
    }
}
