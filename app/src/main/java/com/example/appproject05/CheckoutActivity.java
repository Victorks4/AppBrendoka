package com.example.appproject05;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class CheckoutActivity extends AppCompatActivity {
    private TextView txtSubtotal, txtDelivery, txtTotal;
    private TextView addressTitle, addressDetails;
    private RadioGroup paymentOptions;
    private MaterialButton btnChangeAddress, btnPlaceOrder;
    private double subtotal = 0.0;
    private double deliveryFee = 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
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

    private void setupListeners() {
        btnChangeAddress.setOnClickListener(v -> {
            // TODO: Abrir seleção de endereço
            Toast.makeText(this, "Trocar endereço", Toast.LENGTH_SHORT).show();
        });

        paymentOptions.setOnCheckedChangeListener((group, checkedId) -> {
            // Atualizar UI baseado na forma de pagamento selecionada
            if (checkedId == R.id.radio_money) {
                showChangeInputDialog();
            }
        });

        btnPlaceOrder.setOnClickListener(v -> {
            if (validateOrder()) {
                placeOrder();
            }
        });
    }

    private void loadOrderData() {
        // TODO: Carregar dados do carrinho
        subtotal = getIntent().getDoubleExtra("subtotal", 0.0);
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
        // TODO: Mostrar dialog para troco
        Toast.makeText(this, "Informar troco", Toast.LENGTH_SHORT).show();
    }

    private boolean validateOrder() {
        if (paymentOptions.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione uma forma de pagamento", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void placeOrder() {
        // TODO: Processar pedido
        Toast.makeText(this, "Pedido realizado com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void showLoading(boolean show) {
        // TODO: Implementar loading state
        btnPlaceOrder.setEnabled(!show);
    }
}