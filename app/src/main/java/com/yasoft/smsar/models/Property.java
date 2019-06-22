package com.yasoft.smsar.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Property {

    private int mID;
    private String mImageUrl;
    private String mCity;
    private String mDesc;
    private String mPrice;
    private String mUsername;
    private String area;
    private String type;
    private String category;
    private double longitude;
    private double latitude;
    private int noRooms;
    private int noBathrooms;
    private List<String> likedList;
    private int viewNumber;
    private String address;
    private String date;
    private boolean parking;
    private List<Property> mList = new ArrayList<>();

    public Property() {

    }

    public Property(int mID, String mUsername, String mCity, String mDesc, String mPrice,
                    int noRooms, int noBathrooms, String address, String date, String area, boolean parking,String type,String category,double longitude,double latitude, List<String> likedList) {
        this.mID = mID;
        this.mCity = mCity;
        this.area = area;
        this.mDesc = mDesc;
        this.mPrice = mPrice;
        this.mUsername = mUsername;
        this.noRooms = noRooms;
        this.noBathrooms = noBathrooms;
        this.address = address;
        this.date = date;
        this.parking = parking;

        this.type=type;
        this.category=category;
        this.longitude=longitude;
        this.latitude=latitude;

    }

    public Property(int mID, String mImageUrl, String mCity, String mDesc, String mPrice, String mUsername, 
                    String area, int noRooms, int noBathrooms,
                     String address, String date, boolean parking,String type,String category,double longitude,double latitude,List<String> likedList) {
        this.mID = mID;
        this.mImageUrl = mImageUrl;
        this.mCity = mCity;
        this.mDesc = mDesc;
        this.mPrice = mPrice;
        this.mUsername = mUsername;
        this.area = area;
        this.type = type;
        this.category = category;
        this.noRooms = noRooms;
        this.noBathrooms = noBathrooms;
        this.address = address;
        this.date = date;
        this.likedList=likedList;
        this.parking = parking;
        this.longitude=longitude;
        this.latitude=latitude;
        this.likedList=likedList;

    }
    public Property(int mID, String mImageUrl, String mCity, String mDesc, String mPrice, String mUsername,
                    String area, String type, String category, int noRooms, int noBathrooms,
                    List<String> likedList ,String address, String date, boolean parking) {
        this.mID = mID;
        this.mImageUrl = mImageUrl;
        this.mCity = mCity;
        this.mDesc = mDesc;
        this.mPrice = mPrice;
        this.mUsername = mUsername;
        this.area = area;
        this.type = type;
        this.category = category;
        this.noRooms = noRooms;
        this.noBathrooms = noBathrooms;
        this.likedList = likedList;
        this.address = address;
        this.date = date;
        this.parking = parking;
    }
    public Property(int mID, String mUsername, String mCity, String mDesc, String mPrice,
                    int noRooms, int noBathrooms, String address, String date, String area,
                    boolean parking, String mImageUrl,String type,String category,double longitude,double latitude,List<String> likedList) {
        this.mID = mID;
        this.mCity = mCity;
        this.area = area;
        this.mDesc = mDesc;
        this.mPrice = mPrice;
        this.mUsername = mUsername;
        this.noRooms = noRooms;
        this.noBathrooms = noBathrooms;
        this.address = address;
        this.date = date;
        this.parking = parking;
        this.mImageUrl = mImageUrl;
        this.type=type;
        this.category=category;
        this.longitude=longitude;
        this.latitude=latitude;
        this.likedList=likedList;

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

    public boolean getParking() {

        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public List<String> getlikedList() {
        return likedList;
    }

    public void setlikedList(List<String> likedList) {
        this.likedList = likedList;
    }


    public int getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
        this.viewNumber = viewNumber;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}



