package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NotificationResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("page")
    private int page;

    @SerializedName("limit")
    private int limit;

    @SerializedName("total_records")
    private int totalRecords;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("has_more")
    private boolean hasMore;

    @SerializedName("data")
    private List<NotificationItem> data;

    public boolean isStatus() {
        return status;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public List<NotificationItem> getData() {
        return data;
    }
}