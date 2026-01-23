package com.sagarsweets.in.ApiModel;

public class PincodeRequest {
    String pincode;
    String user_id;
    String device;
    String lon;
    String lat;

    public PincodeRequest(String pincode, String user_id, String device, String lon, String lat) {
        this.pincode = pincode;
        this.user_id = user_id;
        this.device = device;
        this.lon = lon;
        this.lat = lat;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
