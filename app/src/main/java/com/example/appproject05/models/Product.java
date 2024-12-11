package com.example.appproject05.models;

import com.google.firebase.firestore.PropertyName;
import java.io.Serializable;

/**
 * Modelo de dados para produtos
 * Implementa Serializable para permitir passagem entre Activities
 */
public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private double price;
    private String category;
    private int imageResource;
    private long createdAt;
    private boolean active;

    // Construtor vazio necessário para o Firebase
    public Product() {
    }

    // Construtor básico (compatibilidade com código existente)
    public Product(String id, String name, String description, double price, int imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.active = true;
        this.createdAt = System.currentTimeMillis();
    }

    // Construtor completo
    public Product(String id, String name, String description, double price,
                   String category, int imageResource, long createdAt, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageResource = imageResource;
        this.createdAt = createdAt;
        this.active = active;
    }

    // Getters e Setters com anotações do Firebase
    @PropertyName("id")
    public String getId() {
        return id;
    }

    @PropertyName("id")
    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("price")
    public double getPrice() {
        return price;
    }

    @PropertyName("price")
    public void setPrice(double price) {
        this.price = price;
    }

    @PropertyName("category")
    public String getCategory() {
        return category;
    }

    @PropertyName("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @PropertyName("imageResource")
    public int getImageResource() {
        return imageResource;
    }

    @PropertyName("imageResource")
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @PropertyName("createdAt")
    public long getCreatedAt() {
        return createdAt;
    }

    @PropertyName("createdAt")
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @PropertyName("active")
    public boolean isActive() {
        return active;
    }

    @PropertyName("active")
    public void setActive(boolean active) {
        this.active = active;
    }
}