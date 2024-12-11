package com.example.appproject05.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.appproject05.R;
import com.example.appproject05.models.CartItem;
import com.example.appproject05.models.Product;
import com.example.appproject05.utils.CartManager;
import com.google.android.material.button.MaterialButton;

/**
 * Diálogo para adicionar produtos ao carrinho com seleção de quantidade
 */
public class AddToCartDialog extends Dialog {
    private Product product;
    private CartManager cartManager;
    private TextView productName, productPrice, quantity;
    private ImageButton btnDecrease, btnIncrease;
    private MaterialButton btnAddToCart;
    private int currentQuantity = 1;

    public AddToCartDialog(@NonNull Context context, Product product) {
        super(context);
        this.product = product;
        this.cartManager = new CartManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_to_cart);

        // Inicializar views
        initViews();

        // Configurar dados do produto
        setupProductInfo();

        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        quantity = findViewById(R.id.quantity);
        btnDecrease = findViewById(R.id.btn_decrease);
        btnIncrease = findViewById(R.id.btn_increase);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
    }

    private void setupProductInfo() {
        productName.setText(product.getName());
        updateTotalPrice();
    }

    private void setupListeners() {
        btnDecrease.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                updateQuantityViews();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            currentQuantity++;
            updateQuantityViews();
        });

        btnAddToCart.setOnClickListener(v -> addToCart());
    }

    private void updateQuantityViews() {
        quantity.setText(String.valueOf(currentQuantity));
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = product.getPrice() * currentQuantity;
        productPrice.setText(String.format("R$ %.2f", total));
    }

    private void addToCart() {
        CartItem cartItem = new CartItem(
                product.getId(),
                product.getName(),
                "Brendoka Bakery", // Nome da padaria fixo por enquanto
                product.getPrice(),
                currentQuantity
        );

        cartManager.addItem(cartItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Produto adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Erro ao adicionar ao carrinho: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}