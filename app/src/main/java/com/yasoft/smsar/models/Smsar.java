package com.yasoft.smsar.models;

import java.util.ArrayList;

public class Smsar {

 private  String mFullname;
 private  String mPhoneNumber;
 private  String mUsername;
 private  String mEmail;
 private  String mPassword;


    public Smsar(String mFullname, String mPhoneNumber, String mUsername, String mEmail, String mPassword) {
        this.mFullname = mFullname;
        this.mPhoneNumber = mPhoneNumber;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
    }

    public Smsar(String mUsername, String mFullname, String mPhoneNumber){

        this.mUsername=mUsername;
        this.mFullname=mFullname;
        this.mPhoneNumber=mPhoneNumber;


    }

    public Smsar() {

    }
    public Smsar(ArrayList<Smsar> list) {
        this.mUsername=list.get(0).toString();
        this.mFullname=list.get(1).toString();
        this.mPhoneNumber=list.get(2).toString();

    }

    public String getmFullname() {
        return mFullname;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
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
