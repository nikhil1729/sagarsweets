package com.sagarsweets.in.Session;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int productId;
    private String productName;
    private String productImage;
    private double price;
    private int quantity;
    private int sizeId;
    private int userId;   // important if login system exists
    private String sizeSelectedName;

    public CartItem(int productId, String productName,
                    String productImage, double price, int quantity,
                    int sizeId, int userId, String sizeSelectedName) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.price = price;
        this.quantity = quantity;
        this.sizeId = sizeId;
        this.userId = userId;
        this.sizeSelectedName = sizeSelectedName;
    }




    // Getters & Setters


    public String getSizeSelectedName() {
        return sizeSelectedName;
    }

    public void setSizeSelectedName(String sizeSelectedName) {
        this.sizeSelectedName = sizeSelectedName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

