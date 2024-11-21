package com.example.appproject05;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

// Criar nova classe EditarPerfilActivity.java:
public class EditarPerfilActivity extends AppCompatActivity {
    private TextInputEditText edtNome, edtEmail, edtTelefone, edtCep;
    private MaterialButton btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        initViews();
        setupToolbar();
        setupListeners();
    }

    private void initViews() {
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtCep = findViewById(R.id.edtCep);
        btnSalvar = findViewById(R.id.btnSalvar);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupListeners() {
        btnSalvar.setOnClickListener(v -> {
            if (validateFields()) {
                saveProfile();
            }
        });
    }

    private boolean validateFields() {
        // Implementar validações
        return true;
    }

    private void saveProfile() {
        // Implementar salvamento
        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}