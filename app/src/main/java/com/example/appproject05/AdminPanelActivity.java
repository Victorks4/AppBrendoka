package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appproject05.models.AdminDashboardStats;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminPanelActivity extends AppCompatActivity {
    private static final String TAG = "AdminPanelActivity";

    private TextView txtPedidosHoje;
    private TextView txtFaturamentoHoje;
    private TextView txtPedidosCrescimento;
    private TextView txtFaturamentoCrescimento;
    private MaterialCardView cardGerenciarProdutos;
    private MaterialCardView cardGerenciarPedidos;
    private MaterialCardView cardConfiguracoes;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initializeViews();
        setupClickListeners();
        loadDashboardData();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtPedidosHoje = findViewById(R.id.txtPedidosHoje);
        txtFaturamentoHoje = findViewById(R.id.txtFaturamentoHoje);
        txtPedidosCrescimento = findViewById(R.id.txtPedidosCrescimento);
        txtFaturamentoCrescimento = findViewById(R.id.txtFaturamentoCrescimento);
        cardGerenciarProdutos = findViewById(R.id.cardGerenciarProdutos);
        cardGerenciarPedidos = findViewById(R.id.cardGerenciarPedidos);
        cardConfiguracoes = findViewById(R.id.cardConfiguracoes);
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
            // TODO: Implementar tela de configurações
            Toast.makeText(this, "Configurações em desenvolvimento", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadDashboardData() {
        mDatabase.child("dashboard_stats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AdminDashboardStats stats = snapshot.getValue(AdminDashboardStats.class);
                if (stats != null) {
                    updateDashboardUI(stats);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadDashboardData:onCancelled", error.toException());
                Toast.makeText(AdminPanelActivity.this, "Erro ao carregar dados: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDashboardUI(AdminDashboardStats stats) {
        txtPedidosHoje.setText(String.valueOf(stats.getOrdersToday()));
        txtFaturamentoHoje.setText(String.format("R$ %.2f", stats.getRevenueToday()));
        txtPedidosCrescimento.setText(String.format("%.1f%%", stats.getOrdersGrowth()));
        txtFaturamentoCrescimento.setText(String.format("%.1f%%", stats.getRevenueGrowth()));
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