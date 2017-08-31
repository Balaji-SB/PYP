package com.android.pyp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.StrictMode;
import android.widget.Toast;

import com.android.pyp.usermodule.LoginActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    public static final int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "WCX";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERID = "userId";
    public static final String KEY_CONTACTNO = "contactno";
    public static final String KEY_PROFILEPIC = "profilePic";
    public static final String KEY_REGTYPE = "regType";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_EMAIL = "emailId";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_RANDOMCODE = "random_code";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_COMP_ID = "comp_id";
    public static final String KEY_RECRUITERID = "recruiterId";
    public static final String IS_INTERNET_AVAIL = "IsInternetAvail";
    public static final String KEY_TOKEN = "token";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void createLoginSession(String userId, String email, String username, String password, String contactno, String profilePic) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_CONTACTNO, contactno);
        editor.putString(KEY_PROFILEPIC, profilePic);
        editor.commit();
    }


    public boolean checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            _context.startActivity(i);
        }
        return isLoggedIn();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }


    public void setProductNames(List<String> listOfCategoryNames) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Set<String> set = new HashSet<>();
        set.addAll(listOfCategoryNames);
        editor.putStringSet("key", set);
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Toast.makeText(_context, "User Logged out Successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void storeRegIdInPref(String token) {
        editor.putString("regId", token);
        editor.commit();
    }
}
