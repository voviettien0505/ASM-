package com.fpoly.java.service;

public class OrderPaymentNotification {
    private String orderId;

    // Constructors, Getters, and Setters
    public OrderPaymentNotification(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
