package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class ContactUsResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("result")
    private Result result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    // Inner class for "result"
    public static class Result {

        @SerializedName("address")
        private String address;

        @SerializedName("email")
        private String email;

        @SerializedName("phone_number")
        private String phoneNumber;

        @SerializedName("facebook_link")
        private String facebookLink;

        @SerializedName("twitter_link")
        private String twitterLink;

        @SerializedName("google_map")
        private String googleMap;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getFacebookLink() {
            return facebookLink;
        }

        public void setFacebookLink(String facebookLink) {
            this.facebookLink = facebookLink;
        }

        public String getTwitterLink() {
            return twitterLink;
        }

        public void setTwitterLink(String twitterLink) {
            this.twitterLink = twitterLink;
        }

        public String getGoogleMap() {
            return googleMap;
        }

        public void setGoogleMap(String googleMap) {
            this.googleMap = googleMap;
        }
    }
}

