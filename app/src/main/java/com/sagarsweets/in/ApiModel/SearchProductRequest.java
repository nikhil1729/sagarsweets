package com.sagarsweets.in.ApiModel;

import android.content.Context;

import com.sagarsweets.in.utils.DeviceInfo;

public class SearchProductRequest {
    String search_query,pincode,sort,user_id,category_id,filter_max,filter_min,filter_rating;
    Integer page_size;
    Integer page_number;
    String device;

    public SearchProductRequest(String searchQuery, String pincode, String sort, String userId, String filterSubCategoryId, String filterMax, String filterMin, String filterRating, Integer pageSize, Integer pageNumber, Context context) {
        this.search_query = searchQuery;
        this.pincode = pincode;
        this.sort = sort;
        this.user_id = userId;
        this.category_id = filterSubCategoryId;
        this.filter_max = filterMax;
        this.filter_min = filterMin;
        this.filter_rating = filterRating;
        this.page_size = pageSize;
        this.page_number = pageNumber;
        this.device = DeviceInfo.getDeviceString(context);
    }

    public String getSearch_query() {
        return search_query;
    }

    public void setSearch_query(String search_query) {
        this.search_query = search_query;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getFilter_max() {
        return filter_max;
    }

    public void setFilter_max(String filter_max) {
        this.filter_max = filter_max;
    }

    public String getFilter_min() {
        return filter_min;
    }

    public void setFilter_min(String filter_min) {
        this.filter_min = filter_min;
    }

    public String getFilter_rating() {
        return filter_rating;
    }

    public void setFilter_rating(String filter_rating) {
        this.filter_rating = filter_rating;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Integer getPage_number() {
        return page_number;
    }

    public void setPage_number(Integer page_number) {
        this.page_number = page_number;
    }
}
