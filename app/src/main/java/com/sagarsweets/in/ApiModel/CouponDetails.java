package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class CouponDetails {
    @SerializedName("status")
    private boolean status;

    @SerializedName("discount")
    private Double discount;

    @SerializedName("final_amount")
    private Double finalAmount;

    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public Double getDiscount() {
        return discount;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public String getMessage() {
        return message;
    }
}
