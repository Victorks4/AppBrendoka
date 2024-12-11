package com.example.appproject05.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.appproject05.R;
import com.example.appproject05.models.AdminOrder;
import com.example.appproject05.models.CartItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> {
    private List<AdminOrder> orders;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onAdvanceStatus(AdminOrder order);
        void onCancelOrder(AdminOrder order);
    }

    public AdminOrderAdapter(List<AdminOrder> orders, OnOrderActionListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_admin, parent, false);
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

    public void updateOrders(List<AdminOrder> newOrders) {
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
        TextView txtCustomerInfo;
        TextView txtItems;
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
            txtCustomerInfo = itemView.findViewById(R.id.txtCustomerInfo);
            txtItems = itemView.findViewById(R.id.txtItems);
            btnAdvanceStatus = itemView.findViewById(R.id.btnAdvanceStatus);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }

        public void bind(AdminOrder order) {
            txtOrderId.setText("Pedido #" + order.getOrderId());
            chipStatus.setText(order.getStatus());

            // Formatar data
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                    Locale.getDefault()).format(new Date(order.getCreatedAt()));
            txtOrderDate.setText(formattedDate);

            // Informações do cliente
            String customerInfo = String.format("Cliente: %s\nTelefone: %s",
                    order.getCustomerName(), order.getCustomerPhone());
            txtCustomerInfo.setText(customerInfo);

            // Itens do pedido
            StringBuilder itemsText = new StringBuilder("Itens:\n");
            if (order.getItems() != null) {
                for (CartItem item : order.getItems()) {
                    itemsText.append(String.format("%dx %s - R$ %.2f\n",
                            item.getQuantity(), item.getProductName(), item.getTotal()));
                }
            }
            txtItems.setText(itemsText.toString());

            txtOrderTotal.setText(String.format(Locale.getDefault(),
                    "Total: R$ %.2f", order.getTotal()));
            txtDeliveryAddress.setText("Endereço: " + order.getAddress());
            txtPaymentMethod.setText("Pagamento: " + order.getPaymentMethod());

            // Configurar botão de avançar status
            setupAdvanceButton(order);

            btnCancelOrder.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelOrder(order);
                }
            });

            // Ajustar visibilidade dos botões baseado no status
            updateButtonsVisibility(order.getStatus());
        }

        private void setupAdvanceButton(AdminOrder order) {
            String nextStatus = getNextStatus(order.getStatus());
            if (nextStatus != null) {
                btnAdvanceStatus.setText(getButtonTextForStatus(nextStatus));
                btnAdvanceStatus.setVisibility(View.VISIBLE);
                btnAdvanceStatus.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onAdvanceStatus(order);
                    }
                });
            } else {
                btnAdvanceStatus.setVisibility(View.GONE);
            }
        }

        private String getNextStatus(String currentStatus) {
            switch (currentStatus) {
                case AdminOrder.STATUS_PENDING:
                    return AdminOrder.STATUS_PREPARING;
                case AdminOrder.STATUS_PREPARING:
                    return AdminOrder.STATUS_READY;
                case AdminOrder.STATUS_READY:
                    return AdminOrder.STATUS_DELIVERING;
                case AdminOrder.STATUS_DELIVERING:
                    return AdminOrder.STATUS_DELIVERED;
                default:
                    return null;
            }
        }

        private String getButtonTextForStatus(String status) {
            switch (status) {
                case AdminOrder.STATUS_PREPARING:
                    return "Iniciar Preparo";
                case AdminOrder.STATUS_READY:
                    return "Marcar como Pronto";
                case AdminOrder.STATUS_DELIVERING:
                    return "Enviar para Entrega";
                case AdminOrder.STATUS_DELIVERED:
                    return "Confirmar Entrega";
                default:
                    return "Avançar";
            }
        }

        private void updateButtonsVisibility(String status) {
            boolean showButtons = !status.equals(AdminOrder.STATUS_DELIVERED) &&
                    !status.equals(AdminOrder.STATUS_CANCELLED);
            btnAdvanceStatus.setEnabled(showButtons);
            btnCancelOrder.setEnabled(showButtons);
        }
    }
}