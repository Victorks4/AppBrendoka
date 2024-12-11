package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminPanelActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private MaterialCardView cardGerenciarProdutos;
    private MaterialCardView cardGerenciarPedidos;
    private MaterialCardView cardGerenciarUsuarios;
    private MaterialCardView cardConfiguracoes;
    private FloatingActionButton fabAddProduct;
    private TextView txtPedidosHoje;
    private TextView txtFaturamentoHoje;
    private TextView txtPedidosCrescimento;
    private TextView txtFaturamentoCrescimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_admin_panel);
            initViews();
            setupToolbar();
            setupClickListeners();
            loadDashboardData();
        } catch (Exception e) {
            e.printStackTrace();
            // Log o erro para debug
            Log.e("AdminPanelActivity", "Error in onCreate: " + e.getMessage());
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        cardGerenciarProdutos = findViewById(R.id.cardGerenciarProdutos);
        cardGerenciarPedidos = findViewById(R.id.cardGerenciarPedidos);
        cardConfiguracoes = findViewById(R.id.cardConfiguracoes);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        txtPedidosHoje = findViewById(R.id.txtPedidosHoje);
        txtFaturamentoHoje = findViewById(R.id.txtFaturamentoHoje);

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Painel Administrativo");
        }
    }

    private void setupClickListeners() {
        cardGerenciarProdutos.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, GerenciarProdutosActivity.class);
            startActivity(intent);
        });

        cardGerenciarPedidos.setOnClickListener(v -> {
            // TODO: Implementar navegação para tela de pedidos
        });

        cardGerenciarUsuarios.setOnClickListener(v -> {
            // TODO: Implementar navegação para tela de usuários
        });

        cardConfiguracoes.setOnClickListener(v -> {
            // TODO: Implementar navegação para configurações
        });

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, GerenciarProdutosActivity.class);
            intent.putExtra("action", "add");
            startActivity(intent);
        });
    }

    private void loadDashboardData() {
        // TODO: Carregar dados do Firebase/backend
        txtPedidosHoje.setText("15");
        txtFaturamentoHoje.setText("R$ 1.234,00");
        txtPedidosCrescimento.setText("+12.5%");
        txtFaturamentoCrescimento.setText("+8.3%");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}