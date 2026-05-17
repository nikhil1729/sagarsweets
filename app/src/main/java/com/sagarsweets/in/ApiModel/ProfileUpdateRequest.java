package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class ProfileUpdateRequest {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("dob")
    private String dob;

    @SerializedName("email")
    private String email;

    @SerializedName("current_password")
    private String currentPassword;

    @SerializedName("new_password")
    private String newPassword;

    @SerializedName("confirm_password")
    private String confirmPassword;

    @SerializedName("news_later")
    private String newsLater;

    private String device;
    // Constructor
    public ProfileUpdateRequest(String userId,
                                String dob,
                                String email,
                                String currentPassword,
                                String newPassword,
                                String confirmPassword,
                                String newsLater,
                                String device) {
        this.userId = userId;
        this.dob = dob;
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.newsLater = newsLater;
        this.device = device;
    }

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNewsLater() {
        return newsLater;
    }

    public void setNewsLater(String newsLater) {
        this.newsLater = newsLater;
    }
}
