package com.sagarsweets.in.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSession {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_LOGIN = "is_logged_in";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public LoginSession(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Save session
    public void createLoginSession(String id, String name) {
        editor.putBoolean(KEY_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
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

    // Check login
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_LOGIN, false);
    }

    // Logout
    public void logout() {
        editor.clear();
        editor.apply();
    }

}
