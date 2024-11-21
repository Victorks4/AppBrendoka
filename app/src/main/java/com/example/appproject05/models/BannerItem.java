package com.example.appproject05.models;

public class BannerItem {
    private String id;
    private String imageUrl;
    private String title;
    private String description;

    public BannerItem(String id, String imageUrl, String title, String description) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}