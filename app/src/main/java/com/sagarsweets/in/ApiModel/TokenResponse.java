package com.sagarsweets.in.ApiModel;

public class TokenResponse {
    Boolean status;
    String message;

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
