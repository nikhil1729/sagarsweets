package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class UserDefaultAddress {
    Boolean status;
    String message;
    @SerializedName("address")
    private Address address;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
