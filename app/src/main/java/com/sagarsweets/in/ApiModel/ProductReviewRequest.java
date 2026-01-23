package com.sagarsweets.in.ApiModel;

import android.content.Context;

import com.sagarsweets.in.utils.DeviceInfo;

public class ProductReviewRequest {
    Integer product_id;
    String user_id;
    String device;
    public ProductReviewRequest(Integer product_id, String user_id, Context context) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.device = DeviceInfo.getDeviceString(context);
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
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
}
