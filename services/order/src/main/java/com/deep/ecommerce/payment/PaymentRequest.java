package com.deep.ecommerce.payment;

import com.deep.ecommerce.customer.CustomerResponse;
import com.deep.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
