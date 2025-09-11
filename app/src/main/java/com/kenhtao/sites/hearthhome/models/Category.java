package com.kenhtao.sites.hearthhome.models;

public class Category {

    private String id;
    private String title;
    private String avatar;

    private String slug;
    private int status;



    // Constructor rút gọn cho Local
    public Category(String id, String title, String avatar) {
        this.id = id;
        this.title = title;
        this.avatar = avatar;
    }

    // Getter & Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

