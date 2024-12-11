package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appproject05.models.AdminDashboardStats;
import com.example.appproject05.utils.AdminDashboardManager;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RelatoriosActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtTotalVendasPeriodo, txtTotalPedidosPeriodo;
    private TextView txtCrescimentoVendasPeriodo, txtCrescimentoPedidosPeriodo;
    private TextView txtPeriodoSelecionado;
    private FloatingActionButton fabSelecionarPeriodo;

    private AdminDashboardManager dashboardManager;
    private NumberFormat currencyFormatter;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);

        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));

        initViews();
        setupToolbar();
        setupDashboardManager();
        setupFabListener();

        // Verificar se recebeu datas da intent
        long dataInicial = getIntent().getLongExtra("DATA_INICIAL", -1);
        long dataFinal = getIntent().getLongExtra("DATA_FINAL", -1);

        if (dataInicial != -1 && dataFinal != -1) {
            carregarDadosRelatorios(dataInicial, dataFinal);
        } else {
            // Carregar dados padrão (mês atual)
            carregarDadosPadrao();
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        txtTotalVendasPeriodo = findViewById(R.id.txtTotalVendasPeriodo);
        txtTotalPedidosPeriodo = findViewById(R.id.txtTotalPedidosPeriodo);
        txtCrescimentoVendasPeriodo = findViewById(R.id.txtCrescimentoVendasPeriodo);
        txtCrescimentoPedidosPeriodo = findViewById(R.id.txtCrescimentoPedidosPeriodo);
        txtPeriodoSelecionado = findViewById(R.id.txtPeriodoSelecionado);
        fabSelecionarPeriodo = findViewById(R.id.fabSelecionarPeriodo);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Relatórios");
        }
    }

    private void setupDashboardManager() {
        dashboardManager = new AdminDashboardManager();
    }

    private void setupFabListener() {
        fabSelecionarPeriodo.setOnClickListener(v -> {
            Intent intent = new Intent(this, DataSelectionActivity.class);
            startActivity(intent);
        });
    }

    private void carregarDadosPadrao() {
        // Carregar dados do mês atual por padrão
        long dataInicial = getPrimeiroDiaDoMesAtual();
        long dataFinal = System.currentTimeMillis();
        carregarDadosRelatorios(dataInicial, dataFinal);
    }

    private void carregarDadosRelatorios(long dataInicial, long dataFinal) {
        // Atualizar texto do período
        txtPeriodoSelecionado.setText(String.format("%s a %s",
                dateFormat.format(dataInicial),
                dateFormat.format(dataFinal)
        ));

        dashboardManager.fetchDashboardStatsForDateRange(
                dataInicial,
                dataFinal,
                new AdminDashboardManager.DashboardStatsCallback() {
                    @Override
                    public void onStatsRetrieved(AdminDashboardStats stats) {
                        runOnUiThread(() -> {
                            // Atualizar vendas do período
                            txtTotalVendasPeriodo.setText(
                                    currencyFormatter.format(stats.getMonthlyRevenue())
                            );

                            // Atualizar total de pedidos
                            txtTotalPedidosPeriodo.setText(
                                    String.valueOf(stats.getMonthlyOrderCount())
                            );

                            // Atualizar crescimento de vendas
                            double crescimentoVendas = stats.getMonthlyRevenueGrowthPercentage();
                            txtCrescimentoVendasPeriodo.setText(
                                    String.format("%.1f%%", crescimentoVendas)
                            );
                            txtCrescimentoVendasPeriodo.setTextColor(
                                    crescimentoVendas >= 0 ?
                                            getResources().getColor(R.color.success) :
                                            getResources().getColor(R.color.error)
                            );

                            // Atualizar crescimento de pedidos
                            double crescimentoPedidos = stats.getMonthlyOrderGrowthPercentage();
                            txtCrescimentoPedidosPeriodo.setText(
                                    String.format("%.1f%%", crescimentoPedidos)
                            );
                            txtCrescimentoPedidosPeriodo.setTextColor(
                                    crescimentoPedidos >= 0 ?
                                            getResources().getColor(R.color.success) :
                                            getResources().getColor(R.color.error)
                            );
                        });
                    }
                }
        );
    }

    private long getPrimeiroDiaDoMesAtual() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
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