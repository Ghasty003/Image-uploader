package com.example.imageuploader;

import android.net.Uri;

public class Uploads {
   String name;
   String imageUrl;

    public Uploads() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
