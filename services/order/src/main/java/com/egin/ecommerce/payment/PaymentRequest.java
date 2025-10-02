package com.egin.ecommerce.payment;

import com.egin.ecommerce.customer.CustomerResponse;
import com.egin.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
