package com.sagarsweets.in.ApiModel;

public class PodVerifyOtpRequest {
    String userId;
    String podId;
    String otp;
    String delivery_time;
    String delivery_date;
    String address_id;
    String device;
    String delivery_type;
    String longitude;
    String latitude;
    String coupon;


    public PodVerifyOtpRequest(String userId, String podId, String otp, String delivery_time, String delivery_date, String address_id, String device, String delivery_type, String longitude, String latitude,String coupon) {
        this.userId = userId;
        this.podId = podId;
        this.otp = otp;
        this.delivery_time = delivery_time;
        this.delivery_date = delivery_date;
        this.address_id = address_id;
        this.device = device;
        this.delivery_type = delivery_type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.coupon = coupon;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getUserId() {
        return userId;
    }

    public String getPodId() {
        return podId;
    }

    public String getOtp() {
        return otp;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public String getAddress_id() {
        return address_id;
    }

    public String getDevice() {
        return device;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}
