package com.sagarsweets.in.ApiModel;

public class AddAddressRequest {

    private String device_info;
    private String latitude;
    private String longitude;
    private String user_id;
    private String pincode;
    private String fullName;
    private String email;
    private String full_address;
    private String mobileNumber;
    private String city;
    private String distric;
    private String state;
    private String landmark;
    private String addressType;

    public AddAddressRequest(String device_info, String latitude, String longitude,
                             String user_id, String pincode, String fullName, String email,
                             String full_address, String mobileNumber, String city,
                             String distric, String state, String landmark,
                             String addressType) {
        this.device_info = device_info;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user_id = user_id;
        this.pincode = pincode;
        this.fullName = fullName;
        this.email = email;
        this.full_address = full_address;
        this.mobileNumber = mobileNumber;
        this.city = city;
        this.distric = distric;
        this.state = state;
        this.landmark = landmark;
        this.addressType = addressType;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistric() {
        return distric;
    }

    public void setDistric(String distric) {
        this.distric = distric;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
}
