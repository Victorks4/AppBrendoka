package com.example.appproject05.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.models.CartItem;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> items;
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(CartItem item, int position, int newQuantity);
        void onItemRemoved(CartItem item, int position);
    }

    public CartAdapter(List<CartItem> items, CartItemListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, bakeryName, price, quantity;
        ImageButton btnIncrease, btnDecrease, btnRemove;

        CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            bakeryName = itemView.findViewById(R.id.bakery_name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        void bind(CartItem item) {
            productName.setText(item.getProductName());
            bakeryName.setText(item.getBakeryName());
            price.setText(String.format("R$ %.2f", item.getTotal()));
            quantity.setText(String.valueOf(item.getQuantity()));

            btnIncrease.setOnClickListener(v ->
                    listener.onQuantityChanged(item, getAdapterPosition(), item.getQuantity() + 1));

            btnDecrease.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    listener.onQuantityChanged(item, getAdapterPosition(), item.getQuantity() - 1);
                }
            });

            btnRemove.setOnClickListener(v ->
                    listener.onItemRemoved(item, getAdapterPosition()));
        }
    }
}