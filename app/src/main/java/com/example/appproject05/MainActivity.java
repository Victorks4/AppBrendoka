package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            configureEdgeToEdge();
            setContentView(R.layout.activity_main);

            setupWindowInsets();
            initializeViews();
            startIntroAnimation();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao iniciar a atividade: " + e.getMessage(), e);
            fallbackToLoginScreen();
        }
    }

    private void configureEdgeToEdge() {
        EdgeToEdge.enable(this);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        img = findViewById(R.id.Icon);
    }

    private void startIntroAnimation() {
        // Carregar a animação aprimorada
        Animation impactfulIntro = AnimationUtils.loadAnimation(this, R.anim.impactful_intro);

        impactfulIntro.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "Animação iniciada");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                navigateToLoginScreen();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Não usamos repetição neste caso
            }
        });

        img.startAnimation(impactfulIntro);
    }

    private void navigateToLoginScreen() {
        Intent intent = new Intent(MainActivity.this, TelaLogin.class);
        startActivity(intent);

        // Adiciona uma transição suave entre as telas
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        finish();
    }

    private void fallbackToLoginScreen() {
        Log.w(TAG, "Redirecionando para a tela de login devido a um erro crítico");
        navigateToLoginScreen();
    }
}
