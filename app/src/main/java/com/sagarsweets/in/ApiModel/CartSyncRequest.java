package com.sagarsweets.in.ApiModel;

import com.sagarsweets.in.Session.CartItem;

import java.util.List;

public class CartSyncRequest {
    String user_id;
    String device_info;
    List<CartItem> localCart;

    public CartSyncRequest(String user_id, String device_info, List<CartItem> localCart) {
        this.user_id = user_id;
        this.device_info = device_info;
        this.localCart = localCart;
    }
}
