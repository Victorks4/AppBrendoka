package com.example.appproject05.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject05.CheckoutActivity;
import com.example.appproject05.R;
import com.example.appproject05.adapters.CartAdapter;
import com.example.appproject05.models.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartItems;
    private TextView txtSubtotal, txtDeliveryFee, txtTotal;
    private Button btnCheckout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initViews(view);
        setupRecyclerView();
        loadCartItems();
        updateTotals();

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

    private void loadCartItems() {
        // TODO: Load items from local storage or API
        // Mock data for now
        cartItems.add(new CartItem("1", "Pão Francês", "Padaria do João", 0.50, 10));
        cartItems.add(new CartItem("2", "Croissant", "Padaria do João", 5.00, 2));
        adapter.notifyDataSetChanged();
    }

    private void updateTotals() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotal();
        }

        double deliveryFee = 5.00; // Mock delivery fee
        double total = subtotal + deliveryFee;

        txtSubtotal.setText(String.format("R$ %.2f", subtotal));
        txtDeliveryFee.setText(String.format("R$ %.2f", deliveryFee));
        txtTotal.setText(String.format("R$ %.2f", total));
    }

    // Em CartFragment.java, dentro do método startCheckout():

    private void startCheckout() {
        Intent intent = new Intent(getActivity(), CheckoutActivity.class);
        intent.putExtra("subtotal", calculateSubtotal()); // Passar o subtotal calculado
        startActivity(intent);
    }

    private double calculateSubtotal() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotal();
        }
        return subtotal;
    }

    @Override
    public void onQuantityChanged(CartItem item, int position, int newQuantity) {
        item.setQuantity(newQuantity);
        adapter.notifyItemChanged(position);
        updateTotals();
    }

    @Override
    public void onItemRemoved(CartItem item, int position) {
        cartItems.remove(position);
        adapter.notifyItemRemoved(position);
        updateTotals();
    }
}