package com.example.appproject05;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appproject05.models.Address;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAddressActivity extends AppCompatActivity {
    private TextInputEditText editLabel, editCep, editStreet, editNumber;
    private TextInputEditText editComplement, editNeighborhood, editCity, editState;
    private SwitchMaterial switchDefault;
    private MaterialButton btnSave;

    private FirebaseAuth auth;
    private DatabaseReference addressesRef;
    private Address existingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        addressesRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(currentUser.getUid())
                .child("enderecos");

        initViews();
        setupListeners();

        // Verificar se está editando um endereço existente
        existingAddress = (Address) getIntent().getSerializableExtra("address");
        if (existingAddress != null) {
            populateFields();
        }
    }

    private void initViews() {
        editLabel = findViewById(R.id.edit_label);
        editCep = findViewById(R.id.edit_cep);
        editStreet = findViewById(R.id.edit_street);
        editNumber = findViewById(R.id.edit_number);
        editComplement = findViewById(R.id.edit_complement);
        editNeighborhood = findViewById(R.id.edit_neighborhood);
        editCity = findViewById(R.id.edit_city);
        editState = findViewById(R.id.edit_state);
        switchDefault = findViewById(R.id.switch_default);
        btnSave = findViewById(R.id.btn_save);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void populateFields() {
        editLabel.setText(existingAddress.getLabel());

        // Separar city e state do cityState
        String[] cityStateparts = existingAddress.getCityState().split(" - ");
        if (cityStateparts.length == 2) {
            editCity.setText(cityStateparts[0]);
            editState.setText(cityStateparts[1]);
        }

        editStreet.setText(existingAddress.getStreet());
        editComplement.setText(existingAddress.getComplement());
        switchDefault.setChecked(existingAddress.isDefault());
    }

    private void setupListeners() {
        editCep.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && editCep.length() == 8) {
                // TODO: Implement CEP search
                searchCep(editCep.getText().toString());
            }
        });

        btnSave.setOnClickListener(v -> saveAddress());
    }

    private void searchCep(String cep) {
        // Mock CEP search - replace with actual API call
        Toast.makeText(this, "Buscando CEP: " + cep, Toast.LENGTH_SHORT).show();
    }

    private void saveAddress() {
        if (!validateFields()) return;

        Address newAddress = new Address(
                editLabel.getText().toString(),
                editStreet.getText().toString(),
                editNeighborhood.getText().toString(),
                editCity.getText().toString() + " - " + editState.getText().toString(),
                editComplement.getText().toString(),
                switchDefault.isChecked()
        );

        if (existingAddress != null && existingAddress.getUid() != null) {
            // Atualizar endereço existente
            addressesRef.child(existingAddress.getUid()).setValue(newAddress)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Endereço atualizado!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao atualizar endereço", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Criar novo endereço
            addressesRef.push().setValue(newAddress)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Endereço salvo!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao salvar endereço", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private boolean validateFields() {
        if (editLabel.getText().toString().isEmpty()) {
            editLabel.setError("Campo obrigatório");
            return false;
        }
        if (editCep.getText().toString().isEmpty()) {
            editCep.setError("Campo obrigatório");
            return false;
        }
        if (editStreet.getText().toString().isEmpty()) {
            editStreet.setError("Campo obrigatório");
            return false;
        }
        if (editNumber.getText().toString().isEmpty()) {
            editNumber.setError("Campo obrigatório");
            return false;
        }
        if (editNeighborhood.getText().toString().isEmpty()) {
            editNeighborhood.setError("Campo obrigatório");
            return false;
        }
        if (editCity.getText().toString().isEmpty()) {
            editCity.setError("Campo obrigatório");
            return false;
        }
        if (editState.getText().toString().isEmpty()) {
            editState.setError("Campo obrigatório");
            return false;
        }
        return true;
    }
}