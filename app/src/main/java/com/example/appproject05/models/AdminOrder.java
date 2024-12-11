package com.example.appproject05.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Classe que representa um pedido no painel administrativo.
 * Contém informações detalhadas sobre o pedido e métodos para sua gestão.
 */
public class AdminOrder {
    private String orderId;
    private String userId;
    private List<CartItem> items;
    private double subtotal;
    private double deliveryFee;
    private double total;
    private String status;
    private String address;
    private String paymentMethod;
    private Date createdAt;
    private String customerName;
    private String customerPhone;

    // Status possíveis do pedido
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PREPARING = "PREPARING";
    public static final String STATUS_READY = "READY";
    public static final String STATUS_DELIVERING = "DELIVERING";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    /**
     * Construtor padrão necessário para o Firebase
     */
    public AdminOrder() {
        this.items = new ArrayList<>();
        this.createdAt = new Date();
        this.status = STATUS_PENDING;
    }

    /**
     * Construtor completo para criar um novo pedido
     */
    public AdminOrder(String orderId, String userId, List<CartItem> items, String address,
                      String paymentMethod, String customerName, String customerPhone) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items != null ? items : new ArrayList<>();
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.createdAt = new Date();
        this.status = STATUS_PENDING;
        calculateTotals();
    }

    /**
     * Calcula os totais do pedido (subtotal, taxa de entrega e total)
     */
    private void calculateTotals() {
        this.subtotal = 0;
        for (CartItem item : this.items) {
            this.subtotal += item.getTotal();
        }
        // Taxa de entrega fixa por enquanto
        this.deliveryFee = 5.0;
        this.total = this.subtotal + this.deliveryFee;
    }

    /**
     * Atualiza o status do pedido e valida a transição
     * @param newStatus Novo status do pedido
     * @return true se a atualização foi bem sucedida
     */
    public boolean updateStatus(String newStatus) {
        // Validar transição de status
        if (!isValidStatusTransition(newStatus)) {
            return false;
        }
        this.status = newStatus;
        return true;
    }

    /**
     * Valida se a transição de status é permitida
     */
    private boolean isValidStatusTransition(String newStatus) {
        switch (this.status) {
            case STATUS_PENDING:
                return newStatus.equals(STATUS_PREPARING) || newStatus.equals(STATUS_CANCELLED);
            case STATUS_PREPARING:
                return newStatus.equals(STATUS_READY) || newStatus.equals(STATUS_CANCELLED);
            case STATUS_READY:
                return newStatus.equals(STATUS_DELIVERING) || newStatus.equals(STATUS_CANCELLED);
            case STATUS_DELIVERING:
                return newStatus.equals(STATUS_DELIVERED) || newStatus.equals(STATUS_CANCELLED);
            case STATUS_DELIVERED:
            case STATUS_CANCELLED:
                return false; // Status finais não podem ser alterados
            default:
                return false;
        }
    }

    /**
     * Adiciona um item ao pedido
     */
    public void addItem(CartItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
        calculateTotals();
    }

    /**
     * Remove um item do pedido
     */
    public void removeItem(CartItem item) {
        if (this.items != null) {
            this.items.remove(item);
            calculateTotals();
        }
    }

    // Getters e Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        calculateTotals();
    }

    public Date getOrderDate() {
        return createdAt;
    }
    public double getSubtotal() { return subtotal; }
    public double getDeliveryFee() { return deliveryFee; }
    public double getTotal() { return total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
}