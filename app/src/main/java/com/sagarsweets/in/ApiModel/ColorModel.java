package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class ColorModel {
    private int id;
    private String title;

    @SerializedName("is_stock")
    private int stock;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getStock() { return stock; }
}

