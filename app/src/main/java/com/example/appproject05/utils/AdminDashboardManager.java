package com.example.appproject05.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appproject05.models.AdminDashboardStats;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Manages retrieval and calculation of admin dashboard statistics
 */
public class AdminDashboardManager {
    private static final String TAG = "AdminDashboardManager";
    private FirebaseDatabase database;

    public AdminDashboardManager() {
        database = FirebaseDatabase.getInstance();
    }

    /**
     * Fetch daily and monthly statistics from Firebase
     * @param callback Callback to handle retrieved statistics
     */
    public void fetchDashboardStats(DashboardStatsCallback callback) {
        AdminDashboardStats stats = new AdminDashboardStats();

        // Fetch daily order count
        fetchDailyOrderCount(dailyOrderCount -> {
            stats.setDailyOrderCount(dailyOrderCount);

            // Fetch daily revenue
            fetchDailyRevenue(dailyRevenue -> {
                stats.setDailyRevenue(dailyRevenue);

                // Fetch monthly order count
                fetchMonthlyOrderCount(monthlyOrderCount -> {
                    stats.setMonthlyOrderCount(monthlyOrderCount);

                    // Fetch monthly revenue
                    fetchMonthlyRevenue(monthlyRevenue -> {
                        stats.setMonthlyRevenue(monthlyRevenue);

                        // Calculate growth percentages
                        fetchPreviousDayOrderCount(previousDailyCount -> {
                            stats.calculateDailyOrderGrowth(previousDailyCount);

                            fetchPreviousMonthOrderCount(previousMonthCount -> {
                                stats.calculateMonthlyOrderGrowth(previousMonthCount);

                                // Return complete stats
                                callback.onStatsRetrieved(stats);
                            });
                        });
                    });
                });
            });
        });
    }

    /**
     * Fetch daily order count
     * @param callback Callback with order count
     */
    private void fetchDailyOrderCount(IntegerCallback callback) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Query dailyOrderQuery = database.getReference("orders")
                .orderByChild("orderDate")
                .startAt(today.getTimeInMillis())
                .endAt(System.currentTimeMillis());

        dailyOrderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onResult((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching daily order count", error.toException());
                callback.onResult(0);
            }
        });
    }

    /**
     * Fetch daily revenue
     * @param callback Callback with total daily revenue
     */
    private void fetchDailyRevenue(DoubleCallback callback) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Query dailyRevenueQuery = database.getReference("orders")
                .orderByChild("orderDate")
                .startAt(today.getTimeInMillis())
                .endAt(System.currentTimeMillis());

        dailyRevenueQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalRevenue = 0.0;
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    // Assuming each order has a total field
                    Double orderTotal = orderSnapshot.child("total").getValue(Double.class);
                    if (orderTotal != null) {
                        totalRevenue += orderTotal;
                    }
                }
                callback.onResult(totalRevenue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching daily revenue", error.toException());
                callback.onResult(0.0);
            }
        });
    }

    /**
     * Fetch monthly order count
     * @param callback Callback with monthly order count
     */
    private void fetchMonthlyOrderCount(IntegerCallback callback) {
        Calendar firstDayOfMonth = Calendar.getInstance();
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        firstDayOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        firstDayOfMonth.set(Calendar.MINUTE, 0);
        firstDayOfMonth.set(Calendar.SECOND, 0);
        firstDayOfMonth.set(Calendar.MILLISECOND, 0);

        Query monthlyOrderQuery = database.getReference("orders")
                .orderByChild("orderDate")
                .startAt(firstDayOfMonth.getTimeInMillis())
                .endAt(System.currentTimeMillis());

        monthlyOrderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onResult((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching monthly order count", error.toException());
                callback.onResult(0);
            }
        });
    }

    /**
     * Fetch monthly revenue
     * @param callback Callback with total monthly revenue
     */
    private void fetchMonthlyRevenue(DoubleCallback callback) {
        Calendar firstDayOfMonth = Calendar.getInstance();
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        firstDayOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        firstDayOfMonth.set(Calendar.MINUTE, 0);
        firstDayOfMonth.set(Calendar.SECOND, 0);
        firstDayOfMonth.set(Calendar.MILLISECOND, 0);

        Query monthlyRevenueQuery = database.getReference("orders")
                .orderByChild("orderDate")
                .startAt(firstDayOfMonth.getTimeInMillis())
                .endAt(System.currentTimeMillis());

        monthlyRevenueQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalRevenue = 0.0;
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    // Assuming each order has a total field
                    Double orderTotal = orderSnapshot.child("total").getValue(Double.class);
                    if (orderTotal != null) {
                        totalRevenue += orderTotal;
                    }
                }
                callback.onResult(totalRevenue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching monthly revenue", error.toException());
                callback.onResult(0.0);
            }
        });
    }

    /**
     * Fetch previous day's order count for growth calculation
     * @param callback Callback with previous day's order count
     */
    private void fetchPreviousDayOrderCount(IntegerCallback callback) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.set(Calendar.MILLISECOND, 0);

        Calendar endOfYesterday = Calendar.getInstance();
        endOfYesterday.setTimeInMillis(yesterday.getTimeInMillis());
        endOfYesterday.add(Calendar.DAY_OF_MONTH, 1);

        Query previousDayOrderQuery = database.getReference("orders")
                .orderByChild("orderDate")
                .startAt(yesterday.getTimeInMillis())
                .endAt(endOfYesterday.getTimeInMillis());

        previousDayOrderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onResult((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching previous day order count", error.toException());
                callback.onResult(0);
            }
        });
    }

    /**
     * Fetch previous month's order count for growth calculation
     * @param callback Callback with previous month's order count
     */
    private void fetchPreviousMonthOrderCount(IntegerCallback callback) {
        Calendar previousMonth = Calendar.getInstance();
        previousMonth.add(Calendar.MONTH, -1);
        previousMonth.set(Calendar.DAY_OF_MONTH, 1);
        previousMonth.set(Calendar.HOUR_OF_DAY, 0);
        previousMonth.set(Calendar.MINUTE, 0);
        previousMonth.set(Calendar.SECOND, 0);
        previousMonth.set(Calendar.MILLISECOND, 0);

        Calendar endOfPreviousMonth = Calendar.getInstance();
        endOfPreviousMonth.setTimeInMillis(previousMonth.getTimeInMillis());
        endOfPreviousMonth.add(Calendar.MONTH, 1);
        endOfPreviousMonth.set(Calendar.DAY_OF_MONTH, 1);

        Query previousMonthOrderQuery = database.getReference("orders")
                .orderByChild("orderDate")
                .startAt(previousMonth.getTimeInMillis())
                .endAt(endOfPreviousMonth.getTimeInMillis());

        previousMonthOrderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onResult((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching previous month order count", error.toException());
                callback.onResult(0);
            }
        });
    }

    // Callback interfaces
    public interface DashboardStatsCallback {
        void onStatsRetrieved(AdminDashboardStats stats);
    }

    private interface IntegerCallback {
        void onResult(int result);
    }

    private interface DoubleCallback {
        void onResult(double result);
    }
}