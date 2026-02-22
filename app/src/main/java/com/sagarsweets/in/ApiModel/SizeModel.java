package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class SizeModel {

    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("mrp")
    private String mrp;

    public String getMrp() {
        return mrp;
    }

    @SerializedName("selling_price")
    private String sellingPrice;

    @SerializedName("is_stock")
    private int stock;

    public String getTitle() {
        return title;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public int getStock() {
        return stock;
    }

    public Integer getId() {
        return id;
    }
}
