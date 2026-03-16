package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class PayonDeleveryOtpResponse {
    Boolean status;
    String message;
    @SerializedName("pod_id")
    String PodId;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPodId() {
        return PodId;
    }

    public void setPodId(String podId) {
        PodId = podId;
    }
}
