package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class PickStoreAddress {
    @SerializedName("storeName")
    private String storeName;

    @SerializedName("contactNo")
    private String contactNo;

    @SerializedName("address")
    private StoreAddress address;

    public String getStoreName() {
        return storeName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public StoreAddress getAddress() {
        return address;
    }
}
