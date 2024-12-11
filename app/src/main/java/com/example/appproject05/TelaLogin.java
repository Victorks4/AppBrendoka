package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TelaLogin extends AppCompatActivity {
    private TextInputEditText edtEmail, edtSenha;
    private MaterialButton btnEntrar, btnEsqueceuSenha, btnCadastrar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Verificar se usuário já está logado
        verificarSessaoAtiva();

        initViews();
        setupListeners();
    }

    private void verificarSessaoAtiva() {
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual != null) {
            // Verificar se é o email de admin
            if (usuarioAtual.getEmail().equals("admin@teste.com")) {
                // Redirecionar para painel admin
                redirecionarParaPainelAdmin();
            } else {
                // Redirecionar para tela principal de usuário normal
                redirecionarParaTelaPrincipal();
            }
        }
    }

    private void initViews() {
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtSenha = findViewById(R.id.edtSenhaLogin);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnEsqueceuSenha = findViewById(R.id.btnEsqueceuSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
    }

    private void setupListeners() {
        btnEntrar.setOnClickListener(v -> {
            if (validarCampos()) {
                String email = edtEmail.getText().toString().trim();
                String senha = edtSenha.getText().toString().trim();

                // Verificar se é o administrador
                if (email.equals("admin@teste.com") && senha.equals("admin123")) {
                    redirecionarParaPainelAdmin();
                } else {
                    // Login com Firebase
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
                        redirecionarParaTelaPrincipal();
                    } else {
                        // Falha no login
                        Toast.makeText(TelaLogin.this,
                                "Erro ao fazer login: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void redirecionarParaTelaPrincipal() {
        Intent intent = new Intent(TelaLogin.this, TelaPrincipal.class);
        startActivity(intent);
        finish();
    }

    private void redirecionarParaPainelAdmin() {
        Intent intent = new Intent(TelaLogin.this, AdminPanelActivity.class);
        startActivity(intent);
        finish();
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