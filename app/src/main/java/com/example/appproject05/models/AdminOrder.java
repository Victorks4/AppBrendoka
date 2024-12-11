package com.example.appproject05.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modelo de pedido otimizado para Firebase Realtime Database
 */
@IgnoreExtraProperties
public class AdminOrder {
    @PropertyName("order_id")
    private String orderId;

    @PropertyName("user_id")
    private String userId;

    @PropertyName("items")
    private List<CartItem> items;

    @PropertyName("subtotal")
    private Double subtotal;

    @PropertyName("delivery_fee")
    private Double deliveryFee;

    @PropertyName("total")
    private Double total;

    @PropertyName("status")
    private String status;

    @PropertyName("address")
    private String address;

    @PropertyName("payment_method")
    private String paymentMethod;

    @PropertyName("created_at")
    private Long createdAt;

    @PropertyName("customer_name")
    private String customerName;

    @PropertyName("customer_phone")
    private String customerPhone;

    // Status constantes
    @Exclude
    public static final String STATUS_PENDING = "PENDING";
    @Exclude
    public static final String STATUS_PREPARING = "PREPARING";
    @Exclude
    public static final String STATUS_READY = "READY";
    @Exclude
    public static final String STATUS_DELIVERING = "DELIVERING";
    @Exclude
    public static final String STATUS_DELIVERED = "DELIVERED";
    @Exclude
    public static final String STATUS_CANCELLED = "CANCELLED";

    // Construtor vazio necessário para o Firebase
    public AdminOrder() {
        this.items = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.status = STATUS_PENDING;
        this.subtotal = 0.0;
        this.deliveryFee = 0.0;
        this.total = 0.0;
    }

    /**
     * Construtor para novo pedido
     */
    public AdminOrder(String userId, List<CartItem> items, String address,
                      String paymentMethod, String customerName, String customerPhone) {
        this();
        this.userId = userId;
        this.items = items != null ? items : new ArrayList<>();
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        calculateTotals();
    }

    /**
     * Calcula os totais do pedido
     */
    @Exclude
    private void calculateTotals() {
        this.subtotal = 0.0;
        if (this.items != null) {
            for (CartItem item : this.items) {
                this.subtotal += item.getTotal();
            }
        }
        this.deliveryFee = 5.0; // Taxa fixa por enquanto
        this.total = this.subtotal + this.deliveryFee;
    }

    /**
     * Converte o pedido para Map (útil para Firebase)
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("order_id", orderId);
        result.put("user_id", userId);
        result.put("items", items);
        result.put("subtotal", subtotal);
        result.put("delivery_fee", deliveryFee);
        result.put("total", total);
        result.put("status", status);
        result.put("address", address);
        result.put("payment_method", paymentMethod);
        result.put("created_at", createdAt);
        result.put("customer_name", customerName);
        result.put("customer_phone", customerPhone);
        return result;
    }

    // Getters e Setters com anotações do Firebase
    @PropertyName("order_id")
    public String getOrderId() { return orderId; }

    @PropertyName("order_id")
    public void setOrderId(String orderId) { this.orderId = orderId; }

    @PropertyName("user_id")
    public String getUserId() { return userId; }

    @PropertyName("user_id")
    public void setUserId(String userId) { this.userId = userId; }

    @PropertyName("items")
    public List<CartItem> getItems() { return items; }

    @PropertyName("items")
    public void setItems(List<CartItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        calculateTotals();
    }

    @PropertyName("subtotal")
    public Double getSubtotal() { return subtotal; }

    @PropertyName("subtotal")
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    @PropertyName("delivery_fee")
    public Double getDeliveryFee() { return deliveryFee; }

    @PropertyName("delivery_fee")
    public void setDeliveryFee(Double deliveryFee) { this.deliveryFee = deliveryFee; }

    @PropertyName("total")
    public Double getTotal() { return total; }

    @PropertyName("total")
    public void setTotal(Double total) { this.total = total; }

    @PropertyName("status")
    public String getStatus() { return status; }

    @PropertyName("status")
    public void setStatus(String status) { this.status = status; }

    @PropertyName("address")
    public String getAddress() { return address; }

    @PropertyName("address")
    public void setAddress(String address) { this.address = address; }

    @PropertyName("payment_method")
    public String getPaymentMethod() { return paymentMethod; }

    @PropertyName("payment_method")
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    @PropertyName("created_at")
    public Long getCreatedAt() { return createdAt; }

    @PropertyName("created_at")
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }

    @PropertyName("customer_name")
    public String getCustomerName() { return customerName; }

    @PropertyName("customer_name")
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    @PropertyName("customer_phone")
    public String getCustomerPhone() { return customerPhone; }

    @PropertyName("customer_phone")
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
}