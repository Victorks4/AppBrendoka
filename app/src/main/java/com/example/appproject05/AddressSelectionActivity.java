package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject05.adapters.AddressAdapter;
import com.example.appproject05.models.Address;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class AddressSelectionActivity extends AppCompatActivity implements AddressAdapter.AddressClickListener {
    private static final int REQUEST_ADD_ADDRESS = 100;
    private RecyclerView recyclerView;
    private AddressAdapter adapter;
    private List<Address> addresses;
    private MaterialButton btnAddAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selection);
        initViews();
        setupRecyclerView();
        loadAddresses();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        btnAddAddress.setOnClickListener(v -> startAddAddress());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.address_recycler);
        btnAddAddress = findViewById(R.id.btn_add_address);
    }

    private void setupRecyclerView() {
        addresses = new ArrayList<>();
        adapter = new AddressAdapter(addresses, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadAddresses() {
        addresses.clear();
        addresses.add(new Address("Casa", "Rua A, 123", "Centro", "São Paulo - SP", "Apto 101", true));
        addresses.add(new Address("Trabalho", "Rua B, 456", "Jardins", "São Paulo - SP", "", false));
        adapter.notifyDataSetChanged();
    }

    private void startAddAddress() {
        Intent intent = new Intent(this, AddAddressActivity.class);
        startActivityForResult(intent, REQUEST_ADD_ADDRESS);
    }

    @Override
    public void onAddressSelected(Address address) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selected_address", address);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onAddressEdit(Address address) {
        Intent intent = new Intent(this, AddAddressActivity.class);
        intent.putExtra("address", address);
        startActivityForResult(intent, REQUEST_ADD_ADDRESS);
    }

    @Override
    public void onAddressDelete(Address address) {
        addresses.remove(address);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_ADDRESS && resultCode == RESULT_OK) {
            loadAddresses();
        }
    }
}