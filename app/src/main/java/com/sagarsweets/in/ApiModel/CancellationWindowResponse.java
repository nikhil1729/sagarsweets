package com.sagarsweets.in.ApiModel;

public class CancellationWindowResponse {
    private String userId;
    private String txnId;
    private String device;

    public CancellationWindowResponse(String userId, String txnId, String device) {
        this.userId = userId;
        this.txnId = txnId;
        this.device = device;
    }
}
