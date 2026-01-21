package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class SizeModelProductDetails {
    private int id;
    private String title;
    private int stock;
    private String mrp;

    @SerializedName("selling_price")
    private String sellingPrice;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getStock() {
        return stock;
    }

    public String getMrp() {
        return mrp;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }
}
