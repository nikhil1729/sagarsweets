package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private List<CategoryModel> data;

    public boolean isStatus() {
        return status;
    }

    public List<CategoryModel> getData() {
        return data;
    }
}
