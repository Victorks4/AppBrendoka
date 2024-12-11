package com.example.appproject05;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.adapters.OrderAdapter;
import com.example.appproject05.models.AdminOrder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<AdminOrder> allOrders;
    private List<AdminOrder> filteredOrders;
    private DatabaseReference ordersRef;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        // Inicializar listas
        allOrders = new ArrayList<>();
        filteredOrders = new ArrayList<>();

        // Inicializar Firebase
        ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");

        setupToolbar();
        setupTabLayout();
        setupRecyclerView();
        loadOrders();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gerenciar Pedidos");
        }
    }

    private void setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterOrdersByStatus(getStatusForTab(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private String getStatusForTab(int position) {
        switch (position) {
            case 0: return "PENDING";
            case 1: return "PREPARING";
            case 2: return "DELIVERING";
            case 3: return "COMPLETED";
            default: return "PENDING";
        }
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(filteredOrders, this::updateOrderStatus);
        recyclerView.setAdapter(adapter);
    }

    private void loadOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allOrders.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    try {
                        AdminOrder order = orderSnapshot.getValue(AdminOrder.class);
                        if (order != null) {
                            // Garantir que o orderId está definido
                            order.setOrderId(orderSnapshot.getKey());
                            allOrders.add(order);
                        }
                    } catch (Exception e) {
                        Toast.makeText(OrderManagementActivity.this,
                                "Erro ao carregar pedido: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                // Filtrar para o status atual
                filterOrdersByStatus(getStatusForTab(tabLayout.getSelectedTabPosition()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderManagementActivity.this,
                        "Erro ao carregar pedidos: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterOrdersByStatus(String status) {
        filteredOrders.clear();
        filteredOrders.addAll(allOrders.stream()
                .filter(order -> status.equals(order.getStatus()))
                .collect(Collectors.toList()));
        adapter.notifyDataSetChanged();
    }

    private void updateOrderStatus(String orderId, String newStatus) {
        ordersRef.child(orderId).child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Status atualizado com sucesso",
                                Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao atualizar status: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}