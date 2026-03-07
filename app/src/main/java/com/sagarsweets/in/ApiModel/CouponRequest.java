package com.sagarsweets.in.ApiModel;

public class CouponRequest {
    String userId;
    String couponCode;
    String device;
    double totalAmount;

    public CouponRequest(String userId, String couponCode, String device, double totalAmount) {
        this.userId = userId;
        this.couponCode = couponCode;
        this.device = device;
        this.totalAmount = totalAmount;
    }
}
