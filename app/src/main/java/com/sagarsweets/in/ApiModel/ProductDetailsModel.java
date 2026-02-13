package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductDetailsModel {

    @SerializedName("default_image")
    private String defaultImage;

    private List<ImageModel> images;

    @SerializedName("brand_name")
    private String brandName;

    @SerializedName("product_title")
    private String productTitle;

    private float rating;
    private Integer ratingCount;

    @SerializedName("views_count")
    private int viewsCount;

    private String description;

    private List<SpecificationModel> specification;

    private int mrp;

    @SerializedName("selling_price")
    private int sellingPrice;

    @SerializedName("discount_percentage")
    private double discountPercentage;
    private Integer stock;

    public Integer getStock() {
        return stock;
    }

    @SerializedName("expected_day")
    private String expectedDay;

    @SerializedName("related_product")
    private List<ProductModel> relatedProduct;

    // ✅ FIX: Size list
    @SerializedName("Size")
    private List<SizeModel> sizeList;

    @SerializedName("cart_count")
    private int cartCount;

    // Local UI state (NOT from API)
    private int selectedSizePosition = -1;
    @SerializedName("isWishListedMain")
    Boolean isWishListedMain;
    // ---------- GETTERS ----------


    public Boolean getWishListedMain() {
        return isWishListedMain;
    }

    public void setWishListedMain(Boolean wishListedMain) {
        isWishListedMain = wishListedMain;
    }

    public List<SizeModel> getSizeList() {
        return sizeList;
    }

    public int getCartCount() {
        return cartCount;
    }

    public int getSelectedSizePosition() {
        return selectedSizePosition;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public List<ImageModel> getImages() {
        return images;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public float getRating() {
        return rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public String getDescription() {
        return description;
    }

    public List<SpecificationModel> getSpecification() {
        return specification;
    }

    public int getMrp() {
        return mrp;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public String getExpectedDay() {
        return expectedDay;
    }

    public List<ProductModel> getRelatedProduct() {
        return relatedProduct;
    }

    public void setSelectedSizePosition(int selectedSizePosition) {
        this.selectedSizePosition = selectedSizePosition;
    }
}

