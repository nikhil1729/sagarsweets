package com.sagarsweets.in.ApiModel;

public class MyOrderRequest {
    String user_id;
    int limit;
    int page;
    String search;
    String order_status;
    String first_date,second_date;

    public MyOrderRequest(String user_id, int limit, int page, String search, String order_status, String first_date,String second_date) {
        this.user_id = user_id;
        this.limit = limit;
        this.page = page;
        this.search = search;
        this.order_status = order_status;
        this.first_date = first_date;
        this.second_date = second_date;
    }
}
