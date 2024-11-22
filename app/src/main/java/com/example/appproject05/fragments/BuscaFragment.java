package com.example.appproject05.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.adapters.ProductAdapter;
import com.example.appproject05.models.Product;
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
        productAdapter = new ProductAdapter(new ArrayList<>(), product -> {
            // Implementar navegação para detalhes do produto
        });
        searchResultsRecyclerView.setAdapter(productAdapter);
    }

    private void loadAllProducts() {
        allProducts = new ArrayList<>();
        // Adicionar produtos mock para teste
        allProducts.add(new Product("1", "Pão Francês", "Pão fresquinho tradicional", 0.50, R.drawable.ic_bread));
        allProducts.add(new Product("2", "Croissant", "Croissant folhado", 5.00, R.drawable.ic_bread));
        allProducts.add(new Product("3", "Bolo de Chocolate", "Bolo caseiro", 25.00, R.drawable.ic_cake));
        allProducts.add(new Product("4", "Café Expresso", "Café premium", 3.50, R.drawable.ic_coffee));
        productAdapter.updateProducts(allProducts);
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