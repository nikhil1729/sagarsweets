package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryProductResponse {
    @SerializedName("status")
    private boolean status;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("categoryDetails")
    private CategoryDetails categoryDetails;


    public List<CategoryModel> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<CategoryModel> subCategory) {
        this.subCategory = subCategory;
    }

    @SerializedName("subCategory")
    private List<CategoryModel> subCategory;

    @SerializedName("items")
    private List<ProductModel> items;

    @SerializedName("pagination")
    private Pagination pagination;

    public boolean isStatus() {
        return status;
    }

    public CategoryDetails getCategoryDetails() {
        return categoryDetails;
    }

    public List<ProductModel> getItems() {
        return items;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
