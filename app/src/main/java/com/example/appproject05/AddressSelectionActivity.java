package com.example.appproject05;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject05.adapters.AddressAdapter;
import com.example.appproject05.models.Address;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectionActivity extends AppCompatActivity implements AddressAdapter.AddressClickListener {
    private static final int REQUEST_ADD_ADDRESS = 100;
    private RecyclerView recyclerView;
    private AddressAdapter adapter;
    private List<Address> addresses;
    private MaterialButton btnAddAddress;
    private FirebaseAuth auth;
    private DatabaseReference addressesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selection);

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
        addressesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addresses.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Address address = snapshot.getValue(Address.class);
                    if (address != null) {
                        address.setUid(snapshot.getKey());
                        addresses.add(address);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddressSelectionActivity.this,
                        "Erro ao carregar endereços", Toast.LENGTH_SHORT).show();
            }
        });
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
        if (address.getUid() != null) {
            addressesRef.child(address.getUid()).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Endereço removido", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erro ao remover endereço", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_ADDRESS && resultCode == RESULT_OK) {
            loadAddresses();
        }
    }
}