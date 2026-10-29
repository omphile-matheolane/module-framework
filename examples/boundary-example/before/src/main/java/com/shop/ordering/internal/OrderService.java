package com.shop.ordering.internal;

import com.shop.payments.internal.PaymentGatewayImpl;

public class OrderService {
    private final PaymentGatewayImpl payments;

    public OrderService(PaymentGatewayImpl payments) {
        this.payments = payments;
    }

    public void placeOrder() {
        payments.pay();
    }
}
