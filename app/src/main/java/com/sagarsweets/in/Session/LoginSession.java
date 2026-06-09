package com.sagarsweets.in.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSession {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_MOBILE = "mobile_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DOB = "dob";
    private static final String KEY_LOGIN = "is_logged_in";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public LoginSession(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Save session
    public void createLoginSession(String id, String name,String mobile, String email,String dob) {
        editor.putBoolean(KEY_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_DOB,dob);
        editor.apply();
    }

    // Get user id
    public String getUserId() {
        return pref.getString(KEY_ID, "");
    }

    // Get user name
    public String getUserName() {
        return pref.getString(KEY_NAME, "");
    }
    public String getMobile() {
        return pref.getString(KEY_MOBILE, "");
    }
    public String getEmail() {
        return pref.getString(KEY_EMAIL, "");
    }

    public String getDob(){ return pref.getString(KEY_DOB,"");}
    // Check login
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_LOGIN, false);
    }

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void setDob(String dob) {
        editor.putString(KEY_DOB, dob);
        editor.apply();
    }
    // Logout
    public void logout() {
        editor.clear();
        editor.apply();
    }

}
