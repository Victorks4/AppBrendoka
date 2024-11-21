package com.example.appproject05;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class AddAddressActivity extends AppCompatActivity {
    private TextInputEditText editLabel, editCep, editStreet, editNumber;
    private TextInputEditText editComplement, editNeighborhood, editCity, editState;
    private SwitchMaterial switchDefault;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initViews();
        setupListeners();
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

    private void setupListeners() {
        editCep.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && editCep.length() == 8) {
                // TODO: Buscar CEP
                searchCep(editCep.getText().toString());
            }
        });

        btnSave.setOnClickListener(v -> saveAddress());
    }

    private void searchCep(String cep) {
        // Mock de busca de CEP
        Toast.makeText(this, "Buscando CEP: " + cep, Toast.LENGTH_SHORT).show();
    }

    private void saveAddress() {
        if (validateFields()) {
            // Mock de salvamento
            Toast.makeText(this, "Endereço salvo com sucesso!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
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