package com.example.appproject05.models;

public class Category {
    private String id;
    private String name;
    private int iconResource;

    public Category(String id, String name, int iconResource) {
        this.id = id;
        this.name = name;
        this.iconResource = iconResource;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getIconResource() { return iconResource; }
}