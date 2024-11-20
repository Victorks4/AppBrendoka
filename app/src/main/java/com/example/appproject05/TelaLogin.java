package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class TelaLogin extends AppCompatActivity {
    private TextInputEditText edtEmail;
    private TextInputEditText edtSenha;
    private MaterialButton btnEntrar;
    private MaterialButton btnEsqueceuSenha;
    private MaterialButton btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        // Inicializar componentes
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtSenha = findViewById(R.id.edtSenhaLogin);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnEsqueceuSenha = findViewById(R.id.btnEsqueceuSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // Configurar listeners
        btnEntrar.setOnClickListener(v -> {
            if (validarCampos()) {
                Intent intent = new Intent(TelaLogin.this, TelaPrincipal.class);
                startActivity(intent);
                finish();
            }
        });

        btnEsqueceuSenha.setOnClickListener(v -> {
            Intent intent = new Intent(TelaLogin.this, TelaRecuperarSenha.class);
            startActivity(intent);
        });

        btnCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(TelaLogin.this, TelaCadastro.class);
            startActivity(intent);
        });
    }

    private boolean validarCampos() {
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError("Digite seu email");
            return false;
        }
        if (senha.isEmpty()) {
            edtSenha.setError("Digite sua senha");
            return false;
        }
        return true;
    }
}