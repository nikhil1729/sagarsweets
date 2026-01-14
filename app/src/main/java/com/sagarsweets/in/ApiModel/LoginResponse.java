package com.sagarsweets.in.ApiModel;

public class LoginResponse {
    private boolean status;
    private String message;
    private User user;
    private String cart_item_save;



    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCart_item_save(String cart_item_save) {
        this.cart_item_save = cart_item_save;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public String getCartItemSave() {
        return cart_item_save;
    }
}
