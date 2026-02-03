package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class AboutUsModel {

    @SerializedName("about_us_title")
    public String title;

    @SerializedName("about_us_description")
    public String description;

    @SerializedName("presents_description")
    public String presents_description;

    @SerializedName("testimonial_description")
    public String testimonial_description;
    @SerializedName("about_us_img_first")
    public String imgFirst;

    @SerializedName("about_us_img_second")
    public String imgSecond;

    @SerializedName("about_us_img_third")
    public String imgThird;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgFirst() {
        return imgFirst;
    }

    public void setImgFirst(String imgFirst) {
        this.imgFirst = imgFirst;
    }

    public String getImgSecond() {
        return imgSecond;
    }

    public void setImgSecond(String imgSecond) {
        this.imgSecond = imgSecond;
    }

    public String getImgThird() {
        return imgThird;
    }

    public void setImgThird(String imgThird) {
        this.imgThird = imgThird;
    }

    public String getPresents_description() {
        return presents_description;
    }

    public void setPresents_description(String presents_description) {
        this.presents_description = presents_description;
    }

    public String getTestimonial_description() {
        return testimonial_description;
    }

    public void setTestimonial_description(String testimonial_description) {
        this.testimonial_description = testimonial_description;
    }
}
