package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductModel {

    @SerializedName("id")
    private int id;
    @SerializedName("isWIshListed")
    Boolean isWIshListed;
    @SerializedName("product_name")
    private String productName;

    @SerializedName("image_path")
    private String imagePath;

    @SerializedName("mrp")
    private String mrp;

    @SerializedName("selling_price")
    private String sellingPrice;

    @SerializedName("rating")
    private float rating;

    public String getMrp() {
        return mrp;
    }


    private Integer stock;

    public Integer getStock() {
        return stock;
    }

    @SerializedName("ratingCount")
    private String ratingCount;

    // IMPORTANT: Size is an ARRAY
    @SerializedName("Size")
    private List<SizeModel> sizeList;

    public List<SizeModel> getSizeList() {
        return sizeList == null ? new ArrayList<>() : sizeList;
    }

    public String getProductName() {
        return productName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public float getRating() {
        return rating;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public int getId() {
        return id;
    }

    public Boolean getWIshListed() {
        return isWIshListed;
    }

    public void setWIshListed(Boolean WIshListed) {
        isWIshListed = WIshListed;
    }
}
