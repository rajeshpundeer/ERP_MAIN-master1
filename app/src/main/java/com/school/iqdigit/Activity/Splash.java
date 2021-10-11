package com.school.iqdigit.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.MaintenanceResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.Storage.SharedPrefManagerGuest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    private Button login;
    private TextView powby;
    private Button llTeacherLogin, llGuestLogin;
    private String TAG = "Splash";
    private String maintenance = "0";
    public String version, announcement, message;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // get_app_details();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage("Wait a moment....");
        progressDialog.create();
        progressDialog.setCancelable(false);
        //progressDialog.show();
        login = findViewById(R.id.button);
        powby = findViewById(R.id.powby);
        llGuestLogin = findViewById(R.id.btnGuestLogin);
        llTeacherLogin = findViewById(R.id.btnTeacherLogin);
        get_app_details();

        powby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://iqwing.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this, loginActivity.class);
                intent.putExtra("user_type", "student");
                intent.putExtra("version", version);
                intent.putExtra("msgstu","Enter your registered mobile number. If the number matches with the Students Record, an OTP shall be sent.\n" +
                        " If the registered SIM is not placed in this mobile, even then OTP shall be sent. " +
                        "You can login App on multiple Mobile(s)  after verifying OTP each time.");
                startActivity(intent);
            }
        });
        llTeacherLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this, loginActivity.class);
                intent.putExtra("user_type", "staff");
                intent.putExtra("version", version);
                intent.putExtra("msgtea","Enter your registered mobile number.\n"+
                                 " If the number matches with the Teachers Record, an OTP shall be sent.\n"+
                        " If the registered SIM is not placed in this mobile, even then OTP shall be sent.\n"+
                        " You can login App on multiple Mobile(s)  after verifying OTP each time.");
                startActivity(intent);
            }
        });
        llGuestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this, loginActivity.class);
                intent.putExtra("user_type", "guest");
                intent.putExtra("version", version);
                intent.putExtra("msggue","Guest login allows visitors to login  after verifying  the Mobile Number.\n" +
                        "Enter your mobile number, and OTP shall be sent.");
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
       // get_app_details();
        if (Integer.parseInt(maintenance) == 1) {
            Intent intent = new Intent(Splash.this, MaintenanceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("message", message);
            startActivity(intent);
            finish();
        } else {
            if (SharedPrefManager.getInstance(Splash.this).isloggedin()) {
                Intent intent = new Intent(Splash.this,  MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("usertype", "student");
                intent.putExtra("version", version);
                startActivity(intent);
            } else if (SharedPrefManager2.getInstance(Splash.this).isloggedin()) {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("usertype", "staff");
                intent.putExtra("version", version);
                startActivity(intent);
            } else if (SharedPrefManagerGuest.getInstance(Splash.this).isloggedin1()) {
                Intent intent = new Intent(Splash.this, GuestMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("usertype", "guest");
                intent.putExtra("version", version);
                startActivity(intent);
            }
        }
    }

    private void get_app_details() {
        Call<MaintenanceResponse> call3 = RetrofitClient.getInstance().getApi().getappcheck();
        call3.enqueue(new Callback<MaintenanceResponse>() {
            @Override
            public void onResponse(Call<MaintenanceResponse> call, Response<MaintenanceResponse> response) {
               // progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getError() == false) {
                        Log.d(TAG, response.body().getCheck().getMaintenace() +" splash mentainance");
                        if(!response.body().getCheck().getMaintenace().equals("")) {
                            maintenance = response.body().getCheck().getMaintenace();
                            version = response.body().getCheck().getVersion();
                            message = response.body().getCheck().getMMessage();
                            announcement = response.body().getCheck().getAnnouncement();
                            if (Integer.parseInt(maintenance) == 1) {
                                Intent intent = new Intent(Splash.this, MaintenanceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("message", message);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MaintenanceResponse> call, Throwable t) {

            }
        });
    }

}
