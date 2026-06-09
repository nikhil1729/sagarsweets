package com.sagarsweets.in.ApiModel;

public class SaveDobRequest {
    String user_id;
    String device;
    String dob;

    public SaveDobRequest(String user_id, String device, String dob) {
        this.user_id = user_id;
        this.device = device;
        this.dob = dob;
    }
}
