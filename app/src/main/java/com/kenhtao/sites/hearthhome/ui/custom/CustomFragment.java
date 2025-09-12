package com.kenhtao.sites.hearthhome.ui.custom;

import android.media.MediaPlayer;
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
import com.kenhtao.sites.hearthhome.adapter.CustomAdapter;
import com.kenhtao.sites.hearthhome.models.CustomSound;
import com.kenhtao.sites.hearthhome.models.CustomSoundGroup;
import com.kenhtao.sites.hearthhome.models.SoundItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomFragment extends Fragment {

    private static final String TAG = "CustomFragment";

    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;

    private final Map<Long, MediaPlayer> playingSounds = new HashMap<>();
    private final Set<Long> activeSoundIds = new HashSet<>();
    private final Map<Long, Float> volumeLevels = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCustom);
        setupRecyclerView();
        fetchAllSoundsLocal();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        for (MediaPlayer player : playingSounds.values()) {
            if (player != null && player.isPlaying()) player.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (MediaPlayer player : playingSounds.values()) {
            try {
                if (player.isPlaying()) player.stop();
            } catch (IllegalStateException ignored) {}
            player.release();
        }
        playingSounds.clear();
        activeSoundIds.clear();
        volumeLevels.clear();
    }

    private void setupRecyclerView() {
        customAdapter = new CustomAdapter(new ArrayList<>(), getContext(), new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CustomSound sound) {
                handleSoundClick(sound);
            }

            @Override
            public void onVolumeChange(CustomSound sound, float volume) {
                updateSoundVolume(sound, volume);
            }

            @Override
            public float getSoundVolume(CustomSound sound) {
                return volumeLevels.getOrDefault(sound.getId(), 1.0f);
            }

            @Override
            public boolean isSoundActive(CustomSound sound) {
                return activeSoundIds.contains(sound.getId());
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return customAdapter.getItemViewType(position) == CustomAdapter.TYPE_GROUP ? 3 : 1;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(customAdapter);
    }

    // ==================== Load Local Data ====================
    private void fetchAllSoundsLocal() {
        Map<String, List<CustomSound>> grouped = new LinkedHashMap<>();

        List<CustomSound> kitchen = new ArrayList<>();
        kitchen.add(new CustomSound(
                1,
                "Tiếng xoong nồi",
                R.raw.pot,
                R.drawable.pot,
                "Nhà bếp"
        ));
        kitchen.add(new CustomSound(
                2,
                "Tiếng chảo rán",
                R.raw.frying_pan,
                R.drawable.frying_pan,
                "Nhà bếp"
        ));
        kitchen.add(new CustomSound(
                3,
                "Tiếng lò vi sóng",
                R.raw.microwave,
                R.drawable.microwave,
                "Nhà bếp"
        ));
        kitchen.add(new CustomSound(
                4,
                "Tiếng máy xay sinh tố",
                R.raw.blender,
                R.drawable.blender,
                "Nhà bếp"
        ));
        kitchen.add(new CustomSound(
                5,
                "Tiếng máy rửa bát",
                R.raw.dishwasher,
                R.drawable.dishwasher,
                "Nhà bếp"
        ));
        grouped.put("Nhà bếp", kitchen);

        List<CustomSound> dining = new ArrayList<>();
        dining.add(new CustomSound(
                1,
                "Tiếng ăn uống",
                R.raw.chair,
                R.drawable.chair,
                "Phòng ăn"
        ));
        dining.add(new CustomSound(
                2,
                "Tiếng ly chén",
                R.raw.glass,
                R.drawable.glass,
                "Phòng ăn"
        ));
        dining.add(new CustomSound(
                3,
                "Tiếng trò chuyện",
                R.raw.chatting,
                R.drawable.chatting,
                "Phòng ăn"
        ));
        dining.add(new CustomSound(
                4,
                "Tiếng cười",
                R.raw.dishes,
                R.drawable.dishes,
                "Phòng ăn"
        ));
        grouped.put("Phòng ăn", dining);
        List<CustomSound> livingRoom = new ArrayList<>();
        livingRoom.add(new CustomSound(
                1,
                "Tiếng TV",
                R.raw.tv,
                R.drawable.tv,
                "Phòng khách"
        ));
        livingRoom.add(new CustomSound(
                2,
                "Tiếng lò sưởi",
                R.raw.fireplace,
                R.drawable.fireplace,
                "Phòng khách"
        ));
        grouped.put("Phòng khách", livingRoom);
        List<CustomSound> bedroom = new ArrayList<>();
        bedroom.add(new CustomSound(
                1,
                "Tiếng quạt trần",
                R.raw.ceiling_fan,
                R.drawable.ceiling_fan,
                "Phòng ngủ"
        ));
        bedroom.add(new CustomSound(
                2,
                "Tiếng điều hòa",
                R.raw.ac,
                R.drawable.ac,
                "Phòng ngủ"
        ));
        bedroom.add(new CustomSound(
                3,
                "Tiếng quạt cây",
                R.raw.fan,
                R.drawable.fan,
                "Phòng ngủ"
        ));
        bedroom.add(new CustomSound(
                4,
                "Tiếng máy lọc không khí",
                R.raw.air_purifier,
                R.drawable.air_purifier,
                "Phòng ngủ"
        ));
        grouped.put("Phòng ngủ", bedroom);

        List<CustomSound> bathroom = new ArrayList<>();
        bathroom.add(new CustomSound(
                1,
                "Tiếng vòi nước",
                R.raw.faucet,
                R.drawable.faucet,
                "Phòng tắm"
        ));
        bathroom.add(new CustomSound(
                2,
                "Tiếng bồn cầu",
                R.raw.toilet,
                R.drawable.toilet,
                "Phòng tắm"
        ));
        bathroom.add(new CustomSound(
                3,
                "Tiếng vòi sen",
                R.raw.shower,
                R.drawable.shower,
                "Phòng tắm"
        ));
        bathroom.add(new CustomSound(
                4,
                "Tiếng máy sấy tóc",
                R.raw.hair_dryer,
                R.drawable.hair_dryer,
                "Phòng tắm"
        ));
        bathroom.add(new CustomSound(
                5,
                "Tiếng máy giặt",
                R.raw.washing_machine,
                R.drawable.washing_machine,
                "Phòng tắm"
        ));
        grouped.put("Phòng tắm", bathroom);

        List<CustomSoundGroup> groups = new ArrayList<>();
        for (Map.Entry<String, List<CustomSound>> entry : grouped.entrySet()) {
            groups.add(new CustomSoundGroup(entry.getKey(), entry.getValue()));
        }

        requireActivity().runOnUiThread(() -> customAdapter.setData(groups));
    }


    // ==================== MediaPlayer Handling ====================
    private void handleSoundClick(CustomSound sound) {
        long id = sound.getId();
        int resId = sound.getSoundResId();
        if (resId == 0) return; // không có nhạc

        if (playingSounds.containsKey(id)) {
            MediaPlayer player = playingSounds.get(id);
            if (player != null) {
                try {
                    if (player.isPlaying()) player.stop();
                } catch (IllegalStateException ignored) {}
                player.release();
            }
            playingSounds.remove(id);
            activeSoundIds.remove(id);
        } else {
            try {
                MediaPlayer player = MediaPlayer.create(requireContext(), resId); // 🎵 local resource
                if (player != null) {
                    player.setLooping(true);
                    float volume = volumeLevels.getOrDefault(id, 1.0f);
                    player.setVolume(volume, volume);
                    player.start();
                    playingSounds.put(id, player);
                    activeSoundIds.add(id);
                }
            } catch (Exception e) {
                Log.e(TAG, "❌ Lỗi phát âm thanh local: " + resId, e);
            }
        }

        customAdapter.notifyDataSetChanged();
    }


    private void updateSoundVolume(CustomSound sound, float volume) {
        long id = sound.getId();
        volumeLevels.put(id, volume);
        MediaPlayer player = playingSounds.get(id);
        if (player != null) {
            player.setVolume(volume, volume);
        }
    }

    // ==================== Public Utilities ====================
    public void playSoundByName(String name) {
        // Optional: phát 1 âm thanh theo tên
    }
}