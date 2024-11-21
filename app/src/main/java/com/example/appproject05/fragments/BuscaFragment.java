package com.example.appproject05.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.adapters.BakeryAdapter;
import com.example.appproject05.models.Bakery;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class BuscaFragment extends Fragment {
    private RecyclerView searchResultsRecyclerView;
    private BakeryAdapter bakeryAdapter;
    private List<Bakery> bakeries;
    private TextInputEditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busca, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);

        setupRecyclerView();
        setupSearch();

        return view;
    }

    private void setupRecyclerView() {
        bakeries = new ArrayList<>();
        bakeryAdapter = new BakeryAdapter(bakeries, bakery -> {
            // Implementar navegação para detalhes da padaria
        });
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultsRecyclerView.setAdapter(bakeryAdapter);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBakeries(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchBakeries(String query) {
        // Simular busca
        bakeries.clear();
        if (!query.isEmpty()) {
            bakeries.add(new Bakery("1", "Padaria " + query, "", 4.5f, "30-45 min", "R$ 5,00", true));
        }
        bakeryAdapter.notifyDataSetChanged();
    }
}