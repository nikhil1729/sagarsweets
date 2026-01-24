package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopCategoryResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private List<TopCategoryDataModel> data;

    public boolean isStatus() {
        return status;
    }

    public List<TopCategoryDataModel> getData() {
        return data;
    }
}
