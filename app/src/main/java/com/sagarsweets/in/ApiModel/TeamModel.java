package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

public class TeamModel {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("desination")
    private String designation;

    @SerializedName("about_him")
    private String aboutHim;

    @SerializedName("profile_pic")
    private String profilePic;

    @SerializedName("facebook_url")
    private String facebookUrl;

    @SerializedName("google_url")
    private String googleUrl;

    @SerializedName("twitter_url")
    private String twitterUrl;

    @SerializedName("created_by")
    private int createdBy;

    @SerializedName("updated_by")
    private int updatedBy;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("modified_ip")
    private String modifiedIp;

    // ---------------- GETTERS ----------------

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getAboutHim() {
        return aboutHim;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public String getGoogleUrl() {
        return googleUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getModifiedIp() {
        return modifiedIp;
    }
}
