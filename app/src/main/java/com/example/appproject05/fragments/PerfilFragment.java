package com.example.appproject05.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.appproject05.AddAddressActivity;
import com.example.appproject05.EditarPerfilActivity;
import com.example.appproject05.R;
import com.example.appproject05.TelaLogin;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference database;
    private TextView tvNomePerfil, tvEmailPerfil, tvTelefone, tvCep;
    private MaterialButton btnSair; // Botão de logout

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializando Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("usuarios");

        // Inicializando as referências
        initViews(view);

        // Configurar os botões
        setupButtons(view);

        // Carregar dados do perfil em tempo real
        carregarDadosEmTempoReal();

        return view;
    }

    private void initViews(View view) {
        tvNomePerfil = view.findViewById(R.id.txtNomePerfil);
        tvEmailPerfil = view.findViewById(R.id.txtEmailPerfil);
        tvTelefone = view.findViewById(R.id.tvTelefoneValor);
        tvCep = view.findViewById(R.id.tvCepValor);
        btnSair = view.findViewById(R.id.btnSair); // Botão de logout
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
            Intent intent = new Intent(getActivity(), AddAddressActivity.class);
            startActivity(intent);
        });

        btnConfiguracoes.setOnClickListener(v -> {
            ConfiguracoesFragment configFragment = new ConfiguracoesFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, configFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Configurar botão de logout
        btnSair.setOnClickListener(v -> {
            deslogarConta();
        });
    }

    private void deslogarConta() {
        // Deslogar do Firebase
        auth.signOut();

        // Redirecionar para tela de login
        Intent intent = new Intent(getActivity(), TelaLogin.class);
        // Limpar todas as activities anteriores
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Encerrar a activity atual
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void carregarDadosEmTempoReal() {
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual == null) {
            tvNomePerfil.setText("Usuário não logado");
            return;
        }

        String userId = usuarioAtual.getUid();
        database.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nome = dataSnapshot.child("nome").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String telefone = dataSnapshot.child("telefone").getValue(String.class);
                    String cep = dataSnapshot.child("cep").getValue(String.class);

                    tvNomePerfil.setText(nome != null ? nome : "Nome não encontrado");
                    tvEmailPerfil.setText(email != null ? email : "Email não encontrado");
                    tvTelefone.setText(formatarTelefone(telefone));
                    tvCep.setText(formatarCep(cep));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Erro ao carregar dados: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Métodos de formatação existentes (formatarTelefone, formatarCep)
    private String formatarTelefone(String telefone) {
        if (telefone != null && telefone.length() == 11) {
            return String.format("(%s) %s-%s",
                    telefone.substring(0, 2),
                    telefone.substring(2, 7),
                    telefone.substring(7));
        }
        return telefone != null ? telefone : "Telefone inválido";
    }

    private String formatarCep(String cep) {
        if (cep != null && cep.length() == 8) {
            return String.format("%s-%s",
                    cep.substring(0, 5),
                    cep.substring(5));
        }
        return cep != null ? cep : "CEP inválido";
    }
}