package com.sagarsweets.in.ApiModel;

public class WishListByLoggedInUserRequest {
    String user_id;
    String pincode;
    String device;

    public WishListByLoggedInUserRequest(String user_id, String pincode, String device) {
        this.user_id = user_id;
        this.pincode = pincode;
        this.device = device;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
