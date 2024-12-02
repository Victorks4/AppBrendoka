package com.example.appproject05;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class TelaCadastro extends AppCompatActivity {
    private TextInputEditText edtNome, edtEmail, edtSenha, edtConfirmarSenha, edtTelefone, edtCep;
    private MaterialButton btnCadastrar;
    private FirebaseAuth auth; // Referência ao FirebaseAuth
    private DatabaseReference database; // Referência ao Realtime Database

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

        // Inicializar FirebaseAuth e Realtime Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("usuarios");

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    String email = edtEmail.getText().toString().trim();
                    String senha = edtSenha.getText().toString();

                    // Registrar usuário no Firebase
                    cadastrarUsuario(email, senha);
                }
            }
        });
    }

    private void cadastrarUsuario(String email, String senha) {
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sucesso no cadastro
                        String userId = auth.getCurrentUser().getUid();
                        salvarDadosNoBanco(userId);
                        Toast.makeText(TelaCadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        // Falha no cadastro
                        String mensagemErro = "Erro ao cadastrar!";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            mensagemErro = "A senha é muito fraca!";
                        } catch (FirebaseAuthUserCollisionException e) {
                            mensagemErro = "Este email já está cadastrado!";
                        } catch (Exception e) {
                            mensagemErro = e.getMessage();
                        }
                        Toast.makeText(TelaCadastro.this, mensagemErro, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void salvarDadosNoBanco(String userId) {
        String nome = edtNome.getText().toString().trim();
        String telefone = edtTelefone.getText().toString().trim();
        String cep = edtCep.getText().toString().trim();

        // Criar mapa com os dados do usuário
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nome", nome);
        usuario.put("email", edtEmail.getText().toString().trim());
        usuario.put("telefone", telefone);
        usuario.put("cep", cep);

        // Salvar no Realtime Database
        database.child(userId).setValue(usuario)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(TelaCadastro.this, "Dados salvos com sucesso no banco!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(TelaCadastro.this, "Erro ao salvar dados no banco: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
