package com.example.aplikacja.models;

import android.net.Uri;

public class Flower {
    String name;
    String image;

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Flower(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
