package com.sagarsweets.in.ApiModel;

public class TopCategoryRequest {
    String pincode;
    String user_id;

    String device;

    public TopCategoryRequest(String pincode, String user_id, String device) {
        this.pincode = pincode;
        this.user_id = user_id;
        this.device = device;
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
}
