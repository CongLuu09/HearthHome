package com.kenhtao.sites.hearthhome.models;

public class SoundItem {
    private long id;
    private String name;
    private int soundResId;
    private int iconResId;
    private String category;
    private boolean locked;

    public SoundItem() {}

    public SoundItem(long id, String name, int soundResId, int iconResId, String category) {
        this.id = id;
        this.name = name;
        this.soundResId = soundResId;
        this.iconResId = iconResId;
        this.category = category;
        this.locked = false;
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

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
}