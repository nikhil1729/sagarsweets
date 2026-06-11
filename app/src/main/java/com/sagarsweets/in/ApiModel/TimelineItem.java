package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class TimelineItem {

    @SerializedName("step")
    private String step;

    @SerializedName("completed")
    private boolean completed;

    @SerializedName("time")
    private String time;

    @SerializedName("time_ago")
    private String timeAgo;

    @SerializedName("message")
    private String message;

    public String getStep() {
        return step;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getTime() {
        return time;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public String getMessage() {
        return message;
    }
}
