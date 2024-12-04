package com.example.appproject05;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class TelaRecuperarSenha extends AppCompatActivity {
    private TextInputLayout inputLayoutEmail;
    private TextInputEditText edtEmailRecuperacao;
    private MaterialButton btnEnviar;
    private MaterialButton btnVoltar;
    private FirebaseAuth auth; // Referência ao FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        // Inicializar componentes
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        edtEmailRecuperacao = findViewById(R.id.edtEmailRecuperacao);
        btnEnviar = findViewById(R.id.btnEnviarRecuperacao);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Configurar listener do botão enviar
        btnEnviar.setOnClickListener(v -> {
            if (validarEmail()) {
                String email = edtEmailRecuperacao.getText().toString().trim();
                enviarEmailRecuperacao(email);
            }
        });

        // Configurar listener do botão voltar
        btnVoltar.setOnClickListener(v -> finish()); // Fecha a tela e retorna para a anterior
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
//     // envia o link para recuperar a senha
    private void enviarEmailRecuperacao(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(TelaRecuperarSenha.this,
                                "Email de recuperação enviado para: " + email,
                                Toast.LENGTH_LONG).show();
                        finish(); // Fecha a tela após sucesso
                    } else {
                        Toast.makeText(TelaRecuperarSenha.this,
                                "Erro ao enviar email de recuperação. Tente novamente.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
