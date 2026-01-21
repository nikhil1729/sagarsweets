package com.sagarsweets.in.ApiModel;

public class ProductReviewRequest {
    Integer product_id;
    String user_id;

    public ProductReviewRequest(Integer product_id, String user_id) {
        this.product_id = product_id;
        this.user_id = user_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
