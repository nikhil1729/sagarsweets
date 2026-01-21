package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class RelatedProductModel {

    private int id;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("image_path")
    private String imagePath;

    private float rating;
    private int mrp;

    @SerializedName("selling_price")
    private int sellingPrice;

    private String description;

    @SerializedName("views_count")
    private int viewsCount;

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public float getRating() {
        return rating;
    }

    public int getMrp() {
        return mrp;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }
}

