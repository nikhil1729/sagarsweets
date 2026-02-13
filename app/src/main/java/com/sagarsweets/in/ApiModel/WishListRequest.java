package com.sagarsweets.in.ApiModel;

public class WishListRequest {
    String user_id;
    String product_id;
    String device_info;


    public WishListRequest(String user_id, String product_id, String device_info) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.device_info = device_info;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }
}
