package com.example.appproject05;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appproject05.models.Order;
import com.example.appproject05.utils.CartManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.UUID;

public class CheckoutActivity extends AppCompatActivity {
    private TextView txtSubtotal, txtDelivery, txtTotal;
    private TextView addressTitle, addressDetails;
    private RadioGroup paymentOptions;
    private MaterialButton btnChangeAddress, btnPlaceOrder;
    private CartManager cartManager;
    private DatabaseReference ordersRef;
    private double subtotal = 0.0;
    private double deliveryFee = 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        setupFirebase();
        setupCartManager();
        setupListeners();
        loadOrderData();
    }

    private void initViews() {
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
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
    }

    private void setupCartManager() {
        cartManager = new CartManager();
    }

    private void setupListeners() {
        btnChangeAddress.setOnClickListener(v -> {
            // Implementar seleção de endereço
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
        // TODO: Carregar endereço padrão do usuário
        addressTitle.setText("Casa");
        addressDetails.setText("Rua Exemplo, 123 - Bairro - Cidade");
    }

    private void showChangeInputDialog() {
        // TODO: Implementar diálogo para troco
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
        String orderId = UUID.randomUUID().toString();

        Order order = new Order(
                cartManager.getCartItems(),
                addressDetails.getText().toString(),
                getSelectedPaymentMethod()
        );
        order.setOrderId(orderId);
        order.setUserId(userId);

        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    cartManager.clearCart()
                            .addOnSuccessListener(aVoid2 -> {
                                Toast.makeText(this, "Pedido realizado com sucesso!", Toast.LENGTH_LONG).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Erro ao limpar carrinho: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao processar pedido: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    showLoading(false);
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

    private void showLoading(boolean show) {
        btnPlaceOrder.setEnabled(!show);
        // TODO: Implementar um ProgressBar se necessário
    }
}