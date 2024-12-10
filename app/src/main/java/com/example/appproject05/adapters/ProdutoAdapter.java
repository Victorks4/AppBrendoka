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

    public void updateData(List<Product> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }

    class ProdutoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduto;
        TextView txtNomeProduto;
        TextView txtDescricaoProduto;
        TextView txtPrecoProduto;
        ImageButton btnEditarProduto;
        ImageButton btnRemoverProduto;

        ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduto = itemView.findViewById(R.id.imgProduto);
            txtNomeProduto = itemView.findViewById(R.id.txtNomeProduto);
            txtDescricaoProduto = itemView.findViewById(R.id.txtDescricaoProduto);
            btnEditarProduto = itemView.findViewById(R.id.btnEditarProduto);
            btnRemoverProduto = itemView.findViewById(R.id.btnRemoverProduto);
        }

        void bind(Product product) {
            txtNomeProduto.setText(product.getName());
            txtDescricaoProduto.setText(product.getDescription());
            txtPrecoProduto.setText(String.format("R$ %.2f", product.getPrice()));

            // Verificar se tem URL de imagem ou usar resource
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                // TODO: Implementar carregamento de imagem usando Glide ou Picasso
                imgProduto.setImageResource(R.drawable.ic_product); // Fallback
            } else if (product.getImageResource() != 0) {
                imgProduto.setImageResource(product.getImageResource());
            } else {
                imgProduto.setImageResource(R.drawable.ic_product);
            }

            btnEditarProduto.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(product, getAdapterPosition());
                }
            });

            btnRemoverProduto.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(product, getAdapterPosition());
                }
            });
        }
    }
}