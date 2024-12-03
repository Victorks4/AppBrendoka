package com.example.appproject05;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditarPerfilActivity extends AppCompatActivity {

    private TextInputEditText edtNome, edtEmail, edtTelefone, edtCep;
    private MaterialButton btnSalvar;

    private FirebaseAuth auth; // Firebase Authentication
    private DatabaseReference database; // Realtime Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Inicializando Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("usuarios");

        // Inicializando os componentes da interface
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtCep = findViewById(R.id.edtCep);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Carregar dados do usuário atual
        carregarDadosUsuario();

        // Configurar ação do botão Salvar
        btnSalvar.setOnClickListener(v -> salvarAlteracoes());
    }

    private void carregarDadosUsuario() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Erro: Usuário não está logado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Preencher os campos com os dados do Firebase
                    String nome = snapshot.child("nome").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String telefone = snapshot.child("telefone").getValue(String.class);
                    String cep = snapshot.child("cep").getValue(String.class);

                    edtNome.setText(nome);
                    edtEmail.setText(email);
                    edtTelefone.setText(telefone);
                    edtCep.setText(cep);
                } else {
                    Toast.makeText(EditarPerfilActivity.this, "Dados do usuário não encontrados.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EditarPerfilActivity.this, "Erro ao carregar dados: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void salvarAlteracoes() {
        String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String telefone = edtTelefone.getText().toString().trim();
        String cep = edtCep.getText().toString().trim();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(telefone) || TextUtils.isEmpty(cep)) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Erro: Usuário não está logado!", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> updates = new HashMap<>();
        updates.put("nome", nome);
        updates.put("email", email);
        updates.put("telefone", telefone);
        updates.put("cep", cep);

        database.child(userId).updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
