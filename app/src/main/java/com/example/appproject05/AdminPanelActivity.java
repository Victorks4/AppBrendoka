package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.appproject05.models.AdminDashboardStats;
import com.example.appproject05.utils.AdminDashboardManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.NumberFormat;
import java.util.Locale;

public class AdminPanelActivity extends AppCompatActivity {
    private static final String TAG = "AdminPanelActivity";

    private Toolbar toolbar;
    private MaterialCardView cardGerenciarProdutos;
    private MaterialCardView cardGerenciarPedidos;
    private MaterialCardView cardConfiguracoes;
    private FloatingActionButton fabAddProduct;

    private TextView txtPedidosHoje;
    private TextView txtFaturamentoHoje;
    private TextView txtPedidosCrescimento;
    private TextView txtFaturamentoCrescimento;

    private AdminDashboardManager dashboardManager;
    private NumberFormat currencyFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_admin_panel);

            // Initialize formatting
            currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            // Initialize dashboard manager
            dashboardManager = new AdminDashboardManager();

            initViews();
            setupToolbar();
            setupClickListeners();
            loadDashboardData();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
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
            // TODO: Implementar navegação para tela de pedidos
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
        dashboardManager.fetchDashboardStats(stats -> {
            // Update UI on main thread
            runOnUiThread(() -> {
                // Pedidos hoje
                txtPedidosHoje.setText(String.valueOf(stats.getDailyOrderCount()));
                txtPedidosCrescimento.setText(
                        String.format("%.1f%%", stats.getDailyOrderGrowthPercentage())
                );

                // Faturamento hoje
                txtFaturamentoHoje.setText(
                        currencyFormatter.format(stats.getDailyRevenue())
                );
                txtFaturamentoCrescimento.setText(
                        String.format("%.1f%%", stats.getMonthlyOrderGrowthPercentage())
                );
            });
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