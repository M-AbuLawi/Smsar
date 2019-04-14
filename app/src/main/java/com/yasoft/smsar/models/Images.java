package com.yasoft.smsar.models;

import android.graphics.Bitmap;
import android.net.Uri;

public class Images {

   private Bitmap image;

    public  Images(Bitmap image){

        this.image=image;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
