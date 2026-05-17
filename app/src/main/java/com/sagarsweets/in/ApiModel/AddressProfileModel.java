package com.sagarsweets.in.ApiModel;

public class AddressProfileModel {
    private String type;
    private String address;
    private String phone;

    public AddressProfileModel(String type, String address, String phone) {
        this.type = type;
        this.address = address;
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
