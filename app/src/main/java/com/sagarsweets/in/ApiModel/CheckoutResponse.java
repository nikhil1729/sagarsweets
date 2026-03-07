package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class CheckoutResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("result")
    private ResultCheckout result;

    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ResultCheckout getResult() {
        return result;
    }

    public void setResult(ResultCheckout result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
