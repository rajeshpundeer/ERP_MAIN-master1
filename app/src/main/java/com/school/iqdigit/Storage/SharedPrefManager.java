package com.school.iqdigit.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.school.iqdigit.Modeldata.User;

import java.util.Objects;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx){
        this.mCtx = mCtx;
    }
    public static synchronized SharedPrefManager getInstance(Context mCtx){
        if(mInstance == null){
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }
    public void saveUser(User user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", user.getId());
        editor.putString("phone",user.getPhone_number());
        editor.putString("name",user.getName());
        editor.putString("p_class",user.getP_class());
        editor.putString("user_type", "student");
        editor.putString("incharge_id",user.getIncharge_id());
        editor.apply();
    }

    public boolean isloggedin(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return !Objects.equals(sharedPreferences.getString("id", "-1"), "-1");
    }
    public User getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("id", "-1"),
                sharedPreferences.getString("phone",null),
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("p_class",null),
                sharedPreferences.getString("incharge_id",null)

        );
    }
    public void clear(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
