package com.example.appproject05;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.adapters.ProdutoAdapter;
import com.example.appproject05.models.Produto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class GerenciarProdutosActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProdutos;
    private FloatingActionButton fabAddProduct;
    private ProdutoAdapter produtoAdapter;
    private List<Produto> produtoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_produtos);

        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        produtoList = new ArrayList<>();
        // Adicionar produtos de exemplo
        produtoList.add(new Produto("Produto 1", "Descrição do Produto 1"));
        produtoList.add(new Produto("Produto 2", "Descrição do Produto 2"));

        produtoAdapter = new ProdutoAdapter(produtoList);
        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProdutos.setAdapter(produtoAdapter);

        fabAddProduct.setOnClickListener(v -> {
            // TODO: Implementar adição de novo produto
        });
    }
}