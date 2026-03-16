package com.sagarsweets.in.ApiModel;

import com.sagarsweets.in.Session.CartItem;

import java.util.List;

public class PodVerifyOtpResponse {
    Boolean status;
    private List<Item> item;
    private String txn_id;
    private String coupon_discount;
    private String coupon_code;
    private String delivery_charge;
    private String delivery_date;
    private String delivery_time_slot;
    private String delivery_type;
    private int total_product_cost;
    private String payment_gateway;
    private DeliveryAddress delivery_address;
    private PickupAddress pickupAddress;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
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

    public int getTotal_product_cost() {
        return total_product_cost;
    }

    public void setTotal_product_cost(int total_product_cost) {
        this.total_product_cost = total_product_cost;
    }

    public String getPayment_gateway() {
        return payment_gateway;
    }

    public void setPayment_gateway(String payment_gateway) {
        this.payment_gateway = payment_gateway;
    }

    public DeliveryAddress getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(DeliveryAddress delivery_address) {
        this.delivery_address = delivery_address;
    }

    public PickupAddress getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(PickupAddress pickupAddress) {
        this.pickupAddress = pickupAddress;
    }
}
