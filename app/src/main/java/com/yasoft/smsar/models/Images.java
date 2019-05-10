package com.yasoft.smsar.models;

import android.graphics.Bitmap;
import android.net.Uri;

public class Images {

    private Bitmap image;
    private String mName;
    private String mImageUrl;

    public  Images(Bitmap image){

        this.image=image;
    }

    public Images(String mName, String mImageUrl) {

        if (mName.trim().equals(""))
            mName = "No Name";

        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public Images(Bitmap image, String mName, String mImageUrl) {
        this.image = image;
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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
