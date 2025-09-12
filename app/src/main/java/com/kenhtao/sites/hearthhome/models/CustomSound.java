package com.kenhtao.sites.hearthhome.models;

public class CustomSound {

    private long id;
    private String title;
    private int soundResId;
    private int imageResId;
    private String category;
    private boolean locked;

    public CustomSound(long id, String title, int soundResId, int imageResId, String category) {
        this.id = id;
        this.title = title;
        this.soundResId = soundResId;
        this.imageResId = imageResId;
        this.category = category;
        this.locked = false;
    }

    // ========== Getter & Setter ==========
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getSoundResId() { return soundResId; }
    public void setSoundResId(int soundResId) { this.soundResId = soundResId; }

    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
}