package com.sagarsweets.in.Session;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wishlist")
public class WishlistItem {

    @PrimaryKey
    private int productId;



    public WishlistItem(int productId) {
        this.productId = productId;

    }

    public int getProductId() {
        return productId;
    }


}

