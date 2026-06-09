package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CancellationWindowRequest {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("reason")
    @Expose
    private Map<String, String> reason;

    @SerializedName("refund_amount")
    @Expose
    private String refundAmount;

    @SerializedName("message")
    @Expose
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Map<String, String> getReason() {
        return reason;
    }

    public void setReason(Map<String, String> reason) {
        this.reason = reason;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}