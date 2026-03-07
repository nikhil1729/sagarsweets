package com.sagarsweets.in.ApiModel;

public class UserAddressRequest {
    String userId;

    public UserAddressRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
