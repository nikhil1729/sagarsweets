package com.sagarsweets.in.ApiModel;

public class ProfileRequest {
    String user_id;
    String device;

    public ProfileRequest(String user_id, String device) {
        this.user_id = user_id;
        this.device = device;
    }
}
