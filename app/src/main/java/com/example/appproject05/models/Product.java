package com.example.appproject05.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private double price;
    private String category;
    private int imageResource; // Adicionado para compatibilidade
    private String imageUrl;
    private boolean active;
    private long createdAt;

    // Construtor vazio necessário para o Firebase
    public Product() {
        this.createdAt = System.currentTimeMillis();
        this.active = true;
    }

    // Construtor para manter compatibilidade com código existente
    public Product(String id, String name, String description, double price, int imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResource = imageResource;
        this.createdAt = System.currentTimeMillis();
        this.active = true;
    }

    // Construtor para novos produtos com categoria
    public Product(String name, String description, double price, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.createdAt = System.currentTimeMillis();
        this.active = true;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public long getCreatedAt() { return createdAt; }

    // Adicionado para manter compatibilidade com código existente
    public int getImageResource() { return imageResource; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }
}