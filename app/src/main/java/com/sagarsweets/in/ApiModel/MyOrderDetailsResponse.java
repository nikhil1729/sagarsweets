package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderDetailsResponse {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("result")
    @Expose
    private Result result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {

        @SerializedName("txn_id")
        @Expose
        private String txnId;

        @SerializedName("ordered_time")
        @Expose
        private String orderedTime;

        @SerializedName("delivery_date")
        @Expose
        private String deliveryDate;

        @SerializedName("delivery_time")
        @Expose
        private String deliveryTime;

        @SerializedName("delivery_type")
        @Expose
        private String deliveryType;

        @SerializedName("current_status")
        @Expose
        private String currentStatus;

        @SerializedName("customer_name")
        @Expose
        private String customerName;

        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;

        @SerializedName("address")
        @Expose
        private String address;

        @SerializedName("product_details")
        @Expose
        private List<ProductDetail> productDetails;

        @SerializedName("total_product_amount")
        @Expose
        private String totalProductAmount;

        @SerializedName("delivery_charge")
        @Expose
        private String deliveryCharge;

        @SerializedName("coupon_code")
        @Expose
        private String couponCode;

        @SerializedName("coupon_discount")
        @Expose
        private String couponDiscount;

        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;

        public String getTxnId() {
            return txnId;
        }

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        public String getOrderedTime() {
            return orderedTime;
        }

        public void setOrderedTime(String orderedTime) {
            this.orderedTime = orderedTime;
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

        public String getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }

        public String getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(String currentStatus) {
            this.currentStatus = currentStatus;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<ProductDetail> getProductDetails() {
            return productDetails;
        }

        public void setProductDetails(List<ProductDetail> productDetails) {
            this.productDetails = productDetails;
        }

        public String getTotalProductAmount() {
            return totalProductAmount;
        }

        public void setTotalProductAmount(String totalProductAmount) {
            this.totalProductAmount = totalProductAmount;
        }

        public String getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(String deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public String getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
        }

        public String getCouponDiscount() {
            return couponDiscount;
        }

        public void setCouponDiscount(String couponDiscount) {
            this.couponDiscount = couponDiscount;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }
    }

    public static class ProductDetail {

        @SerializedName("image")
        @Expose
        private String image;

        @SerializedName("product_name")
        @Expose
        private String productName;

        @SerializedName("product_size")
        @Expose
        private String productSize;

        @SerializedName("quantity")
        @Expose
        private int quantity;

        @SerializedName("price")
        @Expose
        private String price;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductSize() {
            return productSize;
        }

        public void setProductSize(String productSize) {
            this.productSize = productSize;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}