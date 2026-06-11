package com.sagarsweets.in.ApiModel;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackOrderResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("status")
    private String status; // only for cancelled

    @SerializedName("current_status")
    private String currentStatus; // normal tracking

    @SerializedName("timeline")
    private List<TimelineItem> timeline;

    public boolean isSuccess() {
        return success;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public List<TimelineItem> getTimeline() {
        return timeline;
    }
}