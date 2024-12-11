package com.example.appproject05.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.PropertyName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String userId;
    private String customerName;    // Nome do cliente
    private String customerPhone;   // Telefone do cliente
    private List<CartItem> items;
    private double subtotal;
    private double deliveryFee;
    private String address;
    private String paymentMethod;
    private OrderStatus status;
    private long orderDate;
    private List<StatusHistory> statusHistory;
    private String notes;

    // Construtor vazio necessário para o Firebase
    public Order() {
        this.items = new ArrayList<>();
        this.statusHistory = new ArrayList<>();
    }

    // Construtor simplificado para uso no CheckoutActivity
    public Order(List<CartItem> items, String address, String paymentMethod) {
        this();
        this.items = items;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.status = OrderStatus.PENDING;
        this.orderDate = System.currentTimeMillis();
        this.userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        addStatusHistory(status, "Pedido criado");
        calculateTotals();
    }

    // Getters e Setters para os novos campos
    @PropertyName("customerName")
    public String getCustomerName() {
        return customerName;
    }

    @PropertyName("customerName")
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @PropertyName("customerPhone")
    public String getCustomerPhone() {
        return customerPhone;
    }

    @PropertyName("customerPhone")
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    // Método para atualizar o status
    public void updateStatus(OrderStatus newStatus, String observation) {
        this.status = newStatus;
        addStatusHistory(newStatus, observation);
    }

    // Método privado para adicionar ao histórico
    private void addStatusHistory(OrderStatus status, String observation) {
        StatusHistory history = new StatusHistory(status, System.currentTimeMillis(), observation);
        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }
        statusHistory.add(history);
    }

    private void calculateTotals() {
        this.subtotal = 0;
        for (CartItem item : items) {
            this.subtotal += item.getTotal();
        }
        this.deliveryFee = 5.0; // Taxa fixa por enquanto
    }

    // Classe interna para histórico de status
    public static class StatusHistory implements Serializable {
        private OrderStatus status;
        private long timestamp;
        private String observation;

        public StatusHistory() {}

        public StatusHistory(OrderStatus status, long timestamp, String observation) {
            this.status = status;
            this.timestamp = timestamp;
            this.observation = observation;
        }

        @PropertyName("status")
        public OrderStatus getStatus() { return status; }

        @PropertyName("status")
        public void setStatus(OrderStatus status) { this.status = status; }

        @PropertyName("timestamp")
        public long getTimestamp() { return timestamp; }

        @PropertyName("timestamp")
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

        @PropertyName("observation")
        public String getObservation() { return observation; }

        @PropertyName("observation")
        public void setObservation(String observation) { this.observation = observation; }
    }

    // Getters e Setters existentes
    @PropertyName("orderId")
    public String getOrderId() { return orderId; }

    @PropertyName("orderId")
    public void setOrderId(String orderId) { this.orderId = orderId; }

    @PropertyName("userId")
    public String getUserId() { return userId; }

    @PropertyName("userId")
    public void setUserId(String userId) { this.userId = userId; }

    @PropertyName("items")
    public List<CartItem> getItems() { return items; }

    @PropertyName("items")
    public void setItems(List<CartItem> items) { this.items = items; }

    @PropertyName("subtotal")
    public double getSubtotal() { return subtotal; }

    @PropertyName("subtotal")
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    @PropertyName("deliveryFee")
    public double getDeliveryFee() { return deliveryFee; }

    @PropertyName("deliveryFee")
    public void setDeliveryFee(double deliveryFee) { this.deliveryFee = deliveryFee; }

    @PropertyName("address")
    public String getAddress() { return address; }

    @PropertyName("address")
    public void setAddress(String address) { this.address = address; }

    @PropertyName("paymentMethod")
    public String getPaymentMethod() { return paymentMethod; }

    @PropertyName("paymentMethod")
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    @PropertyName("status")
    public OrderStatus getStatus() { return status; }

    @PropertyName("status")
    public void setStatus(OrderStatus status) { this.status = status; }

    @PropertyName("orderDate")
    public long getOrderDate() { return orderDate; }

    @PropertyName("orderDate")
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }

    @PropertyName("statusHistory")
    public List<StatusHistory> getStatusHistory() { return statusHistory; }

    @PropertyName("statusHistory")
    public void setStatusHistory(List<StatusHistory> statusHistory) { this.statusHistory = statusHistory; }

    @PropertyName("notes")
    public String getNotes() { return notes; }

    @PropertyName("notes")
    public void setNotes(String notes) { this.notes = notes; }

    // Método auxiliar para obter o total
    public double getTotal() {
        return subtotal + deliveryFee;
    }
}