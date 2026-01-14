package com.sagarsweets.in.ApiModel;

public class LoginRequest {
    private String mobile_number;
    private String password;

    private String device;

    public LoginRequest(String mobile, String password, String device) {
        this.mobile_number = mobile;
        this.password = password;
        this.device = device;
    }
}
