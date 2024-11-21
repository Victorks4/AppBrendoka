package com.example.appproject05.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.models.Bakery;
import java.util.List;

public class BakeryAdapter extends RecyclerView.Adapter<BakeryAdapter.BakeryViewHolder> {
    private List<Bakery> bakeries;
    private OnBakeryClickListener listener;

    public BakeryAdapter(List<Bakery> bakeries, OnBakeryClickListener listener) {
        this.bakeries = bakeries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BakeryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bakery_card, parent, false);
        return new BakeryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BakeryViewHolder holder, int position) {
        holder.bind(bakeries.get(position));
    }

    @Override
    public int getItemCount() {
        return bakeries.size();
    }

    class BakeryViewHolder extends RecyclerView.ViewHolder {
        private ImageView bakeryImage;
        private TextView bakeryName;
        private TextView bakeryRating;
        private TextView deliveryTime;
        private TextView deliveryFee;

        BakeryViewHolder(@NonNull View itemView) {
            super(itemView);
            bakeryImage = itemView.findViewById(R.id.bakeryImage);
            bakeryName = itemView.findViewById(R.id.bakeryName);
            bakeryRating = itemView.findViewById(R.id.bakeryRating);
            deliveryTime = itemView.findViewById(R.id.deliveryTime);
            deliveryFee = itemView.findViewById(R.id.deliveryFee);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBakeryClick(bakeries.get(pos));
                }
            });
        }

        void bind(Bakery bakery) {
            bakeryName.setText(bakery.getName());
            bakeryRating.setText(String.format("%.1f", bakery.getRating()));
            deliveryTime.setText(bakery.getDeliveryTime());
            deliveryFee.setText(bakery.getDeliveryFee());
            // Aqui você usaria Glide para carregar a imagem
            // Glide.with(itemView).load(bakery.getImageUrl()).into(bakeryImage);
        }
    }

    public interface OnBakeryClickListener {
        void onBakeryClick(Bakery bakery);
    }
}