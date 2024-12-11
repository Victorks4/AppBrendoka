package com.example.appproject05.adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.models.CartItem;
import com.example.appproject05.models.Order;
import com.example.appproject05.models.OrderStatus;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private OnOrderActionListener listener;
    private FirebaseFirestore db;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OnOrderActionListener {
        void onAdvanceStatus(Order order);
        void onCancelOrder(Order order);
    }

    public AdminOrderAdapter(List<Order> orders, OnOrderActionListener listener) {
        this.orders = orders;
        this.listener = listener;
        this.db = FirebaseFirestore.getInstance();
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
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderId;
        private Chip chipStatus;
        private TextView orderDate;
        private TextView customerInfo;
        private TextView items;
        private TextView orderTotal;
        private TextView deliveryAddress;
        private TextView paymentMethod;
        private MaterialButton btnAdvance;
        private MaterialButton btnCancel;

        OrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.txtOrderId);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            orderDate = itemView.findViewById(R.id.txtOrderDate);
            customerInfo = itemView.findViewById(R.id.txtCustomerInfo);
            items = itemView.findViewById(R.id.txtItems);
            orderTotal = itemView.findViewById(R.id.txtOrderTotal);
            deliveryAddress = itemView.findViewById(R.id.txtDeliveryAddress);
            paymentMethod = itemView.findViewById(R.id.txtPaymentMethod);
            btnAdvance = itemView.findViewById(R.id.btnAdvanceStatus);
            btnCancel = itemView.findViewById(R.id.btnCancelOrder);
        }

        void bind(Order order) {
            orderId.setText("Pedido #" + order.getOrderId());
            orderDate.setText(dateFormat.format(new Date(order.getOrderDate())));

            // Configura o chip de status
            chipStatus.setText(order.getStatus().getLabel());
            chipStatus.setChipBackgroundColor(ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.getContext(), getStatusColor(order.getStatus()))));

            // Carrega informações do cliente
            loadCustomerInfo(order.getUserId());

            // Lista de itens
            StringBuilder itemsText = new StringBuilder();
            for (CartItem item : order.getItems()) {
                itemsText.append(String.format("%dx %s - R$ %.2f\n",
                        item.getQuantity(),
                        item.getProductName(),
                        item.getTotal()));
            }
            items.setText(itemsText.toString().trim());

            // Informações adicionais
            orderTotal.setText(String.format(Locale.getDefault(), "Total: R$ %.2f", order.getTotal()));
            deliveryAddress.setText("Endereço: " + order.getAddress());
            paymentMethod.setText("Pagamento: " + order.getPaymentMethod());

            // Configuração dos botões
            setupButtons(order);
        }

        private void loadCustomerInfo(String userId) {
            db.collection("usuarios").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nome = documentSnapshot.getString("nome");
                            String telefone = documentSnapshot.getString("telefone");
                            customerInfo.setText(String.format("Cliente: %s\nTelefone: %s",
                                    nome != null ? nome : "Nome não disponível",
                                    telefone != null ? telefone : "Telefone não disponível"));
                        }
                    });
        }

        private void setupButtons(Order order) {
            if (order.getStatus() != OrderStatus.DELIVERED &&
                    order.getStatus() != OrderStatus.CANCELLED) {

                btnAdvance.setVisibility(View.VISIBLE);
                btnAdvance.setText(getNextStatusButtonText(order.getStatus()));
                btnAdvance.setOnClickListener(v -> listener.onAdvanceStatus(order));

                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setOnClickListener(v -> listener.onCancelOrder(order));
            } else {
                btnAdvance.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
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

        private String getNextStatusButtonText(OrderStatus status) {
            switch (status) {
                case PENDING:
                    return "Confirmar Pedido";
                case CONFIRMED:
                    return "Iniciar Preparo";
                case PREPARING:
                    return "Marcar como Pronto";
                case READY:
                    return "Enviar para Entrega";
                case OUT_FOR_DELIVERY:
                    return "Confirmar Entrega";
                default:
                    return "Avançar Status";
            }
        }
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }
}