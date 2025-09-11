package com.kenhtao.sites.hearthhome.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kenhtao.sites.hearthhome.R;
import com.kenhtao.sites.hearthhome.models.Category;
import com.kenhtao.sites.hearthhome.ui.player.CategoryPlayerActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Context context;
    private final List<Category> categories;
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvSoundTitle.setText(category.getTitle());

        String avatar = category.getAvatar();
        Log.d("CategoryAdapter", "Avatar raw: " + avatar);

        if (avatar != null && avatar.matches("\\d+")) {
            // Local drawable
            int resId = Integer.parseInt(avatar);
            holder.imgSound.setImageResource(resId);
            Log.d("CategoryAdapter", "Loaded local drawable: " + resId);
        } else if (avatar != null && avatar.startsWith("http")) {
            // Full URL
            Glide.with(context)
                    .load(avatar)
                    .placeholder(R.drawable.sound_airplane)
                    .error(R.drawable.birds_singing)
                    .into(holder.imgSound);
        } else if (avatar != null) {
            // Backend trả về storage path
            String imageUrl = buildFullImageUrl(avatar);
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.sound_airplane)
                    .error(R.drawable.birds_singing)
                    .into(holder.imgSound);
            Log.d("CategoryAdapter", "Loaded from backend: " + imageUrl);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
            } else {
                Intent intent = new Intent(context, CategoryPlayerActivity.class);
                intent.putExtra("categoryId", category.getId());
                intent.putExtra("categoryTitle", category.getTitle());
                intent.putExtra("avatar", category.getAvatar());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSound;
        TextView tvSoundTitle;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSound = itemView.findViewById(R.id.imgSound);
            tvSoundTitle = itemView.findViewById(R.id.tvSoundTitle);
        }
    }

    private String buildFullImageUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.isEmpty()) return null;

        rawUrl = rawUrl.replaceFirst("^/+", "");
        if (!rawUrl.startsWith("storage/")) {
            rawUrl = "storage/" + rawUrl;
        }

        return "https://sleepchills.kenhtao.site/" + rawUrl;
    }
}
