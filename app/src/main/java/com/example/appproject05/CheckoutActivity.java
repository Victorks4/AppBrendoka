package com.example.appproject05;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appproject05.models.CartItem;
import com.example.appproject05.utils.CartManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    private TextView txtSubtotal, txtDelivery, txtTotal;
    private TextView addressTitle, addressDetails;
    private RadioGroup paymentOptions;
    private MaterialButton btnChangeAddress, btnPlaceOrder;
    private CartManager cartManager;
    private FirebaseFirestore firestore;
    private double subtotal = 0.0;
    private double deliveryFee = 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initializeViews();
        setupFirebase();
        setupCartManager();
        setupListeners();
        loadOrderData();
    }

    private void initializeViews() {
        txtSubtotal = findViewById(R.id.txt_checkout_subtotal);
        txtDelivery = findViewById(R.id.txt_checkout_delivery);
        txtTotal = findViewById(R.id.txt_checkout_total);
        addressTitle = findViewById(R.id.address_title);
        addressDetails = findViewById(R.id.address_details);
        paymentOptions = findViewById(R.id.payment_options);
        btnChangeAddress = findViewById(R.id.btn_change_address);
        btnPlaceOrder = findViewById(R.id.btn_place_order);
    }

    private void setupFirebase() {
        firestore = FirebaseFirestore.getInstance();
    }

    private void setupCartManager() {
        cartManager = new CartManager();
    }

    private void setupListeners() {
        btnChangeAddress.setOnClickListener(v -> {
            Toast.makeText(this, "Selecionar endereço", Toast.LENGTH_SHORT).show();
        });

        paymentOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_money) {
                showChangeInputDialog();
            }
        });

        btnPlaceOrder.setOnClickListener(v -> {
            if (validateOrder()) {
                processOrder();
            }
        });
    }

    private void loadOrderData() {
        subtotal = getIntent().getDoubleExtra("subtotal", 0.0);
        deliveryFee = getIntent().getDoubleExtra("delivery_fee", 5.0);
        updateTotals();
        loadDefaultAddress();
    }

    private void updateTotals() {
        txtSubtotal.setText(String.format("R$ %.2f", subtotal));
        txtDelivery.setText(String.format("R$ %.2f", deliveryFee));
        double total = subtotal + deliveryFee;
        txtTotal.setText(String.format("R$ %.2f", total));
    }

    private void loadDefaultAddress() {
        addressTitle.setText("Casa");
        addressDetails.setText("Rua Exemplo, 123 - Bairro - Cidade");
    }

    private void showChangeInputDialog() {
        Toast.makeText(this, "Informar troco", Toast.LENGTH_SHORT).show();
    }

    private boolean validateOrder() {
        if (paymentOptions.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione uma forma de pagamento", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void processOrder() {
        showLoading(true);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Pegar itens do carrinho diretamente
        List<CartItem> cartItems = cartManager.getCartItems();

        // Buscar dados do usuário
        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String customerName = snapshot.child("nome").getValue(String.class);
                        String customerPhone = snapshot.child("telefone").getValue(String.class);

                        // Criar mapa de dados do pedido
                        Map<String, Object> orderData = new HashMap<>();
                        orderData.put("userId", userId);
                        orderData.put("customerName", customerName);
                        orderData.put("customerPhone", customerPhone);
                        orderData.put("items", cartItems);
                        orderData.put("address", addressDetails.getText().toString());
                        orderData.put("paymentMethod", getSelectedPaymentMethod());
                        orderData.put("status", "PENDING");
                        orderData.put("orderDate", System.currentTimeMillis());

                        double subtotal = calculateSubtotal(cartItems);
                        orderData.put("subtotal", subtotal);
                        orderData.put("deliveryFee", deliveryFee);
                        orderData.put("total", subtotal + deliveryFee);

                        // Salvar no Firestore
                        firestore.collection("orders")
                                .add(orderData)
                                .addOnSuccessListener(documentReference -> {
                                    cartManager.clearCart();
                                    Toast.makeText(CheckoutActivity.this,
                                            "Pedido realizado com sucesso!",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> showError("Erro ao processar pedido: " + e.getMessage()));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        showError("Erro ao buscar dados do usuário: " + error.getMessage());
                    }
                });
    }

    private String getSelectedPaymentMethod() {
        int selectedId = paymentOptions.getCheckedRadioButtonId();
        if (selectedId == R.id.radio_credit) {
            return "Cartão de Crédito";
        } else if (selectedId == R.id.radio_pix) {
            return "PIX";
        } else {
            return "Dinheiro";
        }
    }

    private double calculateSubtotal(List<CartItem> items) {
        double subtotal = 0;
        for (CartItem item : items) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        return subtotal;
    }

    private void showLoading(boolean show) {
        btnPlaceOrder.setEnabled(!show);
    }

    private void showError(String message) {
        showLoading(false);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}