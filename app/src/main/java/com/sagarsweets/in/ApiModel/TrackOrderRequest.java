package com.sagarsweets.in.ApiModel;

public class TrackOrderRequest {
    private String user_id;
    private String txn_id;
    private String device;

    public TrackOrderRequest(String user_id, String txn_id, String device) {
        this.user_id = user_id;
        this.txn_id = txn_id;
        this.device = device;
    }
}
