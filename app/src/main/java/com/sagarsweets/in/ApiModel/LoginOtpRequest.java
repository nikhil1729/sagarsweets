package com.sagarsweets.in.ApiModel;

public class LoginOtpRequest {
    private String mobile;
    private String otp;
    private String device_info;

    public LoginOtpRequest(String mobile, String otp, String device_info) {
        this.mobile = mobile;
        this.otp = otp;
        this.device_info = device_info;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }
}
