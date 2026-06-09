package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class NotificationRequest {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("page")
    private int page;

    @SerializedName("limit")
    private int limit;

    public NotificationRequest(String userId, int page, int limit) {
        this.userId = userId;
        this.page = page;
        this.limit = limit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}