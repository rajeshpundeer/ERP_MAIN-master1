package com.school.iqdigit.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

public class SharedPrefManagerGuest {
    private static final String SHARED_PREF_NAME = "my_shared_pref1";
    private static SharedPrefManagerGuest mInstance;
    private Context mCtx;

    private SharedPrefManagerGuest(Context mCtx){
        this.mCtx = mCtx;
    }
    public static synchronized SharedPrefManagerGuest getInstance(Context mCtx){
        if(mInstance == null){
            mInstance = new SharedPrefManagerGuest(mCtx);
        }
        return mInstance;
    }
    public void saveUser(String phone){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone",phone);
        editor.apply();
    }

    public boolean isloggedin1(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return !Objects.equals(sharedPreferences.getString("phone", "-1"), "-1");
    }
    public String getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone", "-1");
    }
    public void clear(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
