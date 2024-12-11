package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appproject05.models.AdminDashboardStats;
import com.example.appproject05.utils.AdminDashboardManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.Locale;

public class AdminPanelActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private MaterialCardView cardGerenciarProdutos;
    private MaterialCardView cardGerenciarPedidos;
    private MaterialCardView cardConfiguracoes;
    private FloatingActionButton fabAddProduct;

    // Novas referências para ações rápidas
    private LinearLayout btnAddProduto;
    private LinearLayout btnVerPedidos;
    private LinearLayout btnRelatorios;

    private TextView txtPedidosHoje;
    private TextView txtFaturamentoHoje;
    private TextView txtPedidosCrescimento;
    private TextView txtFaturamentoCrescimento;

    private AdminDashboardManager dashboardManager;
    private NumberFormat currencyFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_panel);

            // Inicializar formatador de moeda
            currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            initViews();
            setupToolbar();
            setupClickListeners();
            setupQuickActionListeners();
            setupDashboardManager();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AdminPanelActivity", "Error in onCreate: " + e.getMessage());
        }
    }

    private void setupDashboardManager() {
        dashboardManager = new AdminDashboardManager();
        loadDashboardData();
    }

    private void loadDashboardData() {
        dashboardManager.fetchDashboardStats(new AdminDashboardManager.DashboardStatsCallback() {
            @Override
            public void onStatsRetrieved(AdminDashboardStats stats) {
                runOnUiThread(() -> {
                    // Atualizar pedidos de hoje
                    txtPedidosHoje.setText(String.valueOf(stats.getDailyOrderCount()));

                    // Atualizar faturamento de hoje
                    txtFaturamentoHoje.setText(currencyFormatter.format(stats.getDailyRevenue()));

                    // Atualizar crescimento de pedidos
                    txtPedidosCrescimento.setText(String.format("%.1f%%", stats.getDailyOrderGrowthPercentage()));
                    txtPedidosCrescimento.setTextColor(
                            stats.getDailyOrderGrowthPercentage() >= 0 ?
                                    getResources().getColor(R.color.success) :
                                    getResources().getColor(R.color.error)
                    );

                    // Atualizar crescimento de faturamento
                    txtFaturamentoCrescimento.setText(String.format("%.1f%%", stats.getDailyRevenueGrowthPercentage()));
                    txtFaturamentoCrescimento.setTextColor(
                            stats.getDailyRevenueGrowthPercentage() >= 0 ?
                                    getResources().getColor(R.color.success) :
                                    getResources().getColor(R.color.error)
                    );
                });
            }
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        cardGerenciarProdutos = findViewById(R.id.cardGerenciarProdutos);
        cardGerenciarPedidos = findViewById(R.id.cardGerenciarPedidos);
        cardConfiguracoes = findViewById(R.id.cardConfiguracoes);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        // Inicializar views de ações rápidas
        btnAddProduto = findViewById(R.id.btnAddProduto);
        btnVerPedidos = findViewById(R.id.btnVerPedidos);
        btnRelatorios = findViewById(R.id.btnRelatorios);

        txtPedidosHoje = findViewById(R.id.txtPedidosHoje);
        txtFaturamentoHoje = findViewById(R.id.txtFaturamentoHoje);
        txtPedidosCrescimento = findViewById(R.id.txtPedidosCrescimento);
        txtFaturamentoCrescimento = findViewById(R.id.txtFaturamentoCrescimento);
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
            Intent intent = new Intent(AdminPanelActivity.this, OrderManagementActivity.class);
            startActivity(intent);
        });

        cardConfiguracoes.setOnClickListener(v -> {
            Toast.makeText(this, "Configurações em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, GerenciarProdutosActivity.class);
            intent.putExtra("action", "add");
            startActivity(intent);
        });
    }

    private void setupQuickActionListeners() {
        // Ação rápida: Adicionar Produto
        btnAddProduto.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, GerenciarProdutosActivity.class);
            intent.putExtra("action", "add");
            startActivity(intent);
        });

        // Ação rápida: Ver Pedidos
        btnVerPedidos.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, OrderManagementActivity.class);
            startActivity(intent);
        });

        // Ação rápida: Relatórios
        btnRelatorios.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, RelatoriosActivity.class);
            startActivity(intent);
        });
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