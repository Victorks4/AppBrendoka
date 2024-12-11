package com.example.appproject05.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.models.Order;
import com.example.appproject05.models.OrderStatus;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OrderClickListener listener;
    private boolean isAdminView;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OrderClickListener {
        void onOrderStatusChange(Order order, OrderStatus newStatus);
        void onOrderClick(Order order);
    }

    public OrderAdapter(List<Order> orders, OrderClickListener listener, boolean isAdminView) {
        this.orders = orders;
        this.listener = listener;
        this.isAdminView = isAdminView;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderId;
        private TextView txtOrderDate;
        private TextView txtOrderStatus;
        private TextView txtOrderTotal;
        private MaterialButton btnNextStatus;
        private View orderStatusIndicator;

        OrderViewHolder(View itemView) {
            super(itemView);

            txtOrderId = itemView.findViewById(R.id.txt_order_id);
            txtOrderDate = itemView.findViewById(R.id.txt_order_date);
            txtOrderStatus = itemView.findViewById(R.id.txt_order_status);
            txtOrderTotal = itemView.findViewById(R.id.txt_order_total);
            btnNextStatus = itemView.findViewById(R.id.btn_next_status);
            orderStatusIndicator = itemView.findViewById(R.id.order_status_indicator);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onOrderClick(orders.get(position));
                }
            });
        }

        void bind(Order order) {
            txtOrderId.setText("Pedido #" + order.getOrderId());
            txtOrderDate.setText(dateFormat.format(new Date(order.getOrderDate())));
            txtOrderStatus.setText(order.getStatus().getLabel());
            txtOrderTotal.setText(String.format(Locale.getDefault(), "R$ %.2f", order.getTotal()));

            orderStatusIndicator.setBackgroundResource(getStatusColor(order.getStatus()));

            String currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                    FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

            if (isAdminView) {
                setupAdminStatusButton(order);
            } else if (currentUserId != null && currentUserId.equals(order.getUserId())) {
                // Usuário normal só pode cancelar seu próprio pedido quando pendente
                if (order.getStatus() == OrderStatus.PENDING) {
                    btnNextStatus.setVisibility(View.VISIBLE);
                    btnNextStatus.setText("Cancelar Pedido");
                    btnNextStatus.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onOrderStatusChange(order, OrderStatus.CANCELLED);
                        }
                    });
                } else {
                    btnNextStatus.setVisibility(View.GONE);
                }
            } else {
                btnNextStatus.setVisibility(View.GONE);
            }
        }

        private void setupAdminStatusButton(Order order) {
            OrderStatus nextStatus = getNextStatus(order.getStatus());
            if (nextStatus != null) {
                btnNextStatus.setVisibility(View.VISIBLE);
                btnNextStatus.setText(getNextStatusButtonText(nextStatus));
                btnNextStatus.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onOrderStatusChange(order, nextStatus);
                    }
                });
            } else {
                btnNextStatus.setVisibility(View.GONE);
            }
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

    private int getStatusColor(OrderStatus status) {
        switch (status) {
            case PENDING:
                return R.color.status_pending;
            case CONFIRMED:
                return R.color.status_confirmed;
            case PREPARING:
                return R.color.status_preparing;
            case READY:
                return R.color.status_ready;
            case OUT_FOR_DELIVERY:
                return R.color.status_out_for_delivery;
            case DELIVERED:
                return R.color.status_delivered;
            case CANCELLED:
                return R.color.status_cancelled;
            default:
                return R.color.status_unknown;
        }
    }

    private String getNextStatusButtonText(OrderStatus nextStatus) {
        switch (nextStatus) {
            case CONFIRMED:
                return "Confirmar Pedido";
            case PREPARING:
                return "Iniciar Preparação";
            case READY:
                return "Marcar como Pronto";
            case OUT_FOR_DELIVERY:
                return "Enviar para Entrega";
            case DELIVERED:
                return "Confirmar Entrega";
            default:
                return "Próximo Status";
        }
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }
}