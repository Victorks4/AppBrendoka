package com.example.appproject05.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.appproject05.AddressSelectionActivity;
import com.example.appproject05.EditarPerfilActivity;
import com.example.appproject05.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PerfilFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference database;
    private TextView tvNomePerfil;
    private TextView tvEmailPerfil;
    private TextView tvTelefone;
    private TextView tvCep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializando Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("usuarios");

        // Inicializando as referências
        tvNomePerfil = view.findViewById(R.id.txtNomePerfil);  // Usar o ID correto
        tvEmailPerfil = view.findViewById(R.id.txtEmailPerfil);  // Usar o ID correto
        tvTelefone = view.findViewById(R.id.tvTelefone);  // Usar o ID correto
        tvCep = view.findViewById(R.id.tvCep);  // Usar o ID correto

        // Configurar os botões
        setupButtons(view);

        // Carregar dados do perfil
        carregarDadosPerfil();

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

    private void carregarDadosPerfil() {
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual == null) {
            tvNomePerfil.setText("Usuário não logado");
            tvEmailPerfil.setText("");
            tvTelefone.setText("");
            tvCep.setText("");
            return;
        }

        String userId = usuarioAtual.getUid();
        database.child(userId).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String nome = dataSnapshot.child("nome").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String telefone = dataSnapshot.child("telefone").getValue(String.class);
                String cep = dataSnapshot.child("cep").getValue(String.class);

                tvNomePerfil.setText(nome != null ? nome : "Nome não encontrado");
                tvEmailPerfil.setText(email != null ? email : "Email não encontrado");
                tvTelefone.setText(telefone != null ? telefone : "Telefone não encontrado");
                tvCep.setText(cep != null ? cep : "CEP não encontrado");
            } else {
                tvNomePerfil.setText("Perfil não encontrado");
                tvEmailPerfil.setText("");
                tvTelefone.setText("Telefone não encontrado");
                tvCep.setText("CEP não encontrado");
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Erro ao carregar perfil: " + e.getMessage(), Toast.LENGTH_LONG).show();
            tvNomePerfil.setText("Erro ao carregar perfil");
            tvEmailPerfil.setText("");
            tvTelefone.setText("");
            tvCep.setText("");
        });
    }
}
