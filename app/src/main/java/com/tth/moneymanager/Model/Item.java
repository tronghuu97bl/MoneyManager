package com.tth.moneymanager.Model;

public class Item {
    private int id;
    private String name;
    private String image_url;
    private String description;

    public Item() {
    }

    public Item(int id, String name, String image_url, String description) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
