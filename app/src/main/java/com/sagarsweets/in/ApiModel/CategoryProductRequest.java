package com.sagarsweets.in.ApiModel;

import android.content.Context;

import com.sagarsweets.in.utils.DeviceInfo;

public class CategoryProductRequest {
    String category_id;
    String pincode;
    Integer page_size;
    Integer page_number;
    String sort;
    String user_id;
    String device;
    String filter_sub_category_id;
    String filter_max,filter_min;
    String filter_rating;

    public CategoryProductRequest(String category_id, String pincode,
                                  Integer page_size, Integer page_number,
                                  String sort, String user_id,
                                  Context context,
                                  String filter_sub_category_id,
                                  String filter_max,
                                  String filter_min,
                                  String filter_rating) {
        this.category_id = category_id;
        this.pincode = pincode;
        this.page_size = page_size;
        this.page_number = page_number;
        this.sort = sort;
        this.user_id = user_id;
        this.device = DeviceInfo.getDeviceString(context);
        this.filter_sub_category_id = filter_sub_category_id;
        this.filter_max = filter_max;
        this.filter_min = filter_min;
        this.filter_rating = filter_rating;
    }

    public String getFilter_rating() {
        return filter_rating;
    }

    public void setFilter_rating(String filter_rating) {
        this.filter_rating = filter_rating;
    }

    public String getFilter_sub_category_id() {
        return filter_sub_category_id;
    }

    public void setFilter_sub_category_id(String filter_sub_category_id) {
        this.filter_sub_category_id = filter_sub_category_id;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
