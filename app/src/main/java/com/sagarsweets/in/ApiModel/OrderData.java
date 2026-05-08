package com.sagarsweets.in.ApiModel;

public class OrderData {

    private String txn_id;
    private String ordered_date;
    private String coupon_code;
    private int total_product_amount;
    private String delivery_date;
    private String delivery_time_slot;
    private String delivery_type;
    private String image;
    private String short_address;

    private String item_count;
    private String order_status;
    private String status_time;


    // Getters & Setters


    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getStatus_time() {
        return status_time;
    }

    public void setStatus_time(String status_time) {
        this.status_time = status_time;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getOrdered_date() {
        return ordered_date;
    }

    public void setOrdered_date(String ordered_date) {
        this.ordered_date = ordered_date;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public int getTotal_product_amount() {
        return total_product_amount;
    }

    public void setTotal_product_amount(int total_product_amount) {
        this.total_product_amount = total_product_amount;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_time_slot() {
        return delivery_time_slot;
    }

    public void setDelivery_time_slot(String delivery_time_slot) {
        this.delivery_time_slot = delivery_time_slot;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShort_address() {
        return short_address;
    }

    public void setShort_address(String short_address) {
        this.short_address = short_address;
    }
}
