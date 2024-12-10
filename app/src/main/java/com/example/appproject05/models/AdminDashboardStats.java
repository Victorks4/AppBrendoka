package com.example.appproject05.models;

public class AdminDashboardStats {
    private int ordersToday;
    private double revenueToday;
    private double ordersGrowth;
    private double revenueGrowth;
    private int totalProducts;
    private int pendingOrders;

    public AdminDashboardStats() {
        // Construtor vazio necessário para o Firebase
    }

    public AdminDashboardStats(int ordersToday, double revenueToday,
                               double ordersGrowth, double revenueGrowth,
                               int totalProducts, int pendingOrders) {
        this.ordersToday = ordersToday;
        this.revenueToday = revenueToday;
        this.ordersGrowth = ordersGrowth;
        this.revenueGrowth = revenueGrowth;
        this.totalProducts = totalProducts;
        this.pendingOrders = pendingOrders;
    }

    // Getters e Setters
    public int getOrdersToday() { return ordersToday; }
    public void setOrdersToday(int ordersToday) { this.ordersToday = ordersToday; }

    public double getRevenueToday() { return revenueToday; }
    public void setRevenueToday(double revenueToday) { this.revenueToday = revenueToday; }

    public double getOrdersGrowth() { return ordersGrowth; }
    public void setOrdersGrowth(double ordersGrowth) { this.ordersGrowth = ordersGrowth; }

    public double getRevenueGrowth() { return revenueGrowth; }
    public void setRevenueGrowth(double revenueGrowth) { this.revenueGrowth = revenueGrowth; }

    public int getTotalProducts() { return totalProducts; }
    public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }

    public int getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(int pendingOrders) { this.pendingOrders = pendingOrders; }
}