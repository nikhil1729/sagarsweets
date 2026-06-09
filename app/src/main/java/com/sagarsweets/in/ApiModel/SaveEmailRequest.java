package com.sagarsweets.in.ApiModel;

public class SaveEmailRequest {
    private String user_id;
    private String device;
    private String email;

    public SaveEmailRequest(String userId, String device, String email) {
        this.user_id = userId;
        this.device = device;
        this.email = email;
    }
}
