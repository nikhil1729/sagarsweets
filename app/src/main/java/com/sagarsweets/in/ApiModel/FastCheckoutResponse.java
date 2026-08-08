package com.sagarsweets.in.ApiModel;

public class FastCheckoutResponse {

    private Boolean status;
    private String message;
    private String name;
    private String selling_price;
    private int stock;
    private int address_id;
    private String default_address;
    private String size;
    private int delivery_charge;
    private String paymentGatewayKey;
    private String paymentGatewaySalt;
    private String deliveryDate;
    private String deliveryTimeSlot;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTimeSlot() {
        return deliveryTimeSlot;
    }

    public void setDeliveryTimeSlot(String deliveryTimeSlot) {
        this.deliveryTimeSlot = deliveryTimeSlot;
    }

    public String getPaymentGatewayKey() {
        return paymentGatewayKey;
    }

    public void setPaymentGatewayKey(String paymentGatewayKey) {
        this.paymentGatewayKey = paymentGatewayKey;
    }

    public String getPaymentGatewaySalt() {
        return paymentGatewaySalt;
    }

    public void setPaymentGatewaySalt(String paymentGatewaySalt) {
        this.paymentGatewaySalt = paymentGatewaySalt;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getDefault_address() {
        return default_address;
    }

    public void setDefault_address(String default_address) {
        this.default_address = default_address;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(int delivery_charge) {
        this.delivery_charge = delivery_charge;
    }
}
