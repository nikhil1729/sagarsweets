package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class ResultCheckout {
    @SerializedName("currentTime")
    private String currentTime;

    @SerializedName("serverTimeZone")
    private String serverTimeZone;

    @SerializedName("userDefaultAddress")
    private UserDefaultAddress userDefaultAddress;

    @SerializedName("pickStoreAddress")
    private PickStoreAddress pickStoreAddress;

    @SerializedName("couponDetails")
    private CouponDetails couponDetails;

    @SerializedName("deliveryCharge")
    private String deliveryCharge;

    public String getCurrentTime() {
        return currentTime;
    }

    public String getServerTimeZone() {
        return serverTimeZone;
    }

    public UserDefaultAddress getUserDefaultAddress() {
        return userDefaultAddress;
    }

    public PickStoreAddress getPickStoreAddress() {
        return pickStoreAddress;
    }

    public CouponDetails getCouponDetails() {
        return couponDetails;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }
}
