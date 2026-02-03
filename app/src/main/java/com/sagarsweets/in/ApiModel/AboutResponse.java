package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AboutResponse {
    public boolean status;

    @SerializedName("about_us")
    public List<AboutUsModel> aboutUs;

    @SerializedName("our_team")
    public List<TeamModel> ourTeam;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<AboutUsModel> getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(List<AboutUsModel> aboutUs) {
        this.aboutUs = aboutUs;
    }

    public List<TeamModel> getOurTeam() {
        return ourTeam;
    }

    public void setOurTeam(List<TeamModel> ourTeam) {
        this.ourTeam = ourTeam;
    }
}
