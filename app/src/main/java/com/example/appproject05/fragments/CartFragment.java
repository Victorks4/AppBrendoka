package com.example.appproject05.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.CheckoutActivity;
import com.example.appproject05.R;
import com.example.appproject05.adapters.CartAdapter;
import com.example.appproject05.models.CartItem;
import com.example.appproject05.utils.CartManager;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener, CartManager.CartListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartItems;
    private TextView txtSubtotal, txtDeliveryFee, txtTotal;
    private Button btnCheckout;
    private CartManager cartManager;
    private static final double DELIVERY_FEE = 5.00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initViews(view);
        setupRecyclerView();
        setupCartManager();
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.cart_recycler);
        txtSubtotal = view.findViewById(R.id.txt_subtotal);
        txtDeliveryFee = view.findViewById(R.id.txt_delivery_fee);
        txtTotal = view.findViewById(R.id.txt_total);
        btnCheckout = view.findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(v -> startCheckout());
    }

    private void setupRecyclerView() {
        cartItems = new ArrayList<>();
        adapter = new CartAdapter(cartItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupCartManager() {
        cartManager = new CartManager();
        cartManager.setCartListener(this);
    }

    private void startCheckout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "Seu carrinho está vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), CheckoutActivity.class);
        intent.putExtra("subtotal", cartManager.getCartTotal());
        intent.putExtra("delivery_fee", DELIVERY_FEE);
        startActivity(intent);
    }

    @Override
    public void onQuantityChanged(CartItem item, int position, int newQuantity) {
        cartManager.updateItemQuantity(item.getProductId(), newQuantity)
                .addOnFailureListener(e -> Toast.makeText(getContext(),
                        "Erro ao atualizar quantidade: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onItemRemoved(CartItem item, int position) {
        cartManager.removeItem(item.getProductId())
                .addOnFailureListener(e -> Toast.makeText(getContext(),
                        "Erro ao remover item: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onCartUpdated(List<CartItem> items, double total) {
        cartItems.clear();
        cartItems.addAll(items);
        adapter.notifyDataSetChanged();
        updateTotals(total);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), "Erro: " + message, Toast.LENGTH_SHORT).show();
    }

    private void updateTotals(double subtotal) {
        double total = subtotal + DELIVERY_FEE;

        txtSubtotal.setText(String.format("R$ %.2f", subtotal));
        txtDeliveryFee.setText(String.format("R$ %.2f", DELIVERY_FEE));
        txtTotal.setText(String.format("R$ %.2f", total));

        btnCheckout.setEnabled(subtotal > 0);
    }
}