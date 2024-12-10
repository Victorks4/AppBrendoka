package com.example.appproject05.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.models.Product;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onEditClick(Product product, int position);
        void onDeleteClick(Product product, int position);
    }

    public ProdutoAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProdutoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgProduto;
        private final TextView txtNomeProduto;
        private final TextView txtDescricaoProduto;
        private final TextView txtPrecoProduto;
        private final ImageButton btnEditarProduto;
        private final ImageButton btnRemoverProduto;

        ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduto = itemView.findViewById(R.id.imgProduto);
            txtNomeProduto = itemView.findViewById(R.id.txtNomeProduto);
            txtDescricaoProduto = itemView.findViewById(R.id.txtDescricaoProduto);
            txtPrecoProduto = itemView.findViewById(R.id.txtPrecoProduto);
            btnEditarProduto = itemView.findViewById(R.id.btnEditarProduto);
            btnRemoverProduto = itemView.findViewById(R.id.btnRemoverProduto);

            btnEditarProduto.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEditClick(productList.get(position), position);
                }
            });

            btnRemoverProduto.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(productList.get(position), position);
                }
            });
        }

        void bind(Product product) {
            // Aplicar verificação null para evitar NPE
            if (txtNomeProduto != null) {
                txtNomeProduto.setText(product.getName());
            }
            if (txtDescricaoProduto != null) {
                txtDescricaoProduto.setText(product.getDescription());
            }
            if (txtPrecoProduto != null) {
                txtPrecoProduto.setText(String.format("R$ %.2f", product.getPrice()));
            }
            if (imgProduto != null) {
                // Verificar imagem
                if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                    // TODO: Implementar carregamento de imagem
                    imgProduto.setImageResource(R.drawable.ic_product);
                } else if (product.getImageResource() != 0) {
                    imgProduto.setImageResource(product.getImageResource());
                } else {
                    imgProduto.setImageResource(R.drawable.ic_product);
                }
            }
        }
    }
}