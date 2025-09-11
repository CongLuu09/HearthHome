package com.kenhtao.sites.hearthhome.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenhtao.sites.hearthhome.R;
import com.kenhtao.sites.hearthhome.adapter.CategoryAdapter;
import com.kenhtao.sites.hearthhome.models.Category;
import com.kenhtao.sites.hearthhome.ui.player.CategoryPlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private CategoryAdapter categoryAdapter;
    private final List<Category> categoryList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_sounds);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Adapter có callback click
        categoryAdapter = new CategoryAdapter(getContext(), categoryList, category -> {
            Intent intent = new Intent(getContext(), CategoryPlayerActivity.class);
            intent.putExtra("CATEGORY_ID", Long.valueOf(category.getId()));
            intent.putExtra("CATEGORY_TITLE", category.getTitle());
            intent.putExtra("avatar", category.getAvatar());

            Log.d("HomeFragment", "Title: " + category.getTitle() + " | Avatar: " + category.getAvatar());
            startActivity(intent);
        });

        recyclerView.setAdapter(categoryAdapter);

        // Load local categories
        loadLocalCategories();

        return view;
    }

    private void loadLocalCategories() {
        categoryList.clear();
        categoryList.add(new Category("1", "Kitchen", String.valueOf(R.drawable.kitchen)));
        categoryList.add(new Category("2", "Dinging", String.valueOf(R.drawable.dining)));
        categoryList.add(new Category("3", "Living Room", String.valueOf(R.drawable.living_room)));
        categoryAdapter.notifyDataSetChanged();
    }
}