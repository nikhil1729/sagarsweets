package com.sagarsweets.in.ApiModel;

import com.sagarsweets.in.Session.WishlistItem;

import java.util.List;

public class WishListSyncronizeRequest {
    String user_id;
    String device;
    List<WishlistItem> wishlistItem;

    public WishListSyncronizeRequest(String user_id, String device, List<WishlistItem> wishlistItem) {
        this.user_id = user_id;
        this.device = device;
        this.wishlistItem = wishlistItem;
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

    public List<WishlistItem> getWishlistItem() {
        return wishlistItem;
    }

    public void setWishlistItem(List<WishlistItem> wishlistItem) {
        this.wishlistItem = wishlistItem;
    }
}
