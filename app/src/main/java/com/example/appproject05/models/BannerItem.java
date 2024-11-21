package com.example.appproject05.models;

public class BannerItem {
    private String id;
    private int imageResource;
    private String title;
    private String description;

    public BannerItem(String id, int imageResource, String title, String description) {
        this.id = id;
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
    }

    public String getId() { return id; }
    public int getImageResource() { return imageResource; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}