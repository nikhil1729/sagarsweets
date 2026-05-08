package com.sagarsweets.in.ApiModel;

public class MyOrderRequest {
    String user_id;
    int limit;
    int page;
    String search;

    public MyOrderRequest(String user_id, int limit, int page, String search) {
        this.user_id = user_id;
        this.limit = limit;
        this.page = page;
        this.search = search;
    }
}
