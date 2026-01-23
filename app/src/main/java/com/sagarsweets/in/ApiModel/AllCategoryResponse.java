package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllCategoryResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private List<AllCategoryModel> data;

    public boolean isStatus() {
        return status;
    }

    public List<AllCategoryModel> getData() {
        return data;
    }
}
