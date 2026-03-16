package com.sagarsweets.in.ApiModel;

import com.sagarsweets.in.Session.CartItem;

import java.io.Serializable;
import java.util.List;

public class CheckoutProcessData implements Serializable {

    private String userId;
    private String device;
    private List<CartItem> cartItems;
    private String deliveryType;
    private String addressId;
    private String date;
    private String timeSlot;
    private String paymentType;
    private String coupon;
    private  String podId;
    private  String longitude,latitude;

    public CheckoutProcessData(String userId, String device, String deliveryType, String addressId,
                               String date, String timeSlot, String paymentType, String coupon,
                               String podId, String longitude, String latitude) {
        this.userId = userId;
        this.device = device;
        //this.cartItems = cartItems;
        this.deliveryType = deliveryType;
        this.addressId = addressId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.paymentType = paymentType;
        this.coupon = coupon;
        this.podId = podId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getPodId() {
        return podId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDevice() {
        return device;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getDate() {
        return date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getCoupon() {
        return coupon;
    }
}

