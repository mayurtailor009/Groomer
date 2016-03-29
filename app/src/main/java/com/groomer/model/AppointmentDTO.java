package com.groomer.model;

/**
 * Created by Deepak Singh on 29-Mar-16.
 */
public class AppointmentDTO {

    private String mUserName;
    private String mUserAddress;
    private String mUserTime;

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserAddress() {
        return mUserAddress;
    }

    public void setmUserAddress(String mUserAddress) {
        this.mUserAddress = mUserAddress;
    }

    public String getmUserTime() {
        return mUserTime;
    }

    public void setmUserTime(String mUserTime) {
        this.mUserTime = mUserTime;
    }
}
