package com.sagarsweets.in.ApiModel;

import android.content.Context;

import com.sagarsweets.in.utils.DeviceInfo;

public class PapularProductHome {
    String pincode ;
    String user_id;
    String device;
    public PapularProductHome(String pincode, String user_id, Context context) {
        this.pincode = pincode;
        this.user_id = user_id;
        this.device = DeviceInfo.getDeviceString(context);
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
