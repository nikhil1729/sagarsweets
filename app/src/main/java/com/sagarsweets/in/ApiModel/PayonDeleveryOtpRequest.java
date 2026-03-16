package com.sagarsweets.in.ApiModel;

public class PayonDeleveryOtpRequest {
    String device_info;
    String userId;
    String addressId;
    String longitude;
    String latitude;

    public PayonDeleveryOtpRequest(String device_info, String userId, String addressId, String longitude, String latitude) {
        this.device_info = device_info;
        this.userId = userId;
        this.addressId = addressId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
