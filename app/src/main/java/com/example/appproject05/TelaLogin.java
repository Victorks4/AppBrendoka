package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TelaLogin extends AppCompatActivity {
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnEntrar;
    private Button btnEsqueceuSenha;
    private Button btnCadastrar;

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

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    Intent intent = new Intent(TelaLogin.this, TelaPrincipal.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaLogin.this, TelaRecuperarSenha.class);
                startActivity(intent);
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaLogin.this, tela2.class);
                startActivity(intent);
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