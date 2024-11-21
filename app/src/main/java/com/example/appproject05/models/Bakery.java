package com.example.appproject05.models;

public class Bakery {
    private String id;
    private String name;
    private String imageUrl;
    private float rating;
    private String deliveryTime;
    private String deliveryFee;
    private boolean isOpen;

    public Bakery(String id, String name, String imageUrl, float rating,
                  String deliveryTime, String deliveryFee, boolean isOpen) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.deliveryTime = deliveryTime;
        this.deliveryFee = deliveryFee;
        this.isOpen = isOpen;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public float getRating() { return rating; }
    public String getDeliveryTime() { return deliveryTime; }
    public String getDeliveryFee() { return deliveryFee; }
    public boolean isOpen() { return isOpen; }
}