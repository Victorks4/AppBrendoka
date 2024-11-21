package com.example.appproject05.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.appproject05.AddressSelectionActivity;
import com.example.appproject05.R;
import com.google.android.material.button.MaterialButton;

public class PerfilFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);

    }
    // Em PerfilFragment.java, adicionar no método onCreateView():

    private void setupAddressManagement(View view) {
        MaterialButton btnGerenciarEnderecos = view.findViewById(R.id.btnGerenciarEnderecos);
        btnGerenciarEnderecos.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddressSelectionActivity.class);
            startActivity(intent);
        });
    }
}