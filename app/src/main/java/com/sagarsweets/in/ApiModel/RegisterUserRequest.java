package com.sagarsweets.in.ApiModel;

public class RegisterUserRequest {
    private String first_name;
    private String last_name;
    private String mobile;
    private String otp;
    private String password;
    private String re_password;

    private String ip;
    private String device;
    public RegisterUserRequest(String first_name, String last_name, String mobile, String otp,
                               String password, String re_password, String ip, String device) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobile = mobile;
        this.otp = otp;
        this.password = password;
        this.re_password = re_password;
        this.ip = ip;
        this.device = device;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
    }
}
