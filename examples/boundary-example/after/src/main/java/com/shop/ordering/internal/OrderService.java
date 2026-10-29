package com.shop.ordering.internal;

import com.shop.payments.api.PaymentGateway;

public class OrderService {
    private final PaymentGateway payments;

    public OrderService(PaymentGateway payments) {
        this.payments = payments;
    }

    public void placeOrder() {
        payments.pay();
    }
}
