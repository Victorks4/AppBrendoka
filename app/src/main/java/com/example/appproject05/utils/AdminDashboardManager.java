package com.example.appproject05.utils;

import android.util.Log;
import com.example.appproject05.models.AdminDashboardStats;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class AdminDashboardManager {
    private static final String TAG = "AdminDashboardManager";
    private FirebaseDatabase database;

    public AdminDashboardManager() {
        database = FirebaseDatabase.getInstance();
    }

    // Método para buscar estatísticas com intervalo de datas personalizado
    public void fetchDashboardStatsForDateRange(
            long startTimeMillis,
            long endTimeMillis,
            DashboardStatsCallback callback) {

        AdminDashboardStats stats = new AdminDashboardStats();

        // Buscar dados do intervalo selecionado
        fetchStatsForDateRange(startTimeMillis, endTimeMillis, stats, () -> {
            // Buscar dados do período anterior para comparação
            fetchPreviousStatsForDateRange(startTimeMillis, endTimeMillis, stats, () -> {
                callback.onStatsRetrieved(stats);
            });
        });
    }

    private void fetchStatsForDateRange(
            long startTimeMillis,
            long endTimeMillis,
            AdminDashboardStats stats,
            Runnable onComplete) {

        Query rangeQuery = database.getReference("orders")
                .orderByChild("orderDate")
                .startAt(startTimeMillis)
                .endAt(endTimeMillis);

        rangeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int orderCount = 0;
                double revenue = 0.0;

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    orderCount++;
                    Double total = orderSnapshot.child("total").getValue(Double.class);
                    if (total != null) {
                        revenue += total;
                    }
                }

                stats.setMonthlyOrderCount(orderCount);
                stats.setMonthlyRevenue(revenue);
                onComplete.run();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Erro ao buscar estatísticas para intervalo", error.toException());
                onComplete.run();
            }
        });
    }

    private void fetchPreviousStatsForDateRange(
            long startTimeMillis,
            long endTimeMillis,
            AdminDashboardStats stats,
            Runnable onComplete) {

        // Calcular o período anterior com o mesmo intervalo
        long intervalDuration = endTimeMillis - startTimeMillis;
        long previousStartTimeMillis = startTimeMillis - intervalDuration;
        long previousEndTimeMillis = startTimeMillis - 1;

        Query previousRangeQuery = database.getReference("orders")
                .orderByChild("orderDate")
                .startAt(previousStartTimeMillis)
                .endAt(previousEndTimeMillis);

        previousRangeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int orderCount = 0;
                double revenue = 0.0;

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    orderCount++;
                    Double total = orderSnapshot.child("total").getValue(Double.class);
                    if (total != null) {
                        revenue += total;
                    }
                }

                stats.setPreviousMonthlyOrderCount(orderCount);
                stats.setPreviousMonthlyRevenue(revenue);
                onComplete.run();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Erro ao buscar estatísticas do período anterior", error.toException());
                onComplete.run();
            }
        });
    }

    // Método original mantido para compatibilidade
    public void fetchDashboardStats(DashboardStatsCallback callback) {
        Calendar firstDayOfMonth = Calendar.getInstance();
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        firstDayOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        firstDayOfMonth.set(Calendar.MINUTE, 0);
        firstDayOfMonth.set(Calendar.SECOND, 0);
        firstDayOfMonth.set(Calendar.MILLISECOND, 0);

        fetchDashboardStatsForDateRange(
                firstDayOfMonth.getTimeInMillis(),
                System.currentTimeMillis(),
                callback
        );
    }

    public interface DashboardStatsCallback {
        void onStatsRetrieved(AdminDashboardStats stats);
    }
}