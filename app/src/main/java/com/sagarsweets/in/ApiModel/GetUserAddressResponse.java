package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetUserAddressResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;
    @SerializedName("address")
    private List<Address> address;

    public boolean isStatus() {
        return status;
    }

    public List<Address> getAddress() {
        return address;
    }


    public String getMessage() {
        return message;
    }
}
