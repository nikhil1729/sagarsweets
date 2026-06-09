package com.sagarsweets.in.ApiModel;

public class TokenRequest {
    String userId;
    String device;
    String token;

    public TokenRequest(String userId, String device, String token) {
        this.userId = userId;
        this.device = device;
        this.token = token;
    }
}
