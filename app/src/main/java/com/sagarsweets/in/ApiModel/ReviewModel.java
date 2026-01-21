package com.sagarsweets.in.ApiModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.sagarsweets.in.utils.DeviceInfo;

public class ReviewModel implements Parcelable {

    private int id;
    private int product_id;
    private int rated_by;
    private float rating;
    private String email;
    private String review;
    private String created_at;
    private String ip;
    private String device_details;

    protected ReviewModel(Parcel in) {
        id = in.readInt();
        product_id = in.readInt();
        rated_by = in.readInt();
        rating = in.readFloat();
        email = in.readString();
        review = in.readString();
        created_at = in.readString();
        ip = in.readString();
        device_details = in.readString();
    }

    public static final Creator<ReviewModel> CREATOR = new Creator<ReviewModel>() {
        @Override
        public ReviewModel createFromParcel(Parcel in) {
            return new ReviewModel(in);
        }

        @Override
        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };

    public int getId() { return id; }
    public int getProduct_id() { return product_id; }
    public int getRated_by() { return rated_by; }
    public float getRating() { return rating; }
    public String getEmail() {
        return DeviceInfo.maskEmail(email);
    }
    public String getReview() { return review; }
    public String getCreated_at() {
        String timeAgo = DeviceInfo.getTimeAgo(created_at);
        return timeAgo;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(product_id);
        dest.writeInt(rated_by);
        dest.writeFloat(rating);
        dest.writeString(email);
        dest.writeString(review);
        dest.writeString(created_at);
        dest.writeString(ip);
        dest.writeString(device_details);
    }
}

