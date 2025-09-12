package com.kenhtao.sites.hearthhome.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kenhtao.sites.hearthhome.R;
import com.kenhtao.sites.hearthhome.models.LayerSound;
import com.kenhtao.sites.hearthhome.utils.MixLocalManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayLayerAdapter extends RecyclerView.Adapter<PlayLayerAdapter.ViewHolder> {
    private final List<LayerSound> layers;
    private final Context context;
    private final long categoryId; // thêm biến categoryId
    private final Map<Long, MediaPlayer> playerMap = new HashMap<>();

    public PlayLayerAdapter(Context context, List<LayerSound> layers, long categoryId) {
        this.context = context;
        this.layers = layers;
        this.categoryId = categoryId; // gán categoryId
    }

    // --------------------- MediaPlayer control ---------------------
    public void playAllPlayers() {
        for (LayerSound sound : layers) {
            long id = sound.getId();
            MediaPlayer player = playerMap.get(id);
            if (player == null && sound.getSoundResId() != 0) {
                player = MediaPlayer.create(context, sound.getSoundResId());
                if (player != null) {
                    player.setLooping(true);
                    player.setVolume(sound.getVolume(), sound.getVolume());
                    player.start();
                    playerMap.put(id, player);
                    sound.setMediaPlayer(player);
                }
            } else if (player != null && !player.isPlaying()) {
                try {
                    player.start();
                } catch (IllegalStateException e) {
                    Log.e("PlayLayerAdapter", "❌ start failed", e);
                }
            }
        }
    }

    public void pauseAllPlayers() {
        for (LayerSound sound : layers) {
            MediaPlayer player = sound.getMediaPlayer();
            if (player != null && player.isPlaying()) {
                try {
                    player.pause();
                } catch (IllegalStateException e) {
                    Log.e("PlayLayerAdapter", "❌ pause failed", e);
                }
            }
        }
    }

    // --------------------- Listener ---------------------
    public interface OnSoundDeletedListener {
        void onSoundDeleted();
    }

    private OnSoundDeletedListener onSoundDeletedListener;

    public void setOnSoundDeletedListener(OnSoundDeletedListener listener) {
        this.onSoundDeletedListener = listener;
    }

    // --------------------- RecyclerView ---------------------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layer_sound, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LayerSound layer = layers.get(position);

        holder.tvLayerName.setText(layer.getName());

        holder.imgLayerIcon.setImageResource(
                layer.getIconResId() != 0 ? layer.getIconResId() : R.drawable.birds_singing
        );

        holder.seekBarVolume.setProgress((int) (layer.getVolume() * 100));
        holder.seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                layer.setVolume(volume);
                MediaPlayer current = layer.getMediaPlayer();
                if (current != null) current.setVolume(volume, volume);

                // 🔥 Lưu luôn volume vào MixLocalManager
                saveAllLayersToLocal();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // khởi tạo MediaPlayer nếu chưa có
        MediaPlayer player = layer.getMediaPlayer();
        if (player == null && layer.getSoundResId() != 0) {
            try {
                String resType = context.getResources().getResourceTypeName(layer.getSoundResId());
                if ("raw".equals(resType)) {
                    player = MediaPlayer.create(context, layer.getSoundResId());
                    if (player != null) {
                        player.setLooping(true);
                        player.setVolume(layer.getVolume(), layer.getVolume());
                        player.start();
                        playerMap.put(layer.getId(), player);
                        layer.setMediaPlayer(player);
                    }
                } else {
                    Log.e("PlayLayerAdapter", "❌ soundResId không thuộc res/raw mà là: " + resType);
                }
            } catch (Exception e) {
                Log.e("PlayLayerAdapter", "❌ Lỗi tạo MediaPlayer: " + e.getMessage(), e);
            }
        }


        // --------------------- Edit Layer ---------------------
        holder.btnEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Đổi tên âm thanh");

            final EditText input = new EditText(context);
            input.setText(layer.getName());
            builder.setView(input);

            builder.setPositiveButton("Lưu", (dialog, which) -> {
                String newName = input.getText().toString().trim();
                if (!newName.isEmpty()) {
                    layer.setName(newName);
                    notifyItemChanged(holder.getAdapterPosition());

                    // 🔥 Cập nhật trực tiếp MixLocalManager
                    List<LayerSound> savedLayers = MixLocalManager.loadMixFull(context, categoryId);
                    for (LayerSound l : savedLayers) {
                        if (l.getId() == layer.getId()) {
                            l.setName(newName);
                            break;
                        }
                    }
                    MixLocalManager.saveMixFull(context, categoryId, savedLayers);

                    Toast.makeText(context, "✅ Đổi tên thành công", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Huỷ", null);
            builder.show();
        });

        // --------------------- Delete Layer ---------------------
        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            new AlertDialog.Builder(context)
                    .setTitle("Xoá âm thanh")
                    .setMessage("Bạn có chắc muốn xoá \"" + layer.getName() + "\" không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        releaseLayer(layer);
                        layers.remove(pos);
                        notifyItemRemoved(pos);

                        // 🔥 Cập nhật MixLocalManager
                        List<LayerSound> savedLayers = MixLocalManager.loadMixFull(context, categoryId);
                        savedLayers.removeIf(l -> l.getId() == layer.getId());
                        MixLocalManager.saveMixFull(context, categoryId, savedLayers);

                        if (onSoundDeletedListener != null) {
                            onSoundDeletedListener.onSoundDeleted();
                        }

                        Toast.makeText(context, "🗑️ Đã xoá âm thanh", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return layers.size();
    }

    // --------------------- ViewHolder ---------------------
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLayerIcon, btnEdit, btnDelete;
        TextView tvLayerName;
        SeekBar seekBarVolume;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLayerIcon = itemView.findViewById(R.id.imgLayerIcon);
            tvLayerName = itemView.findViewById(R.id.tvLayerName);
            seekBarVolume = itemView.findViewById(R.id.seekBarVolume);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    // --------------------- Helper ---------------------
    private void releaseLayer(LayerSound layer) {
        MediaPlayer player = layer.getMediaPlayer();
        if (player != null) {
            try { if (player.isPlaying()) player.stop(); } catch (Exception ignored) {}
            try { player.release(); } catch (Exception ignored) {}
            playerMap.remove(layer.getId());
            layer.setMediaPlayer(null);
        }
    }

    public void releaseAllPlayers() {
        for (LayerSound layer : layers) {
            releaseLayer(layer);
        }
    }

    // --------------------- Load & Add Layer ---------------------
    public void loadLayersFromDatabase(Context context, long categoryId) {
        List<LayerSound> savedLayers = MixLocalManager.loadMixFull(context, categoryId);
        layers.clear();
        layers.addAll(savedLayers);
        notifyDataSetChanged();
    }

    public void addLayer(LayerSound layer) {
        // kiểm tra trùng tên
        for (LayerSound l : layers) {
            if (l.getName().trim().equalsIgnoreCase(layer.getName().trim())) {
                Toast.makeText(context, "Đã tồn tại âm thanh \"" + layer.getName() + "\"", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        layers.add(layer);
        notifyItemInserted(layers.size() - 1);

        // khởi tạo MediaPlayer
        if (layer.getSoundResId() != 0) {
            MediaPlayer player = MediaPlayer.create(context, layer.getSoundResId());
            if (player != null) {
                player.setLooping(true);
                player.setVolume(layer.getVolume(), layer.getVolume());
                player.start();
                layer.setMediaPlayer(player);
                playerMap.put(layer.getId(), player);
            }
        }

        // 🔥 Lưu luôn vào MixLocalManager
        saveAllLayersToLocal();
    }

    private void saveAllLayersToLocal() {
        MixLocalManager.saveMixFull(context, categoryId, new ArrayList<>(layers));
    }
}

