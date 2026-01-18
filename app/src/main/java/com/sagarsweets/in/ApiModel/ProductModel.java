package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class ProductModel {

    @SerializedName("id")
    private String id;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("image_path")
    private String imagePath;

    @SerializedName("mrp")
    private String mrp;

    @SerializedName("selling_price")
    private String selling_price;

    @SerializedName("is_wishlist")
    private int isWishlist;

    private Float rating;
    private String ratingCount;

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = Float.parseFloat(rating);
    }

    public String getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getPrice() {
        return mrp;
    }

    public String getSalePrice() {
        return selling_price;
    }

    public int getIsWishlist() {
        return isWishlist;
    }
}

