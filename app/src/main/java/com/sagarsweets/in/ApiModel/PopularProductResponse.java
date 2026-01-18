package com.sagarsweets.in.ApiModel;

import java.util.List;

public class PopularProductResponse {
    private boolean status;
    private List<ProductModel> result;
    private int cart_count;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public List<ProductModel> getResult() {
        return result;
    }

    public int getCart_count() {
        return cart_count;
    }

    public String getMessage() {
        return message;
    }
}
