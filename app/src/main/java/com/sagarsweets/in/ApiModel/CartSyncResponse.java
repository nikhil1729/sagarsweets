package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;
import com.sagarsweets.in.Session.CartItem;

import java.util.List;

public class CartSyncResponse {
    @SerializedName("success")
    Boolean status;
    List<CartItem> serverCart;
    String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<CartItem> getServerCart() {
        return serverCart;
    }

    public void setServerCart(List<CartItem> serverCart) {
        this.serverCart = serverCart;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
