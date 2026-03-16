package com.sagarsweets.in.ApiModel;

public class DeliveryAddress {

    private String name;
    private String mobile;
    private String short_address;
    private String full_address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getShort_address() {
        return short_address;
    }

    public void setShort_address(String short_address) {
        this.short_address = short_address;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }
}
