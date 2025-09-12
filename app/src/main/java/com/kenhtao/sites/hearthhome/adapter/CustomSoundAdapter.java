package com.kenhtao.sites.hearthhome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.kenhtao.sites.hearthhome.R;
import com.kenhtao.sites.hearthhome.models.SoundItem;

import java.util.List;

public class CustomSoundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;

    public interface OnSoundClickListener {
        void onSoundClick(SoundItem item);
    }

    private final Context context;
    private final List<Object> items;
    private final OnSoundClickListener listener;

    public CustomSoundAdapter(Context context, List<Object> items, OnSoundClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_custom_group, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_sound_custom, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            String groupTitle = (String) items.get(position);
            ((HeaderViewHolder) holder).tvGroupTitle.setText(groupTitle);

        } else if (holder instanceof ItemViewHolder) {
            SoundItem item = (SoundItem) items.get(position);
            ItemViewHolder vh = (ItemViewHolder) holder;

            vh.tvLabel.setText(item.getName());

            // ✅ Load icon local thay vì Glide/URL
            if (item.getIconResId() != -1) {
                vh.imgIcon.setImageResource(item.getIconResId());
            } else {
                vh.imgIcon.setImageResource(R.drawable.sound_airplane); // fallback
            }

            vh.imgLock.setVisibility(item.isLocked() ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSoundClick(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupTitle;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupTitle = itemView.findViewById(R.id.tv_group_title);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgIcon;
        TextView tvLabel;
        ImageView imgLock;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            imgLock = itemView.findViewById(R.id.imgLock);
        }
    }

}
