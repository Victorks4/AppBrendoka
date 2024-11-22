package com.example.appproject05.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.appproject05.R;
import com.example.appproject05.adapters.*;
import com.example.appproject05.models.*;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements
        BannerAdapter.OnBannerClickListener,
        CategoryAdapter.OnCategoryClickListener,
        ProductAdapter.OnProductClickListener {

    private ViewPager2 bannerViewPager;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView productsRecyclerView;
    private TextView bakeryNameText;
    private TextView bakeryInfoText;
    private List<BannerItem> banners = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(view);
        setupRecyclerViews();
        loadMockData();
        setupBakeryInfo();

        return view;
    }

    private void initializeViews(View view) {
        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
        bakeryNameText = view.findViewById(R.id.bakeryNameText);
        bakeryInfoText = view.findViewById(R.id.bakeryInfoText);
    }

    private void setupRecyclerViews() {
        bannerViewPager.setOffscreenPageLimit(1);

        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Configurando grid de 2 colunas para produtos
        productsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void setupBakeryInfo() {
        bakeryNameText.setText("Padaria do João");
        bakeryInfoText.setText("Aberto • 06:00 - 22:00");
    }

    private void loadMockData() {
        loadMockBanners();
        loadMockCategories();
        loadMockProducts();
    }

    private void loadMockBanners() {
        banners.clear();
        banners.add(new BannerItem("1", R.drawable.banner1, "Pães Fresquinhos", "Todos os dias a partir das 6h"));
        banners.add(new BannerItem("2", R.drawable.banner2, "Promoção de Doces", "50% off em todos os doces"));

        List<Integer> bannerImages = new ArrayList<>();
        for (BannerItem banner : banners) {
            bannerImages.add(banner.getImageResource());
        }

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages, this);
        bannerViewPager.setAdapter(bannerAdapter);
    }

    private void loadMockCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("1", "Pães", R.drawable.ic_bread));
        categories.add(new Category("2", "Bolos", R.drawable.ic_cake));
        categories.add(new Category("3", "Doces", R.drawable.ic_candy));
        categories.add(new Category("4", "Salgados", R.drawable.ic_snack));
        categories.add(new Category("5", "Cafés", R.drawable.ic_coffee));
        categories.add(new Category("6", "Sanduíches", R.drawable.ic_sandwich));

        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, this);
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void loadMockProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("1", "Pão Francês", "Pão fresquinho e crocante", 0.50, R.drawable.ic_bread));
        products.add(new Product("2", "Croissant", "Croissant folhado", 5.00, R.drawable.ic_bread));
        products.add(new Product("3", "Bolo de Chocolate", "Bolo caseiro", 25.00, R.drawable.ic_cake));
        products.add(new Product("4", "Café Expresso", "Café premium", 3.50, R.drawable.ic_coffee));
        products.add(new Product("5", "Coxinha", "Coxinha de frango", 4.50, R.drawable.ic_snack));
        products.add(new Product("6", "Pão de Queijo", "Quentinho", 2.50, R.drawable.ic_bread));

        ProductAdapter productAdapter = new ProductAdapter(products, this);
        productsRecyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onBannerClick(int position) {
        BannerItem banner = banners.get(position);
        // Implementar navegação para detalhes da promoção
    }

    @Override
    public void onCategoryClick(Category category) {
        // Filtrar produtos pela categoria selecionada
    }

    @Override
    public void onProductClick(Product product) {
        // Implementar navegação para detalhes do produto
    }
}