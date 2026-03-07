package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class StoreAddress {
    @SerializedName("id")
    private int id;

    @SerializedName("partner_id")
    private int partnerId;

    @SerializedName("street_1")
    private String street1;

    @SerializedName("street_2")
    private String street2;

    @SerializedName("post_office_list")
    private String postOfficeList;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("country")
    private String country;

    @SerializedName("created_by")
    private int createdBy;

    @SerializedName("updated_by")
    private int updatedBy;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public int getId() {
        return id;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public String getStreet1() {
        return street1;
    }

    public String getStreet2() {
        return street2;
    }

    public String getPostOfficeList() {
        return postOfficeList;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
