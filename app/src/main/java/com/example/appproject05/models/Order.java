package com.example.appproject05.models;

import java.util.List;

public class Order {
    private String orderId;
    private List<CartItem> items;
    private double subtotal;
    private double deliveryFee;
    private String address;
    private String paymentMethod;
    private String status;
    private long orderDate;
    private String userId;
    public Order(List<CartItem> items, String address, String paymentMethod) {
        this.items = items;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.status = "PENDING";
        this.orderDate = System.currentTimeMillis();
        this.orderId = "ORD" + this.orderDate;
        calculateTotals();
    }

    private void calculateTotals() {
        this.subtotal = 0;
        for (CartItem item : items) {
            this.subtotal += item.getTotal();
        }
        this.deliveryFee = 5.0; // Taxa fixa por enquanto
    }

    public double getTotal() {
        return subtotal + deliveryFee;
    }

    // Getters e Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public List<CartItem> getItems() { return items; }
    public double getSubtotal() { return subtotal; }
    public double getDeliveryFee() { return deliveryFee; }
    public String getAddress() { return address; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getOrderDate() { return orderDate; }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}