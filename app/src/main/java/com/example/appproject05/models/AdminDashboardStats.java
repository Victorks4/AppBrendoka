package com.example.appproject05.models;

import java.util.Date;

/**
 * Represents statistical data for the admin dashboard
 * Tracks daily and monthly order and revenue information
 */
public class AdminDashboardStats {
    // Daily metrics
    private int dailyOrderCount;
    private double dailyRevenue;
    private double dailyOrderGrowthPercentage;

    // Monthly metrics
    private int monthlyOrderCount;
    private double monthlyRevenue;
    private double monthlyOrderGrowthPercentage;

    // Timestamp for data collection
    private Date timestamp;

    // Constructor
    public AdminDashboardStats() {
        this.timestamp = new Date();
    }

    // Getters and Setters
    public int getDailyOrderCount() {
        return dailyOrderCount;
    }

    public void setDailyOrderCount(int dailyOrderCount) {
        this.dailyOrderCount = dailyOrderCount;
    }

    public double getDailyRevenue() {
        return dailyRevenue;
    }

    public void setDailyRevenue(double dailyRevenue) {
        this.dailyRevenue = dailyRevenue;
    }

    public double getDailyOrderGrowthPercentage() {
        return dailyOrderGrowthPercentage;
    }

    public void setDailyOrderGrowthPercentage(double dailyOrderGrowthPercentage) {
        this.dailyOrderGrowthPercentage = dailyOrderGrowthPercentage;
    }

    public int getMonthlyOrderCount() {
        return monthlyOrderCount;
    }

    public void setMonthlyOrderCount(int monthlyOrderCount) {
        this.monthlyOrderCount = monthlyOrderCount;
    }

    public double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public double getMonthlyOrderGrowthPercentage() {
        return monthlyOrderGrowthPercentage;
    }

    public void setMonthlyOrderGrowthPercentage(double monthlyOrderGrowthPercentage) {
        this.monthlyOrderGrowthPercentage = monthlyOrderGrowthPercentage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Calculate daily order growth percentage
     * @param previousDayOrderCount Previous day's order count
     */
    public void calculateDailyOrderGrowth(int previousDayOrderCount) {
        if (previousDayOrderCount > 0) {
            this.dailyOrderGrowthPercentage =
                    ((double)(dailyOrderCount - previousDayOrderCount) / previousDayOrderCount) * 100;
        } else {
            this.dailyOrderGrowthPercentage = 100; // First day or no previous orders
        }
    }

    /**
     * Calculate monthly order growth percentage
     * @param previousMonthOrderCount Previous month's order count
     */
    public void calculateMonthlyOrderGrowth(int previousMonthOrderCount) {
        if (previousMonthOrderCount > 0) {
            this.monthlyOrderGrowthPercentage =
                    ((double)(monthlyOrderCount - previousMonthOrderCount) / previousMonthOrderCount) * 100;
        } else {
            this.monthlyOrderGrowthPercentage = 100; // First month or no previous orders
        }
    }
}