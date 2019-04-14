package com.yasoft.smsar.models;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class Property {

    private int mID;
    private Blob mImageDrawable;
    private String mCity;
    private String mDesc;
    private String mPrice;
    private String mUsername;
    private String area;


    private int noRooms;
    private int noBathrooms;
    private static int LIKED_NO;
    private String address;
    private String date;
    private boolean parking;

    private List<Property> mList=new ArrayList<>();

    public Property() {

    }

    public Property(int mID, String mCity, String mDesc, String mPrice) {
        this.mID = mID;
        this.mCity = mCity;
        this.mDesc = mDesc;
        this.mPrice = mPrice;

    }

    public Property(int mID, String mUsername, String mCity, String mDesc, String mPrice) {
        this.mID = mID;
        this.mCity = mCity;
        this.mDesc = mDesc;
        this.mPrice = mPrice;
        this.mUsername = mUsername;
    }

    public Property(int mID, String mUsername, String mCity, String mDesc, String mPrice,
                    int noRooms, int noBathrooms, String address, String date ,String area, boolean parking ) {
        this.mID = mID;
        this.mImageDrawable = mImageDrawable;
        this.mCity = mCity;
        this.area=area;
        this.mDesc = mDesc;
        this.mPrice = mPrice;
        this.mUsername = mUsername;
        this.noRooms = noRooms;
        this.noBathrooms = noBathrooms;
        this.address = address;
        this.date = date;
        this.parking = parking;
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

    public Blob getmImageDrawable() {
        return mImageDrawable;
    }

    public void setmImageDrawable(Blob mImageDrawable) {
        this.mImageDrawable = mImageDrawable;
    }

    public int getNoRooms() {
        return noRooms;
    }

    public void setNoRooms(int noRooms) {
        this.noRooms = noRooms;
    }

    public int getNoBathrooms() {
        return noBathrooms;
    }

    public void setNoBathrooms(int noBathrooms) {
        this.noBathrooms = noBathrooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public void setParking(boolean parking) {
        this.parking = parking;
    }
    public boolean getParking(){

        return parking;
    }
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public static int getLikedNo() {
        return LIKED_NO;
    }

    public static void setLikedNo(int likedNo) {
        LIKED_NO = likedNo;
    }


    public int getLIKED_NO() {
        return LIKED_NO;
    }


}



