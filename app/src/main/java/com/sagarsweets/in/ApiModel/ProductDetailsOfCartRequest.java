package com.sagarsweets.in.ApiModel;

import com.sagarsweets.in.Session.CartItem;
import com.sagarsweets.in.Session.WishlistItem;

import java.util.List;

public class ProductDetailsOfCartRequest {
    String user_id;
    String device;
    List<CartItem> cartItems;

    public ProductDetailsOfCartRequest(String user_id, String device, List<CartItem> cartItems) {
        this.user_id = user_id;
        this.device = device;
        this.cartItems = cartItems;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
