package com.deep.ecommerce.kafka;

import com.deep.ecommerce.customer.CustomerResponse;
import com.deep.ecommerce.order.PaymentMethod;
import com.deep.ecommerce.product.PurchaseResponse;

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
