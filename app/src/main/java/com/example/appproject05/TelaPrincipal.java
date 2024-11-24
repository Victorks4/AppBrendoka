package com.example.appproject05;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.appproject05.fragments.CartFragment;
import com.example.appproject05.fragments.HomeFragment;
import com.example.appproject05.fragments.BuscaFragment;
import com.example.appproject05.fragments.PedidosFragment;
import com.example.appproject05.fragments.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class TelaPrincipal extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tela_principal);

            bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnItemSelectedListener(navListener);

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        } catch (Exception e) {
            Log.e("TelaPrincipal", "Erro no onCreate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_search) {
            selectedFragment = new BuscaFragment();
        } else if (itemId == R.id.nav_orders) {
            selectedFragment = new PedidosFragment();
        } else if (itemId == R.id.nav_profile) {
            selectedFragment = new PerfilFragment();
        }
        else if (itemId == R.id.nav_cart) {
            selectedFragment = new CartFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }

        return true;
    };
}