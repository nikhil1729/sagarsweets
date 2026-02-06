package com.sagarsweets.in.ApiModel;

public class ContactUsFormRequest {
    String name;
    String email;
    String message;
    String device;

    public ContactUsFormRequest(String name, String email, String message, String device) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
