package com.school.iqdigit.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.school.iqdigit.Modeldata.Staff;

import java.util.Objects;

public class SharedPrefManager2 {
    private static final String SHARED_PREF_NAME2 = "my_shared_pref2";
    private static SharedPrefManager2 mInstance;
    private Context mCtx;

    private SharedPrefManager2(Context mCtx){
        this.mCtx = mCtx;
    }
    public static synchronized SharedPrefManager2 getInstance(Context mCtx){
        if(mInstance == null){
            mInstance = new SharedPrefManager2(mCtx);
        }
        return mInstance;
    }
    public void saveStaff(Staff staff){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", staff.getId());
        editor.putString("phone",staff.getPhone_number());
        editor.putString("name",staff.getName());
        editor.putString("gender",staff.getGender());
        editor.putString("fathername",staff.getFathername());
        editor.putString("dob",staff.getDob());
        editor.putString("email",staff.getEmail());
        editor.putString("status", staff.getStatus());
        editor.putString("salery", staff.getSalery());
        editor.putString("dojoining", staff.getDojoining());
        editor.putString("depname", staff.getDepname());
        editor.putString("postname", staff.getPostname());
        editor.putString("address", staff.getAddress());
        editor.putString("imgpath", staff.getImgpath());
        editor.putString("user_type", staff.getUser_type());
                editor.apply();
    }


    public boolean isloggedin(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        return !Objects.equals(sharedPreferences.getString("id", "-1"), "-1");
    }
    public Staff getStaff(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        return new Staff(
                sharedPreferences.getString("id", "-1"),
                sharedPreferences.getString("phone",null),
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("gender",null),
                sharedPreferences.getString("fathername",null),
                sharedPreferences.getString("dob",null),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("status",null),
                sharedPreferences.getString("salery",null),
                sharedPreferences.getString("dojoining",null),
                sharedPreferences.getString("depname",null),
                sharedPreferences.getString("postname",null),
                sharedPreferences.getString("address",null),
                sharedPreferences.getString("imgpath",null),
                sharedPreferences.getString("user_type",null)

        );
    }

    public void clear(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
