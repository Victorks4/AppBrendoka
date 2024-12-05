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
import com.example.appproject05.models.Produto;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {
    private List<Produto> produtoList;

    public ProdutoAdapter(List<Produto> produtoList) {
        this.produtoList = produtoList;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = produtoList.get(position);
        holder.txtNomeProduto.setText(produto.getNome());
        holder.txtDescricaoProduto.setText(produto.getDescricao());
        // TODO: Implementar ações de editar e remover produto
    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduto;
        TextView txtNomeProduto;
        TextView txtDescricaoProduto;
        ImageButton btnEditarProduto;
        ImageButton btnRemoverProduto;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduto = itemView.findViewById(R.id.imgProduto);
            txtNomeProduto = itemView.findViewById(R.id.txtNomeProduto);
            txtDescricaoProduto = itemView.findViewById(R.id.txtDescricaoProduto);
            btnEditarProduto = itemView.findViewById(R.id.btnEditarProduto);
            btnRemoverProduto = itemView.findViewById(R.id.btnRemoverProduto);
        }
    }
}