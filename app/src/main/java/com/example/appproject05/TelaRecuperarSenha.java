package com.example.appproject05;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TelaRecuperarSenha extends AppCompatActivity {
    private EditText edtEmailRecuperacao;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailRecuperacao.getText().toString().trim();

                if (email.isEmpty()) {
                    edtEmailRecuperacao.setError("Digite seu email");
                    return;
                }

                // Aqui você pode adicionar sua lógica de recuperação de senha
                Toast.makeText(TelaRecuperarSenha.this,
                        "Email de recuperação enviado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}