package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView imgLogo;
    private TextView txtAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar views
        imgLogo = findViewById(R.id.imgLogo);
        txtAppName = findViewById(R.id.txtAppName);

        // Carregar animações
        Animation fadeScale = AnimationUtils.loadAnimation(this, R.anim.fade_scale);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // Aplicar animações
        imgLogo.startAnimation(fadeScale);
        txtAppName.startAnimation(slideUp);

        // Configurar listener para a última animação
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // Navegar para TelaLogin
                startActivity(new Intent(MainActivity.this, TelaLogin.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Iniciar a animação de saída após o delay
        imgLogo.postDelayed(() -> {
            imgLogo.startAnimation(fadeOut);
            txtAppName.startAnimation(fadeOut);
        }, 1400);
    }
}