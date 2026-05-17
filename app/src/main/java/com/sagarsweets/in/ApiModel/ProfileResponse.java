package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProfileResponse {

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("dob")
    private String dob;

    @SerializedName("age")
    private String age;

    @SerializedName("notification")
    private int notification;

    @SerializedName("profile_age")
    private String profileAge;

    @SerializedName("address")
    private List<Address> address;

    private Boolean status;
    private String message;

    // Getters and Setters


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public String getProfileAge() {
        return profileAge;
    }

    public void setProfileAge(String profileAge) {
        this.profileAge = profileAge;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

}