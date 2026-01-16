package com.sagarsweets.in.ApiModel;

public class ForgetPasswordRequest {
    private String mobile;
    private String otp;
    private String password;
    private String re_password;
    private String ip;
    private String device_info;

    public ForgetPasswordRequest(String mobile, String otp, String password, String rePassword, String ip, String device_info) {
        this.mobile = mobile;
        this.otp = otp;
        this.password = password;
        this.re_password = rePassword;
        this.ip = ip;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return re_password;
    }

    public void setRePassword(String rePassword) {
        this.re_password = rePassword;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }
}
