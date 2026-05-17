package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("address_id")
    private int addressId;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("email_id")
    private String emailId;

    @SerializedName("mobile_number")
    private String mobileNumber;

    @SerializedName("full_address")
    private String fullAddress;

    @SerializedName("city")
    private String city;

    @SerializedName("distric_name")
    private String districName;

    @SerializedName("state")
    private String state;

    @SerializedName("pincode")
    private int pincode;

    @SerializedName("land_mark")
    private String landMark;

    @SerializedName("address_type")
    private String addressType;

    @SerializedName("is_default")
    private int isDefault;


    public int getAddressId() {
        return addressId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getCity() {
        return city;
    }

    public String getDistricName() {
        return districName;
    }

    public String getState() {
        return state;
    }

    public int getPincode() {
        return pincode;
    }

    public String getLandMark() {
        return landMark;
    }

    public String getAddressType() {
        return addressType;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}