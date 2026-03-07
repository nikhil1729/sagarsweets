package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;
import com.sagarsweets.in.utils.AddressFormatter;

public class AddAddressResponse {
    Boolean status;
    String message;
    @SerializedName("address_id")
    String addressId;
    @SerializedName("address")
    UserDefaultAddress address;

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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public UserDefaultAddress getAddress() {
        return address;
    }

    public void setAddress(UserDefaultAddress address) {
        this.address = address;
    }
}
