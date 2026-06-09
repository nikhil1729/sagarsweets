package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class NotificationItem {

    @SerializedName("id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("reference_id")
    private String referenceId;

    @SerializedName("type")
    private String type;

    @SerializedName("is_read")
    private String isRead;

    @SerializedName("created_at")
    private String createdAt;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getType() {
        return type;
    }

    public String getIsRead() {
        return isRead;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
