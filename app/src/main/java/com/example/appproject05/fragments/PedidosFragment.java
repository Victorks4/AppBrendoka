package com.example.appproject05.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.adapters.OrderAdapter;
import com.example.appproject05.models.Order;
import com.example.appproject05.models.OrderStatus;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class PedidosFragment extends Fragment implements OrderAdapter.OrderClickListener {

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private TabLayout ordersTabLayout;
    private List<Order> allOrders;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);

        initViews(view);
        setupRecyclerView();
        setupTabLayout();
        loadOrders();

        return view;
    }

    private void initViews(View view) {
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersTabLayout = view.findViewById(R.id.ordersTabLayout);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        allOrders = new ArrayList<>();
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(allOrders, this, false); // false indica que não é view de admin
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersRecyclerView.setAdapter(orderAdapter);
    }

    private void setupTabLayout() {
        ordersTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterOrders(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadOrders() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        db.collection("orders")
                .whereEqualTo("userId", userId)
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Tratar erro
                        return;
                    }

                    if (value != null && !value.isEmpty()) {
                        allOrders.clear();
                        for (var doc : value.getDocuments()) {
                            Order order = doc.toObject(Order.class);
                            if (order != null) {
                                order.setOrderId(doc.getId());
                                allOrders.add(order);
                            }
                        }
                        filterOrders(ordersTabLayout.getSelectedTabPosition());
                    }
                });
    }

    private void filterOrders(int tabPosition) {
        List<Order> filteredOrders = new ArrayList<>();

        if (tabPosition == 0) { // Pedidos em andamento
            for (Order order : allOrders) {
                if (order.getStatus() != OrderStatus.DELIVERED &&
                        order.getStatus() != OrderStatus.CANCELLED) {
                    filteredOrders.add(order);
                }
            }
        } else { // Histórico
            for (Order order : allOrders) {
                if (order.getStatus() == OrderStatus.DELIVERED ||
                        order.getStatus() == OrderStatus.CANCELLED) {
                    filteredOrders.add(order);
                }
            }
        }

        orderAdapter.updateOrders(filteredOrders);
    }

    @Override
    public void onOrderStatusChange(Order order, OrderStatus newStatus) {
        if (order.getStatus() == OrderStatus.PENDING && newStatus == OrderStatus.CANCELLED) {
            // Usuário só pode cancelar pedidos pendentes
            db.collection("orders").document(order.getOrderId())
                    .update("status", newStatus,
                            "statusHistory", order.getStatusHistory())
                    .addOnSuccessListener(aVoid -> loadOrders());
        }
    }

    @Override
    public void onOrderClick(Order order) {
        // Implementar navegação para detalhes do pedido
        // Por exemplo:
        // Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
        // intent.putExtra("orderId", order.getOrderId());
        // startActivity(intent);
    }
}