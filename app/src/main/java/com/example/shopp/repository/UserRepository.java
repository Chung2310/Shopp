package com.example.shopp.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.shopp.model.User;

public class UserRepository {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_NAME = "UserName";
    private static final String KEY_ID = "UserID";
    private static final String KEY_EMAIL = "UserEmail";
    private static final String KEY_TOKEN_ACCESS = "UserTokenAccess";
    private static final String KEY_TOKEN_REFRESH = "UserTokenRefresh";
    private static final String KEY_PHONE = "UserPhone";

    private static final String KEY_ADDRESS = "UserAddress";
    private static final String KEY_ROLE = "UserRole";
    private static final String KEY_AVATAR_URL = "UserAvatarUrl";
    private static final String KEY_BACKGROUND_URL = "UserBackgroundUrl";

    private final SharedPreferences prefs;

    public UserRepository(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(User user){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_NAME, user.getFullName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_ADDRESS, user.getAddress());
        editor.putString(KEY_TOKEN_ACCESS, user.getToken());
        editor.putString(KEY_TOKEN_REFRESH, user.getRefreshToken());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_ROLE, user.getRole());
        editor.putString(KEY_AVATAR_URL, user.getAvatarUrl());
        editor.putString(KEY_BACKGROUND_URL, user.getBackgroundUrl());

        // Thêm log cho tất cả trường
        Log.d("UserPrefs", "Saving user:");
        Log.d("UserPrefs", "ID: " + user.getId());
        Log.d("UserPrefs", "Full Name: " + user.getFullName());
        Log.d("UserPrefs", "Email: " + user.getEmail());
        Log.d("UserPrefs", "Address: " + user.getAddress());
        Log.d("UserPrefs", "Access Token: " + user.getToken());
        Log.d("UserPrefs", "Refresh Token: " + user.getRefreshToken());
        Log.d("UserPrefs", "Phone: " + user.getPhone());
        Log.d("UserPrefs", "Role: " + user.getRole());
        Log.d("UserPrefs", "Avatar URL: " + user.getAvatarUrl());
        Log.d("UserPrefs", "Background URL: " + user.getBackgroundUrl());

        editor.apply();
    }


    public User getUser(){
        int id = prefs.getInt(KEY_ID, -1);
        String fullName = prefs.getString(KEY_NAME, null);
        String email = prefs.getString(KEY_EMAIL, null);
        String address = prefs.getString(KEY_ADDRESS, null);
        String accessToken = prefs.getString(KEY_TOKEN_ACCESS, null);
        String refreshToken = prefs.getString(KEY_TOKEN_REFRESH, null);
        String phone = prefs.getString(KEY_PHONE, null);
        String role = prefs.getString(KEY_ROLE, null);
        String avatar = prefs.getString(KEY_AVATAR_URL, null);
        String background = prefs.getString(KEY_BACKGROUND_URL, null);

        Log.d("UserPrefs", "Loaded user:");
        Log.d("UserPrefs", "ID: " + id);
        Log.d("UserPrefs", "Full Name: " + fullName);
        Log.d("UserPrefs", "Email: " + email);
        Log.d("UserPrefs", "Address: " + address);
        Log.d("UserPrefs", "Access Token: " + accessToken);
        Log.d("UserPrefs", "Refresh Token: " + refreshToken);
        Log.d("UserPrefs", "Phone: " + phone);
        Log.d("UserPrefs", "Role: " + role);
        Log.d("UserPrefs", "Avatar URL: " + avatar);
        Log.d("UserPrefs", "Background URL: " + background);

        return new User(id, email, fullName, phone, address, avatar, background, role, accessToken, refreshToken);
    }


    public  void clearUser(){
        prefs.edit().clear().apply();
    }

}
