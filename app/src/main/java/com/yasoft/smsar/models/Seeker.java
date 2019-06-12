package com.yasoft.smsar.models;

import java.util.ArrayList;

public class Seeker {

    private  String mFullname;
    private  String mPhoneNumber;
    private  String mUsername;
    private  String mEmail;
    private  String mPassword;

    public Seeker() {

    }


    public Seeker(String mFullname, String mPhoneNumber, String mUsername, String mEmail, String mPassword) {
        this.mFullname = mFullname;
        this.mPhoneNumber = mPhoneNumber;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
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
