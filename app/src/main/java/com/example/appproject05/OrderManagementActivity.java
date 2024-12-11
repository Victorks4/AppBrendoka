package com.example.appproject05;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.adapters.AdminOrderAdapter;
import com.example.appproject05.models.Order;
import com.example.appproject05.models.OrderStatus;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementActivity extends AppCompatActivity implements AdminOrderAdapter.OnOrderActionListener {
    private RecyclerView recyclerView;
    private AdminOrderAdapter adapter;
    private List<Order> allOrders;
    private FirebaseFirestore db;
    private TabLayout tabLayout;
    private ListenerRegistration ordersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        initViews();
        setupToolbar();
        setupTabLayout();
        setupRecyclerView();
        loadOrders();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewOrders);
        tabLayout = findViewById(R.id.tabLayout);
        db = FirebaseFirestore.getInstance();
        allOrders = new ArrayList<>();
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Pendentes"));
        tabLayout.addTab(tabLayout.newTab().setText("Em Preparo"));
        tabLayout.addTab(tabLayout.newTab().setText("Entrega"));
        tabLayout.addTab(tabLayout.newTab().setText("Concluídos"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterOrdersByStatus(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerView() {
        adapter = new AdminOrderAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Gerenciar Pedidos");
        }
    }
    private void loadOrders() {
        ordersListener = db.collection("orders")
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Erro ao carregar pedidos: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        allOrders.clear();
                        for (var doc : value.getDocuments()) {
                            Order order = doc.toObject(Order.class);
                            if (order != null) {
                                order.setOrderId(doc.getId());
                                allOrders.add(order);
                            }
                        }
                        filterOrdersByStatus(tabLayout.getSelectedTabPosition());
                    }
                });
    }

    private void filterOrdersByStatus(int tabPosition) {
        List<Order> filteredOrders = new ArrayList<>();

        switch (tabPosition) {
            case 0: // Pendentes
                for (Order order : allOrders) {
                    if (order.getStatus() == OrderStatus.PENDING) {
                        filteredOrders.add(order);
                    }
                }
                break;
            case 1: // Em Preparo
                for (Order order : allOrders) {
                    if (order.getStatus() == OrderStatus.CONFIRMED ||
                            order.getStatus() == OrderStatus.PREPARING) {
                        filteredOrders.add(order);
                    }
                }
                break;
            case 2: // Entrega
                for (Order order : allOrders) {
                    if (order.getStatus() == OrderStatus.READY ||
                            order.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
                        filteredOrders.add(order);
                    }
                }
                break;
            case 3: // Concluídos
                for (Order order : allOrders) {
                    if (order.getStatus() == OrderStatus.DELIVERED ||
                            order.getStatus() == OrderStatus.CANCELLED) {
                        filteredOrders.add(order);
                    }
                }
                break;
        }
        adapter.updateOrders(filteredOrders);
    }

    @Override
    public void onAdvanceStatus(Order order) {
        OrderStatus nextStatus = getNextStatus(order.getStatus());
        if (nextStatus != null) {
            updateOrderStatus(order, nextStatus);
        }
    }

    private OrderStatus getNextStatus(OrderStatus currentStatus) {
        switch (currentStatus) {
            case PENDING:
                return OrderStatus.CONFIRMED;
            case CONFIRMED:
                return OrderStatus.PREPARING;
            case PREPARING:
                return OrderStatus.READY;
            case READY:
                return OrderStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY:
                return OrderStatus.DELIVERED;
            default:
                return null;
        }
    }

    private void updateOrderStatus(Order order, OrderStatus newStatus) {
        String observation = "Status atualizado para " + newStatus.getLabel();
        order.updateStatus(newStatus, observation);

        db.collection("orders").document(order.getOrderId())
                .set(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Status atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    // A atualização da lista será automática pelo SnapshotListener
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao atualizar status: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onCancelOrder(Order order) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Cancelar Pedido")
                .setMessage("Tem certeza que deseja cancelar este pedido?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    updateOrderStatus(order, OrderStatus.CANCELLED);
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ordersListener != null) {
            ordersListener.remove();
        }
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