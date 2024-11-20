package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class TelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        MaterialButton btnCadastrar = findViewById(R.id.btnCadastrar);
        MaterialButton btnEntrar = findViewById(R.id.btnEntrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaInicial.this, TelaCadastro.class));
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui você pode adicionar a navegação para uma tela de login
                // Por enquanto vamos só mostrar a tela de cadastro
                startActivity(new Intent(TelaInicial.this, TelaCadastro.class));
            }
        });
    }
}