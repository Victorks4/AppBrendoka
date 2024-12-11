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

    public Order() {
        // Construtor vazio necessário para o Firebase
    }

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
        subtotal = 0;
        for (CartItem item : items) {
            subtotal += item.getTotal();
        }
        deliveryFee = 5.0; // Taxa fixa
    }

    public double getTotal() {
        return subtotal + deliveryFee;
    }

    // Getters e Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getOrderDate() { return orderDate; }
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}