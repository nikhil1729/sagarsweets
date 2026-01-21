package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class ImageModel {

    private int id;

    @SerializedName("product_id")
    private int productId;

    private String image;

    @SerializedName("created_at")
    private String createdAt;

    public String getImage() {
        return image;
    }
}

