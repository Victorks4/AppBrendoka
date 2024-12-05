package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        cardGerenciarProdutos = findViewById(R.id.cardGerenciarProdutos);

        cardGerenciarProdutos.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, GerenciarProdutosActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        cardGerenciarProdutos = findViewById(R.id.cardGerenciarProdutos);
        cardGerenciarPedidos = findViewById(R.id.cardGerenciarPedidos);
        cardGerenciarUsuarios = findViewById(R.id.cardGerenciarUsuarios);
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
            // TODO: Implementar navegação para tela de produtos
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
            // TODO: Implementar adição de produto
        });
    }

    private void loadDashboardData() {
        // TODO: Carregar dados do Firebase/backend
        txtPedidosHoje.setText("15");
        txtFaturamentoHoje.setText("R$ 1.234,00");
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