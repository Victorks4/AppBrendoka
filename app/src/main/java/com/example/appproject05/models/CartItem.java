package com.example.appproject05.models;

public class CartItem {
    private String productId;
    private String productName;
    private String bakeryName;
    private double price;
    private int quantity;
    private String notes;

    // Construtor vazio necessário para o Firebase
    public CartItem() {
        // Construtor vazio requerido para deserialização do Firebase
    }

    public CartItem(String productId, String productName, String bakeryName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.bakeryName = bakeryName;
        this.price = price;
        this.quantity = quantity;
        this.notes = "";
    }

    // Getters e Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBakeryName() {
        return bakeryName;
    }

    public void setBakeryName(String bakeryName) {
        this.bakeryName = bakeryName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getTotal() {
        return price * quantity;
    }
}