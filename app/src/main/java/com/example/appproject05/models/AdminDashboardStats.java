package com.example.appproject05.models;

public class AdminDashboardStats {
    private int dailyOrderCount;
    private double dailyRevenue;
    private int monthlyOrderCount;
    private double monthlyRevenue;

    // Campos para cálculo de crescimento
    private int previousDailyOrderCount;
    private double previousDailyRevenue;
    private int previousMonthlyOrderCount;
    private double previousMonthlyRevenue;

    // Getters e Setters existentes
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

    // Métodos para calcular crescimento
    public double getDailyOrderGrowthPercentage() {
        if (previousDailyOrderCount == 0) {
            return dailyOrderCount > 0 ? 100.0 : 0.0;
        }
        return ((dailyOrderCount - previousDailyOrderCount) / (double) previousDailyOrderCount) * 100.0;
    }

    public double getDailyRevenueGrowthPercentage() {
        if (previousDailyRevenue == 0) {
            return dailyRevenue > 0 ? 100.0 : 0.0;
        }
        return ((dailyRevenue - previousDailyRevenue) / previousDailyRevenue) * 100.0;
    }

    public double getMonthlyOrderGrowthPercentage() {
        if (previousMonthlyOrderCount == 0) {
            return monthlyOrderCount > 0 ? 100.0 : 0.0;
        }
        return ((monthlyOrderCount - previousMonthlyOrderCount) / (double) previousMonthlyOrderCount) * 100.0;
    }

    public double getMonthlyRevenueGrowthPercentage() {
        if (previousMonthlyRevenue == 0) {
            return monthlyRevenue > 0 ? 100.0 : 0.0;
        }
        return ((monthlyRevenue - previousMonthlyRevenue) / previousMonthlyRevenue) * 100.0;
    }

    // Métodos para definir dados anteriores
    public void setPreviousDailyOrderCount(int previousDailyOrderCount) {
        this.previousDailyOrderCount = previousDailyOrderCount;
    }

    public void setPreviousDailyRevenue(double previousDailyRevenue) {
        this.previousDailyRevenue = previousDailyRevenue;
    }

    public void setPreviousMonthlyOrderCount(int previousMonthlyOrderCount) {
        this.previousMonthlyOrderCount = previousMonthlyOrderCount;
    }

    public void setPreviousMonthlyRevenue(double previousMonthlyRevenue) {
        this.previousMonthlyRevenue = previousMonthlyRevenue;
    }
}