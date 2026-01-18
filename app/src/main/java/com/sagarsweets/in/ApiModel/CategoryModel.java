package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
