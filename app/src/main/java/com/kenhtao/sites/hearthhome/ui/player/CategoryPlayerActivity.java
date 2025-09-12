package com.kenhtao.sites.hearthhome.ui.player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kenhtao.sites.hearthhome.R;
import com.kenhtao.sites.hearthhome.adapter.PlayLayerAdapter;
import com.kenhtao.sites.hearthhome.models.LayerSound;
import com.kenhtao.sites.hearthhome.timer.TimerCallBack;
import com.kenhtao.sites.hearthhome.timer.TimerDialog;
import com.kenhtao.sites.hearthhome.timer.TimerViewModel;
import com.kenhtao.sites.hearthhome.ui.custom.CustomSoundPickerActivity;
import com.kenhtao.sites.hearthhome.utils.MixLocalManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryPlayerActivity extends AppCompatActivity {
    private ImageView btnBack, btnPlayPause, imgBackground, btnAddLayer;
    private TextView tvTitle, tvTimer;
    private RecyclerView recyclerViewLayers;

    private boolean isPlaying = false;
    private MediaPlayer mainPlayer;

    private PlayLayerAdapter layerAdapter;
    private final List<LayerSound> layers = new ArrayList<>();

    private TimerViewModel timerViewModel;
    private long categoryId;
    private String categoryTitle;
    private String categoryAvatar;

    private int mainSoundResId = -1; // resource id của sound chính

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_player);

        initViews();
        getIntentData();
        setupBackgroundImage();

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        setupTimerObserver();

        setupListeners();
        setupLayerSounds();

        mapCategoryToLocalSound();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        tvTitle = findViewById(R.id.tvTitle);
        tvTimer = findViewById(R.id.tvTimer);
        recyclerViewLayers = findViewById(R.id.recyclerViewLayers);
        imgBackground = findViewById(R.id.imgBackground);
        btnAddLayer = findViewById(R.id.btnAddLayer);
    }

    private void getIntentData() {
        categoryId = getIntent().getLongExtra("CATEGORY_ID", -1);
        categoryTitle = getIntent().getStringExtra("CATEGORY_TITLE");
        categoryAvatar = getIntent().getStringExtra("avatar");

        tvTitle.setText(categoryTitle != null ? categoryTitle : "Unknown");
    }

    private void setupBackgroundImage() {
        if (categoryAvatar != null && categoryAvatar.matches("\\d+")) {

            int resId = Integer.parseInt(categoryAvatar);
            imgBackground.setImageResource(resId);
        } else if (categoryAvatar != null && categoryAvatar.startsWith("http")) {

            Glide.with(this).load(categoryAvatar)
                    .placeholder(R.drawable.birds_singing)
                    .error(R.drawable.birds_singing)
                    .into(imgBackground);
        } else {

            imgBackground.setImageResource(R.drawable.birds_singing);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnPlayPause.setOnClickListener(v -> {
            if (isPlaying) pauseMainSound(); else playMainSound();
        });
        btnAddLayer.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomSoundPickerActivity.class);
            customSoundLauncher.launch(intent);
        });
        tvTimer.setOnClickListener(v -> {
            TimerDialog dialog = TimerDialog.newInstance();
            dialog.setCallback(new TimerCallBack() {
                public void onTimerSet(long durationMillis) { timerViewModel.startTimer(durationMillis); }
                public void onTimerCancelled() { timerViewModel.cancelTimer(); }
                public void onTimerFinished() {
                    pauseMainSound();
                    if (layerAdapter != null) layerAdapter.releaseAllPlayers();
                }
            });
            dialog.show(getSupportFragmentManager(), "TimerDialog");
        });
    }


    private void setupTimerObserver() {
        timerViewModel.getTimeLeftMillis().observe(this, timeLeft -> {
            if (timeLeft > 0) {
                long minutes = (timeLeft / 1000) / 60;
                long seconds = (timeLeft / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            } else {
                tvTimer.setText("Timer");
                pauseMainSound();
            }
        });
    }

    private void setupLayerSounds() {
        layerAdapter = new PlayLayerAdapter(this, layers, categoryId);
        recyclerViewLayers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLayers.setAdapter(layerAdapter);

        // Listener khi xóa layer
        layerAdapter.setOnSoundDeletedListener(() -> {
            saveMixToLocal(); // lưu mix vào SharedPreferences
            Toast.makeText(this, "✅ Đã lưu mix sau khi xoá", Toast.LENGTH_SHORT).show();
        });

        // ✅ Load dữ liệu tự động từ MixLocalManager / AppDatabase
        layerAdapter.loadLayersFromDatabase(this, categoryId);
    }




    private void mapCategoryToLocalSound() {
        switch ((int) categoryId) {
            case 1: mainSoundResId = R.raw.kitchen; break;
            case 2: mainSoundResId = R.raw.dining; break;
            case 3: mainSoundResId = R.raw.living_room; break;
            case 4: mainSoundResId = R.raw.bedroom; break;
            default:
                Toast.makeText(this, "❌ Không tìm thấy nhạc local", Toast.LENGTH_SHORT).show();
        }
    }

    private void playMainSound() {
        if (mainSoundResId == -1) {
            Toast.makeText(this, "❌ Không có nhạc để phát", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mainPlayer == null) {
            mainPlayer = MediaPlayer.create(this, mainSoundResId);
            mainPlayer.setLooping(true);
        }

        if (!mainPlayer.isPlaying()) {
            mainPlayer.start();
            isPlaying = true;
            btnPlayPause.setImageResource(R.drawable.stop);
        }
    }

    private void pauseMainSound() {
        if (mainPlayer != null) {
            if (mainPlayer.isPlaying()) {
                mainPlayer.pause();
            }
            mainPlayer.release();
            mainPlayer = null;
        }
        isPlaying = false;
        btnPlayPause.setImageResource(R.drawable.play);
    }

    private final ActivityResultLauncher<Intent> customSoundLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    long soundId = data.getLongExtra("soundId", -1);
                    String name = data.getStringExtra("name");
                    int soundResId = data.getIntExtra("soundResId", 0);
                    int iconResId = data.getIntExtra("iconResId", 0);

                    if (soundId != -1 && soundResId != 0) {

                        LayerSound newLayer = new LayerSound(
                                soundId,
                                name,
                                soundResId,
                                iconResId,
                                0.5f // volume mặc định
                        );

                        layers.add(newLayer);
                        layerAdapter.notifyItemInserted(layers.size() - 1);

                        saveMixToLocal(); // lưu lại mix
                    } else {
                        Toast.makeText(this, "❌ Âm thanh không hợp lệ.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    private void saveMixToLocal() {
        try {
            if (layers == null || layers.isEmpty()) {
                Log.w("SAVE_MIX", "⚠ Không có layer nào để lưu");
                return;
            }

            // Lưu mix vào local qua MixLocalManager
            MixLocalManager.saveMixFull(
                    this,
                    categoryId,
                    layers
            );

            Log.d("SAVE_MIX", "✅ Mix đã auto-save xuống local");
        } catch (Exception e) {
            Log.e("SAVE_MIX", "❌ Lỗi khi lưu mix local: " + e.getMessage(), e);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        pauseMainSound();
        if (layerAdapter != null) layerAdapter.releaseAllPlayers();
    }
}
