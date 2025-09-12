package com.kenhtao.sites.hearthhome.ui.custom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kenhtao.sites.hearthhome.R;
import com.kenhtao.sites.hearthhome.adapter.CustomSoundAdapter;
import com.kenhtao.sites.hearthhome.models.CustomSound;
import com.kenhtao.sites.hearthhome.models.SoundItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomSoundPickerActivity extends AppCompatActivity implements CustomSoundAdapter.OnSoundClickListener {

    private RecyclerView recyclerView;
    private CustomSoundAdapter adapter;
    private ImageView btnClose;
    private TextView tvTitle;
    private final List<Object> allItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sound_picker);

        recyclerView = findViewById(R.id.recyclerViewCustomSounds);
        btnClose = findViewById(R.id.btnClose);
        tvTitle = findViewById(R.id.tvTitle);

        setupRecyclerView();
        setupListeners();
        loadLocalSounds(); // 🔥 thay vì fetch API
    }

    private void setupListeners() {
        btnClose.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new CustomSoundAdapter(this, allItems, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == CustomSoundAdapter.VIEW_TYPE_HEADER ? 3 : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadLocalSounds() {
        allItems.clear();

        Map<String, List<SoundItem>> grouped = new LinkedHashMap<>();

        List<SoundItem> kitchen = new ArrayList<>();
        kitchen.add(new SoundItem(
                1,
                "Tiếng xoong nồi",
                R.raw.pot,
                R.drawable.pot,
                "Nhà bếp"
        ));
        kitchen.add(new SoundItem(
                2,
                "Tiếng chảo rán",
                R.raw.frying_pan,
                R.drawable.frying_pan,
                "Nhà bếp"
        ));
        kitchen.add(new SoundItem(
                3,
                "Tiếng lò vi sóng",
                R.raw.microwave,
                R.drawable.microwave,
                "Nhà bếp"
        ));
        kitchen.add(new SoundItem(
                4,
                "Tiếng máy xay sinh tố",
                R.raw.blender,
                R.drawable.blender,
                "Nhà bếp"
        ));
        kitchen.add(new SoundItem(
                5,
                "Tiếng máy rửa bát",
                R.raw.dishwasher,
                R.drawable.dishwasher,
                "Nhà bếp"
        ));
        grouped.put("Nhà bếp", kitchen);

        List<SoundItem> dining = new ArrayList<>();
        dining.add(new SoundItem(
                1,
                "Tiếng ăn uống",
                R.raw.chair,
                R.drawable.chair,
                "Phòng ăn"
        ));
        dining.add(new SoundItem(
                2,
                "Tiếng ly chén",
                R.raw.glass,
                R.drawable.glass,
                "Phòng ăn"
        ));
        dining.add(new SoundItem(
                3,
                "Tiếng trò chuyện",
                R.raw.chatting,
                R.drawable.chatting,
                "Phòng ăn"
        ));
        dining.add(new SoundItem(
                4,
                "Tiếng cười",
                R.raw.dishes,
                R.drawable.dishes,
                "Phòng ăn"
        ));
        grouped.put("Phòng ăn", dining);
        List<SoundItem> livingRoom = new ArrayList<>();
        livingRoom.add(new SoundItem(
                1,
                "Tiếng TV",
                R.raw.tv,
                R.drawable.tv,
                "Phòng khách"
        ));
        livingRoom.add(new SoundItem(
                2,
                "Tiếng lò sưởi",
                R.raw.fireplace,
                R.drawable.fireplace,
                "Phòng khách"
        ));
        grouped.put("Phòng khách", livingRoom);

        List<SoundItem> bedroom = new ArrayList<>();
        bedroom.add(new SoundItem(
                1,
                "Tiếng quạt trần",
                R.raw.ceiling_fan,
                R.drawable.ceiling_fan,
                "Phòng ngủ"
        ));
        bedroom.add(new SoundItem(
                2,
                "Tiếng điều hòa",
                R.raw.ac,
                R.drawable.ac,
                "Phòng ngủ"
        ));
        bedroom.add(new SoundItem(
                3,
                "Tiếng quạt cây",
                R.raw.fan,
                R.drawable.fan,
                "Phòng ngủ"
        ));
        bedroom.add(new SoundItem(
                4,
                "Tiếng máy lọc không khí",
                R.raw.air_purifier,
                R.drawable.air_purifier,
                "Phòng ngủ"
        ));
        grouped.put("Phòng ngủ", bedroom);

        List<SoundItem> bathroom = new ArrayList<>();
        bathroom.add(new SoundItem(
                1,
                "Tiếng vòi nước",
                R.raw.faucet,
                R.drawable.faucet,
                "Phòng tắm"
        ));
        bathroom.add(new SoundItem(
                2,
                "Tiếng bồn cầu",
                R.raw.toilet,
                R.drawable.toilet,
                "Phòng tắm"
        ));
        bathroom.add(new SoundItem(
                3,
                "Tiếng vòi sen",
                R.raw.shower,
                R.drawable.shower,
                "Phòng tắm"
        ));
        bathroom.add(new SoundItem(
                4,
                "Tiếng máy sấy tóc",
                R.raw.hair_dryer,
                R.drawable.hair_dryer,
                "Phòng tắm"
        ));
        bathroom.add(new SoundItem(
                5,
                "Tiếng máy giặt",
                R.raw.washing_machine,
                R.drawable.washing_machine,
                "Phòng tắm"
        ));
        grouped.put("Phòng tắm", bathroom);

        // Add header + sound
        for (Map.Entry<String, List<SoundItem>> entry : grouped.entrySet()) {
            allItems.add(entry.getKey());        // header
            allItems.addAll(entry.getValue());   // sounds
        }

        runOnUiThread(() -> adapter.notifyDataSetChanged());
    }

    private void returnResult(SoundItem item) {
        Intent intent = new Intent();
        intent.putExtra("soundId", item.getId());
        intent.putExtra("name", item.getName());
        intent.putExtra("soundResId", item.getSoundResId()); // 🔥 gửi resId thay vì fileUrl
        intent.putExtra("iconResId", item.getIconResId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSoundClick(SoundItem item) {
        returnResult(item);
    }
}