package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PolicyResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("result")
    private List<PolicyItem> result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<PolicyItem> getResult() {
        return result;
    }

    public void setResult(List<PolicyItem> result) {
        this.result = result;
    }
}
