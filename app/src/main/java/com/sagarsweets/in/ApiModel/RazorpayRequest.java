package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class RazorpayRequest {
    String userId;
    String device;
    String delivery_type;

    String address_id;
    String delivery_date;
    String delivery_time;
    String paymentType;
    String coupon;
    String payment_id;
    String longitude;
    String latitude;

    public RazorpayRequest(String userId, String device, String deliveryType,
                           String selectedAddress, String selectedDate, String selectedTimeSlot,
                           String paymentType, String receivedCoupon, String paymentId,
                           String longitude, String latitude) {
        this.userId = userId;
        this.device = device;
        this.delivery_type = deliveryType;
        this.address_id = selectedAddress;
        this.delivery_date = selectedDate;
        this.delivery_time = selectedTimeSlot;
        this.paymentType = paymentType;
        this.coupon = receivedCoupon;
        this.payment_id = paymentId;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
