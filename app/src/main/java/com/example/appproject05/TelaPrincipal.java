package com.example.appproject05;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class TelaPrincipal extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private FragmentManager fragmentManager;
    private Fragment homeFragment, perfilFragment, configuracoesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        // Inicializar o FragmentManager
        fragmentManager = getSupportFragmentManager();

        // Inicializar os fragmentos
        homeFragment = new HomeFragment();
        perfilFragment = new PerfilFragment();
        configuracoesFragment = new ConfiguracoesFragment();

        // Configurar o BottomNavigationView
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                // Remover animação de transição padrão
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Ocultar todos os fragmentos primeiro
                if (homeFragment.isAdded()) transaction.hide(homeFragment);
                if (perfilFragment.isAdded()) transaction.hide(perfilFragment);
                if (configuracoesFragment.isAdded()) transaction.hide(configuracoesFragment);

                // Mostrar o fragmento selecionado
                if (itemId == R.id.nav_home) {
                    if (!homeFragment.isAdded()) {
                        transaction.add(R.id.fragment_container, homeFragment);
                    }
                    transaction.show(homeFragment);
                    setTitle("Início");
                    return true;
                } else if (itemId == R.id.nav_perfil) {
                    if (!perfilFragment.isAdded()) {
                        transaction.add(R.id.fragment_container, perfilFragment);
                    }
                    transaction.show(perfilFragment);
                    setTitle("Perfil");
                    return true;
                } else if (itemId == R.id.nav_configuracoes) {
                    if (!configuracoesFragment.isAdded()) {
                        transaction.add(R.id.fragment_container, configuracoesFragment);
                    }
                    transaction.show(configuracoesFragment);
                    setTitle("Configurações");
                    return true;
                }

                transaction.commit();
                return false;
            }
        });

        // Configurar o fragmento inicial
        if (savedInstanceState == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, homeFragment);
            transaction.commit();
            setTitle("Início");
        }
    }

    // Método para lidar com o botão de voltar
    @Override
    public void onBackPressed() {
        // Se não estiver no fragmento home, voltar para ele
        if (!homeFragment.isVisible()) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        } else {
            // Se estiver no home, comportamento padrão do botão voltar
            super.onBackPressed();
        }
    }

    // Método auxiliar para trocar o título da ActionBar
    private void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}