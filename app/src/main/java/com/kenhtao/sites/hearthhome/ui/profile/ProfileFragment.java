package com.kenhtao.sites.hearthhome.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.slider.Slider;
import com.kenhtao.sites.hearthhome.R;
public class ProfileFragment extends Fragment {

    private ShapeableImageView imgAvatar;
    private TextView tvName, tvStats;
    private ImageView iconEqualizer;
    private MaterialCardView cardEqualizer, cardVolume, cardMusicMode;
    private Slider sliderKitchenVolume, sliderLivingRoomVolume;
    private ChipGroup chipGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Ánh xạ view
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvStats = view.findViewById(R.id.tvStats);

        iconEqualizer = view.findViewById(R.id.iconEqualizer);
        cardEqualizer = (MaterialCardView) iconEqualizer.getParent().getParent(); // lấy card cha

        sliderKitchenVolume = view.findViewById(R.id.sliderKitchenVolume);
        sliderLivingRoomVolume = view.findViewById(R.id.sliderLivingRoomVolume);

        // cardVolume chính là cha chứa slider
        cardVolume = (MaterialCardView) sliderKitchenVolume.getParent().getParent();

        chipGroup = view.findViewById(R.id.chipGroup); // nhớ thêm id trong XML
        // cardMusicMode chính là cha của chipGroup
        cardMusicMode = (MaterialCardView) chipGroup.getParent().getParent();

        // Xử lý logic
        setupEvents();

        return view;
    }

    private void setupEvents() {
        // Ví dụ: click vào Equalizer
        cardEqualizer.setOnClickListener(v -> {
            // TODO: mở Equalizer Activity
        });

        // Slider thay đổi giá trị
        sliderKitchenVolume.addOnChangeListener((slider, value, fromUser) -> {
            // TODO: xử lý volume phòng bếp
        });

        sliderLivingRoomVolume.addOnChangeListener((slider, value, fromUser) -> {
            // TODO: xử lý volume phòng khách
        });

        // Chip chọn chế độ âm nhạc
        if (chipGroup != null) {
            chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
                if (!checkedIds.isEmpty()) {
                    int id = checkedIds.get(0);
                    String selected = group.findViewById(id).toString();
                    // TODO: xử lý chế độ nhạc đã chọn
                }
            });
        }
    }
}