package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class TelaLogin extends AppCompatActivity {
    private TextInputEditText edtEmail;
    private TextInputEditText edtSenha;
    private MaterialButton btnEntrar;
    private MaterialButton btnEsqueceuSenha;
    private MaterialButton btnCadastrar;

    // Referência ao FirebaseAuth
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Inicializar componentes
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtSenha = findViewById(R.id.edtSenhaLogin);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnEsqueceuSenha = findViewById(R.id.btnEsqueceuSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // Configurar listeners
        btnEntrar.setOnClickListener(v -> {
            if (validarCampos()) {
                String email = edtEmail.getText().toString().trim();
                String senha = edtSenha.getText().toString().trim();

                // Verificar se é o administrador
                if (email.equals("admin@teste.com") && senha.equals("admin123")) {
                    // Redirecionar para a tela de administrador
                    Intent intent = new Intent(TelaLogin.this, AdminPanelActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Realizar login com Firebase
                    loginUsuario(email, senha);
                }
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

    private void loginUsuario(String email, String senha) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        Intent intent = new Intent(TelaLogin.this, TelaPrincipal.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Falha no login
                        Toast.makeText(TelaLogin.this, "Erro ao fazer login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
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
