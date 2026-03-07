package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class CouponDetails {
    @SerializedName("status")
    private boolean status;

    @SerializedName("discount")
    private int discount;

    @SerializedName("final_amount")
    private int finalAmount;

    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public int getDiscount() {
        return discount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public String getMessage() {
        return message;
    }
}
