package com.example.appproject05;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataSelectionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtDataInicial, txtDataFinal;
    private MaterialButton btnDataInicial, btnDataFinal, btnBuscar;

    private Calendar dataInicial, dataFinal;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_selection);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));

        initViews();
        setupToolbar();
        setupDatePickers();
        setupBuscarButton();

        // Configurar datas padrão
        dataInicial = Calendar.getInstance();
        dataInicial.set(Calendar.DAY_OF_MONTH, 1);
        dataFinal = Calendar.getInstance();

        updateDateTexts();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        txtDataInicial = findViewById(R.id.txtDataInicial);
        txtDataFinal = findViewById(R.id.txtDataFinal);
        btnDataInicial = findViewById(R.id.btnDataInicial);
        btnDataFinal = findViewById(R.id.btnDataFinal);
        btnBuscar = findViewById(R.id.btnBuscar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Selecionar Período");
        }
    }

    private void setupDatePickers() {
        btnDataInicial.setOnClickListener(v -> mostrarDatePicker(true));
        btnDataFinal.setOnClickListener(v -> mostrarDatePicker(false));
    }

    private void mostrarDatePicker(boolean isDataInicial) {
        Calendar currentDate = isDataInicial ? dataInicial : dataFinal;

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    if (isDataInicial) {
                        dataInicial = selectedDate;
                    } else {
                        dataFinal = selectedDate;
                    }

                    updateDateTexts();
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void updateDateTexts() {
        txtDataInicial.setText(dateFormat.format(dataInicial.getTime()));
        txtDataFinal.setText(dateFormat.format(dataFinal.getTime()));
    }

    private void setupBuscarButton() {
        btnBuscar.setOnClickListener(v -> {
            Intent intent = new Intent(this, RelatoriosActivity.class);
            intent.putExtra("DATA_INICIAL", dataInicial.getTimeInMillis());
            intent.putExtra("DATA_FINAL", dataFinal.getTimeInMillis());
            startActivity(intent);
        });
    }
}