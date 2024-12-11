package com.example.appproject05.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject05.R;
import com.example.appproject05.adapters.OrderAdapter;
import com.example.appproject05.models.CartItem;
import com.example.appproject05.models.Order;
import com.example.appproject05.models.OrderStatus;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PedidosFragment extends Fragment implements OrderAdapter.OrderClickListener {
    private static final String TAG = "PedidosFragment";

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private TabLayout tabLayout;
    private TextView emptyStateText;
    private ProgressBar progressBar;
    private List<Order> allOrders;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private SimpleDateFormat dateFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        initializeViews(view);
        setupFirebase();
        setupRecyclerView();
        setupTabLayout();
        loadOrders();
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.ordersRecyclerView);
        tabLayout = view.findViewById(R.id.ordersTabLayout);
        emptyStateText = view.findViewById(R.id.emptyStateText);
        progressBar = view.findViewById(R.id.progressBar);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    private void setupFirebase() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private void setupRecyclerView() {
        allOrders = new ArrayList<>();
        orderAdapter = new OrderAdapter(allOrders, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(orderAdapter);
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void loadOrders() {
        if (auth.getCurrentUser() == null) {
            showError("Usuário não está logado");
            return;
        }

        showLoading(true);
        String userId = auth.getCurrentUser().getUid();

        db.collection("orders")
                .whereEqualTo("userId", userId)
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    showLoading(false);

                    if (error != null) {
                        Log.e(TAG, "Erro ao carregar pedidos", error);
                        showError("Erro ao carregar pedidos: " + error.getMessage());
                        return;
                    }

                    if (value != null && !value.isEmpty()) {
                        allOrders.clear();
                        for (QueryDocumentSnapshot document : value) {
                            try {
                                Order order = document.toObject(Order.class);
                                order.setOrderId(document.getId());
                                allOrders.add(order);
                            } catch (Exception e) {
                                Log.e(TAG, "Erro ao converter documento: " + document.getId(), e);
                            }
                        }
                        filterOrders(tabLayout.getSelectedTabPosition());
                    } else {
                        showEmptyState();
                    }
                });
    }

    private void filterOrders(int tabPosition) {
        List<Order> filteredOrders = new ArrayList<>();

        if (tabPosition == 0) { // Pedidos Ativos
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
        updateEmptyState(filteredOrders.isEmpty());
    }

    @Override
    public void onOrderClick(Order order) {
        showOrderDetailsDialog(order);
    }

    @Override
    public void onCancelOrder(Order order) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cancelar Pedido")
                .setMessage("Tem certeza que deseja cancelar este pedido?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    updateOrderStatus(order, OrderStatus.CANCELLED, "Pedido cancelado pelo cliente");
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public void onAdvanceStatus(Order order) {
        OrderStatus nextStatus = getNextStatus(order.getStatus());
        if (nextStatus != null) {
            updateOrderStatus(order, nextStatus, "Status atualizado para: " + nextStatus.getLabel());
        }
    }

    private void updateOrderStatus(Order order, OrderStatus newStatus, String observation) {
        showLoading(true);
        order.updateStatus(newStatus, observation);

        db.collection("orders")
                .document(order.getOrderId())
                .set(order)
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    showSuccess("Status atualizado com sucesso!");
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    showError("Erro ao atualizar status: " + e.getMessage());
                });
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

    private void showOrderDetailsDialog(Order order) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Pedido #" + order.getOrderId())
                .setMessage(buildOrderDetails(order))
                .setPositiveButton("Fechar", null)
                .show();
    }

    private String buildOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Data: ").append(dateFormat.format(new Date(order.getOrderDate()))).append("\n\n");
        details.append("Status: ").append(order.getStatus().getLabel()).append("\n\n");

        details.append("Itens:\n");
        for (CartItem item : order.getItems()) {
            details.append("- ")
                    .append(item.getProductName())
                    .append(" (").append(item.getQuantity()).append("x)")
                    .append(" R$ ").append(String.format(Locale.getDefault(), "%.2f", item.getTotal()))
                    .append("\n");
        }

        details.append("\nSubtotal: R$ ").append(String.format(Locale.getDefault(), "%.2f", order.getSubtotal()));
        details.append("\nTaxa de entrega: R$ ").append(String.format(Locale.getDefault(), "%.2f", order.getDeliveryFee()));
        details.append("\nTotal: R$ ").append(String.format(Locale.getDefault(), "%.2f", order.getTotal()));

        details.append("\n\nEndereço de entrega:\n").append(order.getAddress());
        details.append("\n\nForma de pagamento: ").append(order.getPaymentMethod());

        if (order.getStatusHistory() != null && !order.getStatusHistory().isEmpty()) {
            details.append("\n\nHistórico:\n");
            for (Order.StatusHistory history : order.getStatusHistory()) {
                details.append("- ")
                        .append(dateFormat.format(new Date(history.getTimestamp())))
                        .append(": ")
                        .append(history.getStatus().getLabel());
                if (history.getObservation() != null && !history.getObservation().isEmpty()) {
                    details.append(" (").append(history.getObservation()).append(")");
                }
                details.append("\n");
            }
        }

        return details.toString();
    }

    private void showEmptyState() {
        if (allOrders.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyStateText.setText("Você ainda não fez nenhum pedido");
        } else {
            emptyStateText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            emptyStateText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            if (tabLayout.getSelectedTabPosition() == 0) {
                emptyStateText.setText("Você não tem pedidos ativos no momento");
            } else {
                emptyStateText.setText("Seu histórico de pedidos está vazio");
            }
        } else {
            emptyStateText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                    .setAction("Tentar Novamente", v -> loadOrders())
                    .show();
        }
    }

    private void showSuccess(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
}