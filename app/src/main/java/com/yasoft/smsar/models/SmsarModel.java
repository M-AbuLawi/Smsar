package com.yasoft.smsar.models;

import java.util.ArrayList;

public class SmsarModel {

    String mFullname;
    String mPhoneNumber;
    String mUsername;
    public SmsarModel(String mUsername,String mFullname,String mPhoneNumber){

        this.mUsername=mUsername;
        this.mFullname=mFullname;
        this.mPhoneNumber=mPhoneNumber;


    }

    public SmsarModel() {

    }
    public SmsarModel(ArrayList<SmsarModel> list) {
        this.mUsername=list.get(0).toString();
        this.mFullname=list.get(1).toString();
        this.mPhoneNumber=list.get(2).toString();

    }

    public String getmFullname() {
        return mFullname;
    }

    public void setmFullname(String mFullname) {
        this.mFullname = mFullname;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }
}
