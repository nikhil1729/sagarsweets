package com.sagarsweets.in.ApiModel;

public class CancelProductRequest {
    String userId;
    String device;
    String txnId;
    String reson;
    String addtionalNotes;

    public CancelProductRequest(String userId, String device, String txnId, String reson, String addtionalNotes) {
        this.userId = userId;
        this.device = device;
        this.txnId = txnId;
        this.reson = reson;
        this.addtionalNotes = addtionalNotes;
    }
}
