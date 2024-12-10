package com.example.appproject05.models;

import java.util.Date;

public class AdminOrder {
    private String orderId;
    private String userId;
    private String status; // PENDING, PREPARING, DELIVERING, COMPLETED, CANCELLED
    private double total;
    private Date createdAt;
    private String deliveryAddress;
    private String paymentMethod;

    public AdminOrder() {
        // Construtor vazio necessário para o Firebase
    }

    public AdminOrder(String orderId, String userId, String status, double total,
                      Date createdAt, String deliveryAddress, String paymentMethod) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
    }

    // Getters e Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}