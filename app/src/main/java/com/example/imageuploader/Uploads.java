package com.example.imageuploader;

import android.net.Uri;

public class Uploads {
   String name;
   Uri imageUrl;

    public Uploads() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }
}
