package com.sagarsweets.in.ApiModel;

import com.sagarsweets.in.Session.CartItem;

import java.util.List;

public class CheckoutRequest {
    String device_info;
    List<CartItem> cartList;
    String user_id;
    String longitude,latitude;
    String coupon;

    public CheckoutRequest(String device_info, List<CartItem> cartList,
                           String user_id, String longitude, String latitude,
                           String coupon) {
        this.device_info = device_info;
        this.cartList = cartList;
        this.user_id = user_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.coupon = coupon;
    }
}
