package com.school.iqdigit.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Model.LiveClass1Response;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Activity.loginActivity.KEY_OTP;
import static com.school.iqdigit.Activity.loginActivity.USER_PREF;

public class LiveClassStaff1Activity extends AppCompatActivity {
    private ImageView back, imgHome;
    private ProgressDialog mProg;
    private String otp, mobileno, teachetid;
    private String myBaseUrl = BuildConfig.BASE_UR;
    private String myURL;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class_staff1);
        back = findViewById(R.id.backbtn);
        imgHome = findViewById(R.id.imgHome);
        final Staff staff = SharedPrefManager2.getInstance(LiveClassStaff1Activity.this).getStaff();
        mobileno = staff.getPhone_number();
        teachetid = staff.getId();
        imgHome = findViewById(R.id.imgHome);
        sp = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        otp = sp.getString(KEY_OTP, "");
        mProg = new ProgressDialog(this);
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);
        mProg.show();
        if (InternetCheck.isInternetOn(this) == true) {
            geturl();
        } else {
            showsnackbar();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  startActivity(new Intent(LiveClasssActivity.this, MainActivity.class));
                finish();
            }
        });

    }
    private void geturl() {
        Call<LiveClass1Response> call = RetrofitClient.getInstance().getApi().getliveclassurl();
        call.enqueue(new Callback<LiveClass1Response>() {
            @Override
            public void onResponse(Call<LiveClass1Response> call, Response<LiveClass1Response> response) {
                mProg.dismiss();
                myURL = response.body().staffurl_prefix+"&teacher_id=" + teachetid + "&mobile=" + mobileno + "&otp=" + otp;
                runLiveclass(myURL);
            }
            @Override
            public void onFailure(Call<LiveClass1Response> call, Throwable t) {

            }
        });
    }
    public void runLiveclass(String url) {
        // If url contains mailto link then open Mail Intent
        boolean isAppInstalled = appInstalledOrNot("com.android.chrome");
        if (isAppInstalled) {
            Intent i = new Intent();
            i.setPackage("com.android.chrome");
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(LiveClassStaff1Activity.this)
                    .setTitle("Google Chrome Not Installed")
                    .setMessage("Web Browser Google Chrome is required to proced further.")
                    .setPositiveButton("Install Google Chrome",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.android.chrome&hl=en_IN"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("No, thanks",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "Google Chrome is required.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }).create();
            dialog.show();
        }
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return true;
    }

    private void showsnackbar() {
        if(mProg.isShowing()){
            mProg.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(LiveClassStaff1Activity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}