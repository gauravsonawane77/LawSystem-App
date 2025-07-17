package com.example.lawsystem;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ID = "id";
    private static final String KEY_ROLE = "role";

    public static void saveUserInfo(Context context, String name, String email, String id, String role) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public static String[] getUserInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String id = sharedPreferences.getString(KEY_ID, "");
        String role = sharedPreferences.getString(KEY_ROLE, "");
        return new String[]{name, email, id, role};
    }
}
