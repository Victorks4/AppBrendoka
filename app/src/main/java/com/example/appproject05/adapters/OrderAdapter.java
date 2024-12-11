package com.example.appproject05.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.appproject05.R;
import com.example.appproject05.models.Order;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onAdvanceStatus(Order order);
        void onCancelOrder(Order order);
    }

    public OrderAdapter(List<Order> orders, OnOrderActionListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId;
        Chip chipStatus;
        TextView txtOrderDate;
        TextView txtOrderTotal;
        TextView txtDeliveryAddress;
        TextView txtPaymentMethod;
        MaterialButton btnAdvanceStatus;
        MaterialButton btnCancelOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtDeliveryAddress = itemView.findViewById(R.id.txtDeliveryAddress);
            txtPaymentMethod = itemView.findViewById(R.id.txtPaymentMethod);
            btnAdvanceStatus = itemView.findViewById(R.id.btnAdvanceStatus);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }

        public void bind(Order order) {
            txtOrderId.setText("Pedido #" + order.getOrderId());
            chipStatus.setText(order.getStatus());

            // Formatar data
            String formattedDate;
            try {
                long timestamp = order.getOrderDate();
                Date date = new Date(timestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                formattedDate = sdf.format(date);
            } catch (Exception e) {
                formattedDate = "Data não disponível";
            }
            txtOrderDate.setText(formattedDate);

            txtOrderTotal.setText(String.format(Locale.getDefault(), "Total: R$ %.2f", order.getTotal()));
            txtDeliveryAddress.setText("Endereço: " + order.getAddress());
            txtPaymentMethod.setText("Pagamento: " + order.getPaymentMethod());

            btnAdvanceStatus.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAdvanceStatus(order);
                }
            });

            btnCancelOrder.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelOrder(order);
                }
            });
        }
    }
}