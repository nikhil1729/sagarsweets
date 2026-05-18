package com.sagarsweets.in.ApiModel;

public class AddressSetDefaultRequest {
    private Integer addressId;
    private String userId;
    public AddressSetDefaultRequest(Integer addressId,String userId) {
        this.addressId = addressId;
        this.userId = userId;
    }
}
