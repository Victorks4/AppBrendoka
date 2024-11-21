package com.example.appproject05.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
        BakeryAdapter.OnBakeryClickListener {

    private ViewPager2 bannerViewPager;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView nearbyBakeriesRecyclerView;
    private List<BannerItem> banners = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializeViews(view);
        setupRecyclerViews();
        loadMockData();

        return view;
    }

    private void initializeViews(View view) {
        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        nearbyBakeriesRecyclerView = view.findViewById(R.id.nearbyBakeriesRecyclerView);
    }

    private void setupRecyclerViews() {
        bannerViewPager.setOffscreenPageLimit(1);
        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        nearbyBakeriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
    }

    private void loadMockData() {
        loadMockBanners();
        loadMockCategories();
        loadMockBakeries();
    }

    private void loadMockBanners() {
        banners.clear();
        banners.add(new BannerItem("1", R.drawable.banner1, "Promoção 1", "Descrição 1"));
        banners.add(new BannerItem("2", R.drawable.banner2, "Promoção 2", "Descrição 2"));

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

    private void loadMockBakeries() {
        List<Bakery> bakeries = new ArrayList<>();
        bakeries.add(new Bakery("1", "Padaria do João", "", 4.5f, "30-45 min", "R$ 5,00", true));
        bakeries.add(new Bakery("2", "Padaria Maria", "", 4.8f, "25-35 min", "R$ 6,00", true));
        BakeryAdapter bakeryAdapter = new BakeryAdapter(bakeries, this);
        nearbyBakeriesRecyclerView.setAdapter(bakeryAdapter);
    }

    @Override
    public void onBannerClick(int position) {
        BannerItem banner = banners.get(position);
        // Implementar navegação para detalhes da promoção
    }

    @Override
    public void onCategoryClick(Category category) {
        // Implementar navegação para lista de produtos da categoria
    }

    @Override
    public void onBakeryClick(Bakery bakery) {
        // Implementar navegação para detalhes da padaria
    }
}