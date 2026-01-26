package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("pageCount")
    private int pageCount;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("pageSize")
    private int pageSize;

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}
