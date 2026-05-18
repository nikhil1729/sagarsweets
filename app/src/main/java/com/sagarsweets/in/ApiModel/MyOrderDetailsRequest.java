package com.sagarsweets.in.ApiModel;

public class MyOrderDetailsRequest {
    String userId;
    String txnId;
    String device;

    public MyOrderDetailsRequest(String userId, String txnId, String device) {
        this.userId = userId;
        this.txnId = txnId;
        this.device = device;
    }
}
