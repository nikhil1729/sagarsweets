package com.sagarsweets.in.ApiModel;

public class UpdateCartRequest {
    String user_id;
    int productId;
    int quantity;
    int sizeId;
    String device;

    public UpdateCartRequest(String user_id, int productId, int quantity, int sizeId, String device) {
        this.user_id = user_id;
        this.productId = productId;
        this.quantity = quantity;
        this.sizeId = sizeId;
        this.device = device;
    }
}
