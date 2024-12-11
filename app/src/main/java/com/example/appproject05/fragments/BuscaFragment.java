package com.example.appproject05.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.adapters.ProductAdapter;
import com.example.appproject05.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuscaFragment extends Fragment {
    private RecyclerView searchResultsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> allProducts;
    private EditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busca, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);

        setupSearchView();
        setupRecyclerView();
        loadAllProducts();

        return view;
    }

    private void setupSearchView() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRecyclerView() {
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter(new ArrayList<>()); // Remove o callback
        searchResultsRecyclerView.setAdapter(productAdapter);
    }

    private void loadAllProducts() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allProducts = new ArrayList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    String id = productSnapshot.getKey();
                    String name = productSnapshot.child("name").getValue(String.class);
                    String description = productSnapshot.child("description").getValue(String.class);
                    double price = productSnapshot.child("price").getValue(Double.class);
                    String category = productSnapshot.child("category").getValue(String.class);
                    int imageResource = productSnapshot.child("imageResource").getValue(Integer.class);
                    boolean active = productSnapshot.child("active").getValue(Boolean.class);
                    long createdAt = productSnapshot.child("createdAt").getValue(Long.class);

                    if (active) {
                        allProducts.add(new Product(id, name, description, price, category, imageResource, createdAt, active));
                    }
                }
                productAdapter.updateProducts(allProducts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void filterProducts(String query) {
        if (query.isEmpty()) {
            productAdapter.updateProducts(allProducts);
            return;
        }

        List<Product> filteredList = allProducts.stream()
                .filter(product ->
                        product.getName().toLowerCase().contains(query.toLowerCase()) ||
                                product.getDescription().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        productAdapter.updateProducts(filteredList);
    }
}