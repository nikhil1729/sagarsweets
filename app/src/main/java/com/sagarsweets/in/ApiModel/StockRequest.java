package com.sagarsweets.in.ApiModel;

public class StockRequest {
    private int product_id;
    private int size_id;

    public StockRequest(int product_id, int size_id) {
        this.product_id = product_id;
        this.size_id = size_id;
    }
}
