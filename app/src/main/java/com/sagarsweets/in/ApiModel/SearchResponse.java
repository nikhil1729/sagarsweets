package com.sagarsweets.in.ApiModel;

import java.util.List;

public class SearchResponse {
    private boolean status;
    private List<CategoryDetails> allCategory;
    private List<ProductModel> items;
    private Pagination pagination;

    public boolean isStatus() {
        return status;
    }

    public List<CategoryDetails> getAllCategory() {
        return allCategory;
    }

    public List<ProductModel> getItems() {
        return items;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
