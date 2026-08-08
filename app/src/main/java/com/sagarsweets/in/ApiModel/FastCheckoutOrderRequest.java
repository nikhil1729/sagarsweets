package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class FastCheckoutOrderRequest {

    @SerializedName("userId")
    private String userId;

    @SerializedName("productId")
    private String productId;

    @SerializedName("sizeId")
    private String sizeId;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("addressId")
    private Integer addressId;

    @SerializedName("paymentId")
    private String paymentId;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("device")
    private String device;

    @SerializedName("delivery_type")
    private String deliveryType;

    @SerializedName("coupon")
    private String coupon;

    @SerializedName("delivery_date")
    private String deliveryDate;

    @SerializedName("delivery_time")
    private String deliveryTime;

    public FastCheckoutOrderRequest() {
    }

    public FastCheckoutOrderRequest(
            String userId,
            String productId,
            String sizeId,
            String quantity,
            Integer addressId,
            String paymentId,
            String longitude,
            String latitude,
            String device,
            String deliveryType,
            String coupon,
            String deliveryDate,
            String deliveryTime
    ) {
        this.userId = userId;
        this.productId = productId;
        this.sizeId = sizeId;
        this.quantity = quantity;
        this.addressId = addressId;
        this.paymentId = paymentId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.device = device;
        this.deliveryType = deliveryType;
        this.coupon = coupon;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}