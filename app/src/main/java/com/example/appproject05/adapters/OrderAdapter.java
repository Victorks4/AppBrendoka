package com.example.appproject05.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.models.AdminOrder;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final List<AdminOrder> orderList;
    private final OnOrderStatusChangeListener statusChangeListener;
    private final SimpleDateFormat dateFormat;

    public interface OnOrderStatusChangeListener {
        void onOrderStatusChange(String orderId, String newStatus);
    }

    public OrderAdapter(List<AdminOrder> orderList, OnOrderStatusChangeListener listener) {
        this.orderList = orderList;
        this.statusChangeListener = listener;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_admin, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        AdminOrder order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtOrderId;
        private final TextView txtOrderDate;
        private final TextView txtOrderTotal;
        private final TextView txtDeliveryAddress;
        private final TextView txtPaymentMethod;
        private final Chip chipStatus;
        private final MaterialButton btnAdvanceStatus;
        private final MaterialButton btnCancelOrder;

        OrderViewHolder(View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtDeliveryAddress = itemView.findViewById(R.id.txtDeliveryAddress);
            txtPaymentMethod = itemView.findViewById(R.id.txtPaymentMethod);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            btnAdvanceStatus = itemView.findViewById(R.id.btnAdvanceStatus);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }

        void bind(AdminOrder order) {
            txtOrderId.setText("Pedido #" + order.getOrderId());
            txtOrderDate.setText(dateFormat.format(order.getCreatedAt()));
            txtOrderTotal.setText(String.format(Locale.getDefault(), "R$ %.2f", order.getTotal()));
            txtDeliveryAddress.setText(order.getDeliveryAddress());
            txtPaymentMethod.setText(order.getPaymentMethod());

            updateStatusViews(order.getStatus());

            btnAdvanceStatus.setOnClickListener(v -> {
                String nextStatus = getNextStatus(order.getStatus());
                if (nextStatus != null) {
                    statusChangeListener.onOrderStatusChange(order.getOrderId(), nextStatus);
                }
            });

            btnCancelOrder.setOnClickListener(v ->
                    statusChangeListener.onOrderStatusChange(order.getOrderId(), "CANCELLED"));
        }

        private void updateStatusViews(String status) {
            chipStatus.setText(getStatusDisplayText(status));

            // Configurar cor do chip baseado no status
            int colorRes;
            switch (status) {
                case "PENDING":
                    colorRes = R.color.status_pending;
                    btnAdvanceStatus.setText("Iniciar Preparo");
                    break;
                case "PREPARING":
                    colorRes = R.color.status_preparing;
                    btnAdvanceStatus.setText("Enviar para Entrega");
                    break;
                case "DELIVERING":
                    colorRes = R.color.status_delivering;
                    btnAdvanceStatus.setText("Confirmar Entrega");
                    break;
                case "COMPLETED":
                    colorRes = R.color.status_completed;
                    btnAdvanceStatus.setVisibility(View.GONE);
                    btnCancelOrder.setVisibility(View.GONE);
                    break;
                case "CANCELLED":
                    colorRes = R.color.status_cancelled;
                    btnAdvanceStatus.setVisibility(View.GONE);
                    btnCancelOrder.setVisibility(View.GONE);
                    break;
                default:
                    colorRes = R.color.status_pending;
                    break;
            }

            chipStatus.setChipBackgroundColorResource(colorRes);
        }

        private String getNextStatus(String currentStatus) {
            switch (currentStatus) {
                case "PENDING":
                    return "PREPARING";
                case "PREPARING":
                    return "DELIVERING";
                case "DELIVERING":
                    return "COMPLETED";
                default:
                    return null;
            }
        }

        private String getStatusDisplayText(String status) {
            switch (status) {
                case "PENDING":
                    return "Pendente";
                case "PREPARING":
                    return "Em Preparo";
                case "DELIVERING":
                    return "Em Entrega";
                case "COMPLETED":
                    return "Concluído";
                case "CANCELLED":
                    return "Cancelado";
                default:
                    return status;
            }
        }
    }
}