package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class CategoryDetails {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public CategoryDetails(String number, String selectSubCategory) {
        this.id = Integer.parseInt(number);
        this.name = selectSubCategory;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name;
    }
}
