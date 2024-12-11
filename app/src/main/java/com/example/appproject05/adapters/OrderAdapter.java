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
import com.google.android.material.chip.Chip;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private OrderClickListener listener;
    private SimpleDateFormat dateFormat;

    public interface OrderClickListener {
        void onOrderClick(Order order);
        void onCancelOrder(Order order);
        void onAdvanceStatus(Order order);
    }

    public OrderAdapter(List<Order> orders, OrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
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
        private TextView txtOrderId;
        private Chip chipStatus;
        private TextView txtOrderDate;
        private TextView txtOrderTotal;
        private TextView txtDeliveryAddress;
        private TextView txtPaymentMethod;
        private MaterialButton btnAdvanceStatus;
        private MaterialButton btnCancelOrder;

        OrderViewHolder(View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtDeliveryAddress = itemView.findViewById(R.id.txtDeliveryAddress);
            txtPaymentMethod = itemView.findViewById(R.id.txtPaymentMethod);
            btnAdvanceStatus = itemView.findViewById(R.id.btnAdvanceStatus);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onOrderClick(orders.get(position));
                }
            });

            btnAdvanceStatus.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAdvanceStatus(orders.get(position));
                }
            });

            btnCancelOrder.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCancelOrder(orders.get(position));
                }
            });
        }

        void bind(Order order) {
            txtOrderId.setText("Pedido #" + order.getOrderId());
            chipStatus.setText(order.getStatus().getLabel());
            txtOrderDate.setText("Data: " + dateFormat.format(new Date(order.getOrderDate())));
            txtOrderTotal.setText(String.format(Locale.getDefault(), "Total: R$ %.2f", order.getTotal()));
            txtDeliveryAddress.setText("Endereço: " + order.getAddress());
            txtPaymentMethod.setText("Pagamento: " + order.getPaymentMethod());

            // Configurar visibilidade dos botões baseado no status
            boolean showButtons = order.getStatus() != OrderStatus.DELIVERED &&
                    order.getStatus() != OrderStatus.CANCELLED;
            btnAdvanceStatus.setVisibility(showButtons ? View.VISIBLE : View.GONE);
            btnCancelOrder.setVisibility(showButtons ? View.VISIBLE : View.GONE);

            // Configurar cor do chip baseado no status
            switch (order.getStatus()) {
                case PENDING:
                    chipStatus.setChipBackgroundColorResource(R.color.status_pending);
                    break;
                case CONFIRMED:
                case PREPARING:
                    chipStatus.setChipBackgroundColorResource(R.color.status_preparing);
                    break;
                case READY:
                case OUT_FOR_DELIVERY:
                    chipStatus.setChipBackgroundColorResource(R.color.status_delivering);
                    break;
                case DELIVERED:
                    chipStatus.setChipBackgroundColorResource(R.color.status_delivered);
                    break;
                case CANCELLED:
                    chipStatus.setChipBackgroundColorResource(R.color.status_cancelled);
                    break;
            }

            // Configurar texto do botão baseado no status
            if (order.getStatus() == OrderStatus.PENDING) {
                btnAdvanceStatus.setText("Confirmar Pedido");
            } else if (order.getStatus() == OrderStatus.CONFIRMED) {
                btnAdvanceStatus.setText("Iniciar Preparo");
            } else if (order.getStatus() == OrderStatus.PREPARING) {
                btnAdvanceStatus.setText("Marcar como Pronto");
            } else if (order.getStatus() == OrderStatus.READY) {
                btnAdvanceStatus.setText("Enviar para Entrega");
            } else if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
                btnAdvanceStatus.setText("Confirmar Entrega");
            }
        }
    }
}