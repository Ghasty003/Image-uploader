package com.example.imageuploader;

public class Uploads {
    String mName;
    String mImageUrl;

    public Uploads() {

    }

    public Uploads(String mName, String mImageUrl) {

        if (mName.trim().equals("")) {
            mName = "No name";
        }

        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
