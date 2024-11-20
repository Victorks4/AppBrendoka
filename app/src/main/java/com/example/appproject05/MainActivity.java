package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ImageView Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajuste de padding para lidar com as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referência para o ImageView (Ícone que será animado)
        Img = findViewById(R.id.Icon);

        // Carregar a animação definida em XML (zoom_fade.xml)
        Animation zoomFade = AnimationUtils.loadAnimation(this, R.anim.zoom_fade);

        // Adicionar listener para detectar o fim da animação
        zoomFade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Opcional: Código a executar quando a animação começa
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Navegar para a próxima tela após a animação
                Intent intent = new Intent(MainActivity.this, tela2.class);
                startActivity(intent);
                finish(); // Finaliza a atividade atual
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Opcional: Código para animações que se repetem
            }
        });

        // Iniciar a animação
        Img.startAnimation(zoomFade);
    }
}
