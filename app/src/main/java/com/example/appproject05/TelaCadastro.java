package com.example.appproject05;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TelaCadastro extends AppCompatActivity {
    private TextInputEditText edtNome, edtEmail, edtSenha, edtConfirmarSenha, edtTelefone, edtCep;
    private MaterialButton btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        // Inicializando componentes
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmarSenha = findViewById(R.id.edtConfirmarSenha);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtCep = findViewById(R.id.edtCep);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    // Aqui você implementaria a lógica de cadastro
                    Toast.makeText(TelaCadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private boolean validarCampos() {
        boolean isValido = true;

        // Validar Nome
        if (edtNome.getText().toString().trim().isEmpty()) {
            edtNome.setError("Digite seu nome");
            isValido = false;
        }

        // Validar Email
        String email = edtEmail.getText().toString().trim();
        if (email.isEmpty()) {
            edtEmail.setError("Digite seu email");
            isValido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Digite um email válido");
            isValido = false;
        }

        // Validar Senha
        String senha = edtSenha.getText().toString();
        if (senha.isEmpty()) {
            edtSenha.setError("Digite uma senha");
            isValido = false;
        } else if (senha.length() < 6) {
            edtSenha.setError("A senha deve ter pelo menos 6 caracteres");
            isValido = false;
        }

        // Validar Confirmação de Senha
        String confirmarSenha = edtConfirmarSenha.getText().toString();
        if (confirmarSenha.isEmpty()) {
            edtConfirmarSenha.setError("Confirme sua senha");
            isValido = false;
        } else if (!confirmarSenha.equals(senha)) {
            edtConfirmarSenha.setError("As senhas não coincidem");
            isValido = false;
        }

        // Validar Telefone
        if (edtTelefone.getText().toString().trim().isEmpty()) {
            edtTelefone.setError("Digite seu telefone");
            isValido = false;
        }

        // Validar CEP
        String cep = edtCep.getText().toString().trim();
        if (cep.isEmpty()) {
            edtCep.setError("Digite seu CEP");
            isValido = false;
        } else if (cep.length() != 8) {
            edtCep.setError("CEP inválido");
            isValido = false;
        }

        return isValido;
    }
}