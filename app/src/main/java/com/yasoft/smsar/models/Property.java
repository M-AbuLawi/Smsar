package com.yasoft.smsar.models;

import java.sql.Blob;
import java.util.ArrayList;

public class Property {

        private int mID;
        private Blob mImageDrawable;
        private String mCity;
        private String mDesc;
        private String mPrice;
        private String mUsername;


    public Property(int mID, String mCity, String mDesc, String mPrice) {
        this.mID=mID;
        this.mCity = mCity;
        this.mDesc = mDesc;
        this.mPrice = mPrice;

    }
    public Property(int mID,String mUsername, String mCity, String mDesc, String mPrice) {
        this.mID=mID;
        this.mCity = mCity;
        this.mDesc = mDesc;
        this.mPrice = mPrice;
        this.mUsername=mUsername;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }
}



