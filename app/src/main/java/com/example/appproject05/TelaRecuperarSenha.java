package com.example.appproject05;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TelaRecuperarSenha extends AppCompatActivity {
    private TextInputLayout inputLayoutEmail;
    private TextInputEditText edtEmailRecuperacao;
    private MaterialButton btnEnviar;
    private MaterialButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        // Inicializar componentes
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        edtEmailRecuperacao = findViewById(R.id.edtEmailRecuperacao);
        btnEnviar = findViewById(R.id.btnEnviarRecuperacao);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Configurar listener do botão enviar
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarEmail()) {
                    // Aqui você implementaria a lógica real de recuperação de senha
                    // Por enquanto apenas mostraremos uma mensagem
                    Toast.makeText(TelaRecuperarSenha.this,
                            "Email de recuperação enviado para: " + edtEmailRecuperacao.getText().toString(),
                            Toast.LENGTH_LONG).show();
                    finish(); // Fecha a tela após enviar
                }
            }
        });

        // Configurar listener do botão voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Fecha a tela e retorna para a anterior
            }
        });
    }

    private boolean validarEmail() {
        String email = edtEmailRecuperacao.getText().toString().trim();

        if (email.isEmpty()) {
            inputLayoutEmail.setError("Digite seu email");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputLayoutEmail.setError("Digite um email válido");
            return false;
        }

        inputLayoutEmail.setError(null);
        return true;
    }
}