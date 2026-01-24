package com.sagarsweets.in.ApiModel;

import java.util.List;

public class TopCategoryDataModel {
    int id;

    public int getId() {
        return id;
    }

    String category_name;
    String image;

    public String getImage() {
        return image;
    }

    List<ProductModel> product;

    public String getCategory_name() {
        return category_name;
    }

    public List<ProductModel> getProduct() {
        return product;
    }
}
