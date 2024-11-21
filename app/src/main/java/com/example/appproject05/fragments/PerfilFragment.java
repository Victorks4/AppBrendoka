package com.example.appproject05.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.appproject05.AddressSelectionActivity;
import com.example.appproject05.EditarPerfilActivity;
import com.example.appproject05.R;
import com.google.android.material.button.MaterialButton;

public class PerfilFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        setupButtons(view);
        return view;
    }

    private void setupButtons(View view) {
        MaterialButton btnGerenciarEnderecos = view.findViewById(R.id.btnGerenciarEnderecos);
        MaterialButton btnConfiguracoes = view.findViewById(R.id.btnConfiguracoes);
        MaterialButton btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);

        btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
            startActivity(intent);
        });
        btnGerenciarEnderecos.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddressSelectionActivity.class);
            startActivity(intent);
        });

        btnConfiguracoes.setOnClickListener(v -> {
            // Usando Fragment Transaction ao invés de nova Activity
            ConfiguracoesFragment configFragment = new ConfiguracoesFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, configFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}