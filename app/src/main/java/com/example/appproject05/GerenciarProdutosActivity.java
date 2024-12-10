package com.example.appproject05;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.adapters.ProdutoAdapter;
import com.example.appproject05.models.Product;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;

public class GerenciarProdutosActivity extends AppCompatActivity implements ProdutoAdapter.OnProductClickListener {
    private RecyclerView recyclerViewProdutos;
    private ProdutoAdapter produtoAdapter;
    private List<Product> productList;
    private FloatingActionButton fabAddProduct;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_produtos);

        // Inicializar Firebase
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        // Inicializar views
        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        // Configurar RecyclerView
        productList = new ArrayList<>();
        produtoAdapter = new ProdutoAdapter(productList, this); // Agora passando this como listener
        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProdutos.setAdapter(produtoAdapter);

        // Configurar FAB
        fabAddProduct.setOnClickListener(v -> showAddProductDialog());

        // Carregar produtos
        loadProducts();
    }

    private void loadProducts() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        product.setId(snapshot.getKey());
                        productList.add(product);
                    }
                }
                produtoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GerenciarProdutosActivity.this,
                        "Erro ao carregar produtos: " + databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddProductDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        TextInputEditText edtName = dialogView.findViewById(R.id.edtProductName);
        TextInputEditText edtDescription = dialogView.findViewById(R.id.edtProductDescription);
        TextInputEditText edtPrice = dialogView.findViewById(R.id.edtProductPrice);
        TextInputEditText edtCategory = dialogView.findViewById(R.id.edtProductCategory);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Adicionar Produto")
                .setView(dialogView)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String name = edtName.getText().toString();
                    String description = edtDescription.getText().toString();
                    String priceStr = edtPrice.getText().toString();
                    String category = edtCategory.getText().toString();

                    if (!name.isEmpty() && !description.isEmpty() && !priceStr.isEmpty()) {
                        double price = Double.parseDouble(priceStr);
                        Product newProduct = new Product(name, description, price, category);
                        addProduct(newProduct);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void addProduct(Product product) {
        String productId = productsRef.push().getKey();
        if (productId != null) {
            product.setId(productId);
            productsRef.child(productId).setValue(product)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(this, "Produto adicionado com sucesso", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Erro ao adicionar produto: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onEditClick(Product product, int position) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        TextInputEditText edtName = dialogView.findViewById(R.id.edtProductName);
        TextInputEditText edtDescription = dialogView.findViewById(R.id.edtProductDescription);
        TextInputEditText edtPrice = dialogView.findViewById(R.id.edtProductPrice);
        TextInputEditText edtCategory = dialogView.findViewById(R.id.edtProductCategory);

        // Preencher dados atuais
        edtName.setText(product.getName());
        edtDescription.setText(product.getDescription());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtCategory.setText(product.getCategory());

        new MaterialAlertDialogBuilder(this)
                .setTitle("Editar Produto")
                .setView(dialogView)
                .setPositiveButton("Atualizar", (dialog, which) -> {
                    product.setName(edtName.getText().toString());
                    product.setDescription(edtDescription.getText().toString());
                    product.setPrice(Double.parseDouble(edtPrice.getText().toString()));
                    product.setCategory(edtCategory.getText().toString());

                    productsRef.child(product.getId()).setValue(product)
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(this, "Produto atualizado com sucesso", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Erro ao atualizar produto: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDeleteClick(Product product, int position) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Deseja realmente excluir este produto?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    productsRef.child(product.getId()).removeValue()
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(this, "Produto excluído com sucesso", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Erro ao excluir produto: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Não", null)
                .show();
    }
}