package com.sagarsweets.in.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class PincodeSession {
    private static final String PREF_NAME = "sagar_session";

    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_CITY = "city";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_STATE = "state";
    private static final String KEY_PINCODE_ACTIVE = "pincode_active";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PincodeSession(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // ✅ Save pincode data
    public void savePincode(String pincode, String city, String district, String state) {
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_DISTRICT, district);
        editor.putString(KEY_STATE, state);
        editor.putBoolean(KEY_PINCODE_ACTIVE, true);
        editor.apply();
    }

    // ✅ Check if pincode saved
    public boolean hasPincode() {
        return pref.getBoolean(KEY_PINCODE_ACTIVE, false);
    }

    // ✅ Getters
    public String getPincode() {
        return pref.getString(KEY_PINCODE, "");
    }

    public String getCity() {
        return pref.getString(KEY_CITY, "");
    }

    public String getDistrict() {
        return pref.getString(KEY_DISTRICT, "");
    }

    public String getState() {
        return pref.getString(KEY_STATE, "");
    }

    // ❌ Clear pincode (change location)
    public void clearPincode() {
        editor.clear();
        editor.apply();
    }
}
