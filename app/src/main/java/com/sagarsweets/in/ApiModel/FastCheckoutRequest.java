package com.sagarsweets.in.ApiModel;

public class FastCheckoutRequest {
    private String userId;
    private String productId;
    private String sizeId;
    private String device;
    private String latitude;
    private String longitude;

    public FastCheckoutRequest(String userId, String productId,
                               String sizeId, String device,
                               String latitude,
                               String longitude) {
        this.userId = userId;
        this.productId = productId;
        this.sizeId = sizeId;
        this.device = device;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
