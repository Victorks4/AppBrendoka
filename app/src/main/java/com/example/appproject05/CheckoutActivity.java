package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appproject05.models.Address;
import com.example.appproject05.models.CartItem;
import com.example.appproject05.utils.CartManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity implements CartManager.CartListener {
    private static final int REQUEST_CHANGE_ADDRESS = 200;
    private TextView txtSubtotal, txtDelivery, txtTotal;
    private TextView addressTitle, addressDetails;
    private RadioGroup paymentOptions;
    private MaterialButton btnChangeAddress, btnPlaceOrder;
    private CartManager cartManager;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private double subtotal = 0.0;
    private double deliveryFee = 5.0;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initializeViews();
        setupFirestore();
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

    private void setupFirestore() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private void setupCartManager() {
        cartManager = new CartManager();
        cartManager.setCartListener(this);
    }

    private void setupListeners() {
        btnChangeAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressSelectionActivity.class);
            startActivityForResult(intent, REQUEST_CHANGE_ADDRESS);
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
        loadDefaultAddress();
    }

    private void loadDefaultAddress() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("usuarios")
                    .child(currentUser.getUid());

            userRef.child("enderecos").orderByChild("default").equalTo(true)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Address defaultAddress = snapshot.getValue(Address.class);
                                if (defaultAddress != null) {
                                    updateSelectedAddress(defaultAddress);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(CheckoutActivity.this,
                                    "Erro ao carregar endereço padrão",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateSelectedAddress(Address address) {
        if (address != null) {
            addressTitle.setText(address.getLabel());
            addressDetails.setText(
                    address.getStreet() + ", " +
                            address.getNeighborhood() + " - " +
                            address.getCityState()
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_ADDRESS && resultCode == RESULT_OK) {
            Address selectedAddress = (Address) data.getSerializableExtra("selected_address");
            if (selectedAddress != null) {
                updateSelectedAddress(selectedAddress);
            }
        }
    }

    private void processOrder() {
        showLoading(true);
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            showError("Usuário não está logado");
            return;
        }

        cartItems = cartManager.getCartItems();
        if (cartItems.isEmpty()) {
            showError("Seu carrinho está vazio");
            return;
        }

        String userId = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String customerName = dataSnapshot.child("nome").getValue(String.class);
                    String customerPhone = dataSnapshot.child("telefone").getValue(String.class);

                    if (customerName == null || customerPhone == null) {
                        showError("Dados do cliente incompletos");
                        return;
                    }

                    Map<String, Object> orderData = new HashMap<>();
                    orderData.put("userId", userId);
                    orderData.put("customerName", customerName);
                    orderData.put("customerPhone", customerPhone);
                    orderData.put("items", cartItems);
                    orderData.put("address", addressDetails.getText().toString());
                    orderData.put("paymentMethod", getSelectedPaymentMethod());
                    orderData.put("status", "PENDING");
                    orderData.put("orderDate", System.currentTimeMillis());
                    orderData.put("subtotal", subtotal);
                    orderData.put("deliveryFee", deliveryFee);
                    orderData.put("total", subtotal + deliveryFee);

                    db.collection("orders")
                            .add(orderData)
                            .addOnSuccessListener(documentReference -> {
                                documentReference.update("orderId", documentReference.getId());

                                cartManager.clearCart()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(CheckoutActivity.this,
                                                    "Pedido realizado com sucesso!",
                                                    Toast.LENGTH_LONG).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                                showError("Erro ao limpar carrinho: " + e.getMessage()));
                            })
                            .addOnFailureListener(e ->
                                    showError("Erro ao processar pedido: " + e.getMessage()));
                } else {
                    showError("Dados do usuário não encontrados");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showError("Erro ao buscar dados do usuário: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onCartUpdated(List<CartItem> items, double total) {
        this.cartItems = items;
        this.subtotal = total;
        updateTotals();
    }

    @Override
    public void onError(String message) {
        showError(message);
    }

    private void updateTotals() {
        txtSubtotal.setText(String.format("R$ %.2f", subtotal));
        txtDelivery.setText(String.format("R$ %.2f", deliveryFee));
        double total = subtotal + deliveryFee;
        txtTotal.setText(String.format("R$ %.2f", total));
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

    private boolean validateOrder() {
        if (paymentOptions.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione uma forma de pagamento", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showChangeInputDialog() {
        Toast.makeText(this, "Informar troco", Toast.LENGTH_SHORT).show();
    }

    private void showLoading(boolean show) {
        btnPlaceOrder.setEnabled(!show);
    }

    private void showError(String message) {
        showLoading(false);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}