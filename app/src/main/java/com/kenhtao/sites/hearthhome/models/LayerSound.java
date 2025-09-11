package com.kenhtao.sites.hearthhome.models;

import android.media.MediaPlayer;

public class LayerSound {
    private long id;
    private String name;

    private int soundResId;  // file âm thanh trong res/raw
    private int iconResId;   // icon trong res/drawable

    private float volume = 0.5f;
    private transient MediaPlayer mediaPlayer;

    // --- Constructor ---
    public LayerSound(long id, String name, int soundResId, int iconResId, float volume) {
        this.id = id;
        this.name = name;
        this.soundResId = soundResId;
        this.iconResId = iconResId;
        this.volume = volume;
    }

    public LayerSound(String name, int soundResId, int iconResId) {
        this(-1, name, soundResId, iconResId, 0.5f);
    }

    // --- Getter & Setter ---
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSoundResId() { return soundResId; }
    public void setSoundResId(int soundResId) { this.soundResId = soundResId; }

    public int getIconResId() { return iconResId; }
    public void setIconResId(int iconResId) { this.iconResId = iconResId; }

    public float getVolume() { return volume; }
    public void setVolume(float volume) { this.volume = volume; }

    public MediaPlayer getMediaPlayer() { return mediaPlayer; }
    public void setMediaPlayer(MediaPlayer mediaPlayer) { this.mediaPlayer = mediaPlayer; }

    public boolean hasLocalSound() { return soundResId != 0; }

    public void release() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception ignored) {}
            mediaPlayer = null;
        }
    }

    @Override
    public String toString() {
        return "LayerSound{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", soundResId=" + soundResId +
                ", iconResId=" + iconResId +
                ", volume=" + volume +
                '}';
    }
}

