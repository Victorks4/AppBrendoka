package com.example.appproject05.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.models.AdminOrder;
import com.example.appproject05.models.CartItem;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<AdminOrder> orders;
    private OnOrderStatusChangeListener statusChangeListener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OnOrderStatusChangeListener {
        void onStatusChange(String orderId, String newStatus);
    }

    public OrderAdapter(List<AdminOrder> orders, OnOrderStatusChangeListener listener) {
        this.orders = orders;
        this.statusChangeListener = listener;
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
        AdminOrder order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, dateText, statusText, totalText, itemsText, addressText;
        MaterialButton btnUpdateStatus;

        OrderViewHolder(View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.order_id);
            dateText = itemView.findViewById(R.id.order_date);
            statusText = itemView.findViewById(R.id.order_status);
            totalText = itemView.findViewById(R.id.order_total);
            itemsText = itemView.findViewById(R.id.order_items);
            addressText = itemView.findViewById(R.id.order_address);
            btnUpdateStatus = itemView.findViewById(R.id.btn_update_status);
        }

        void bind(AdminOrder order) {
            orderIdText.setText("Pedido #" + order.getOrderId());
            dateText.setText(dateFormat.format(new Date(String.valueOf(order.getOrderDate()))));
            statusText.setText("Status: " + order.getStatus());
            totalText.setText(String.format("Total: R$ %.2f", order.getTotal()));

            // Criando lista de items
            StringBuilder items = new StringBuilder();
            for (CartItem item : order.getItems()) {
                items.append(item.getQuantity())
                        .append("x ")
                        .append(item.getProductName())
                        .append("\n");
            }
            itemsText.setText(items.toString());

            addressText.setText(order.getAddress());

            setupStatusButton(order);
        }

        private void setupStatusButton(AdminOrder order) {
            String nextStatus = getNextStatus(order.getStatus());
            if (nextStatus != null) {
                btnUpdateStatus.setText("Marcar como " + nextStatus);
                btnUpdateStatus.setOnClickListener(v ->
                        statusChangeListener.onStatusChange(order.getOrderId(), nextStatus));
                btnUpdateStatus.setVisibility(View.VISIBLE);
            } else {
                btnUpdateStatus.setVisibility(View.GONE);
            }
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
    }
}