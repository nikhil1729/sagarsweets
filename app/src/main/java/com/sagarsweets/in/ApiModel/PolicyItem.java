package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PolicyItem implements Serializable {

    @SerializedName("type")
    private String type;

    @SerializedName("content")
    private String content;

    @SerializedName("updated_at")
    private String updatedAt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

