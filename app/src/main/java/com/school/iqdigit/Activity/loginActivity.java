package com.school.iqdigit.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chaos.view.PinView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.LoginResponse;
import com.school.iqdigit.Model.OtpResponse;
import com.school.iqdigit.Model.staffloginresponse;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.Storage.SharedPrefManagerGuest;
import com.school.iqdigit.utility.InternetCheck;

import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginActivity extends AppCompatActivity {
    ProgressDialog mProg;
    String phone;
    EditText Phonenum;
    AppCompatButton login_btn, verifyCodeButton;
    AppCompatButton otpcall;
    LinearLayout phonenum_layout;
    LinearLayout verify_layout;
    PinView verifyCodeET;
    Intent mainintent;
    Dialog myDialog2;
    String otpphone;
    String school_phone;
    String txtmessage;
    private TextView tvMsg;
    int otp;
    private boolean checkinternet;
    private String TAG = "loginActivity";
    private SharedPreferences sp;
    public static final String USER_PREF = "USER_PREF";
    public static final String KEY_OTP = "KEY_OTP";
    private String strmob ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        myDialog2 = new Dialog(this);
        mainintent = getIntent();
        mProg = new ProgressDialog(loginActivity.this);
        login_btn = findViewById(R.id.login_btn);
        verifyCodeButton = findViewById(R.id.submit);
        otpcall = findViewById(R.id.otpcall);
        phonenum_layout = findViewById(R.id.phonenumlayout);
        verify_layout = findViewById(R.id.verify_layout);
        verifyCodeET = findViewById(R.id.pinView);
        Phonenum = findViewById(R.id.idphone);
        checkinternet = InternetCheck.isInternetOn(loginActivity.this);
        sp = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        tvMsg = findViewById(R.id.tvMsg);
        if (mainintent.getStringExtra("user_type").equals("student")) {
            txtmessage = mainintent.getStringExtra("msgstu");
        } else if (mainintent.getStringExtra("user_type").equals("staff")) {
            txtmessage = mainintent.getStringExtra("msgtea");
        } else if (mainintent.getStringExtra("user_type").equals("guest")) {
            txtmessage = mainintent.getStringExtra("msggue");
            }
        otpcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopuplogout();
            }
        });
        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = Objects.requireNonNull(verifyCodeET.getText()).toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    verifyCodeET.setError("Enter code...");
                    verifyCodeET.requestFocus();
                    return;
                }
                mProg.setMessage("Loading.....");
                mProg.setTitle(R.string.app_name);
                mProg.show();

                if (verifyCodeET.getText().toString().trim().equalsIgnoreCase(String.valueOf(otp))) {
                    if (mainintent.getStringExtra("user_type").equals("student")) {
                        checkinternet = InternetCheck.isInternetOn(loginActivity.this);
                        Toast.makeText(loginActivity.this, "Phone Verification Successful", Toast.LENGTH_LONG).show();
                        userlogin();
                    } else if (mainintent.getStringExtra("user_type").equals("staff")) {
                        Toast.makeText(loginActivity.this, "Phone Verification Successful", Toast.LENGTH_LONG).show();
                        stafflogin();
                    } else if (mainintent.getStringExtra("user_type").equals("guest")) {
                        mProg.show();
                        Toast.makeText(loginActivity.this, "Phone Verification Successful", Toast.LENGTH_LONG).show();
                        if (InternetCheck.isInternetOn(loginActivity.this) == true) {
                            guestlogin();
                        } else {
                            showsnackbar();
                        }
                    }
                    mProg.dismiss();
                } else {
                    mProg.dismiss();
                    Toast.makeText(loginActivity.this, "Please Enter Valid OTP", Toast.LENGTH_LONG).show();
                }

            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strmob = Phonenum.getText().toString();
                if (strmob.length() != 10) {
                    Toast.makeText(loginActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                } else {
                    mProg.setMessage("Loading.....");
                    mProg.setTitle(R.string.app_name);
                    mProg.show();
                    phone = Phonenum.getText().toString().trim();
                    if (mainintent.getStringExtra("user_type").equals("student")) {
                        Call<LoginResponse> call = RetrofitClient
                                .getInstance().getApi().userlogin(phone);
                        call.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                //Log.d(TAG, "response: " + response.body().getUser());
                                if (response.isSuccessful() && response.body() != null) {
                                    LoginResponse loginResponse = response.body();
                                    if (!loginResponse.isError()) {
                                      login();
                                    } else {
                                        mProg.dismiss();
                                        ShowNotificationPop("This Mobile number is not registered with No students, Please contact School for more details.");
                                        //Toast.makeText(loginActivity.this, "This Mobile number is not registered with No students, Please contact School for more details.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                mProg.dismiss();
                                Log.d("error; ", " " + t);
                            }
                        });
                    } else if (mainintent.getStringExtra("user_type").equals("staff")) {
                        Call<staffloginresponse> call = RetrofitClient.getInstance().getApi().staffloginbyphone(phone);
                        call.enqueue(new Callback<staffloginresponse>() {
                            @Override
                            public void onResponse(Call<staffloginresponse> call, Response<staffloginresponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    staffloginresponse staffloginresponses = response.body();
                                    if (!staffloginresponses.isError()) {
                                        login();
                                    } else {
                                        mProg.dismiss();
                                        ShowNotificationPop("This Mobile number is not registered , Please contact School for more details.");
                                        //Toast.makeText(loginActivity.this, "This Mobile number is not registered , Please contact School for more details.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<staffloginresponse> call, Throwable t) {
                                mProg.dismiss();
                            }
                        });
                    } else if (mainintent.getStringExtra("user_type").equals("guest")) {
                        login();
                    }

                }
            }
        });
        ShowNotificationPop(txtmessage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isloggedin()) {
            Intent intent = new Intent(loginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (SharedPrefManagerGuest.getInstance(this).isloggedin1()) {
            Intent intent = new Intent(loginActivity.this, GuestMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    public void ShowNotificationPop(String txt){
        Dialog dialog;
        TextView textView;
        FloatingActionButton imgcancel;
        Button btnOk;
        dialog = new Dialog(loginActivity.this);
        dialog.setContentView(R.layout.ic_common_text_popup);
        textView = dialog.findViewById(R.id.textmaintain);
        imgcancel = dialog.findViewById(R.id.img_cancel_dialog);
        btnOk = dialog.findViewById(R.id.btnOk);
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        textView.setText(txt);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.closeOptionsMenu();
        dialog.setCancelable(true);
        dialog.show();
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                onBackPressed();
            }
        };
    }
    public void ShowPopuplogout() {
        myDialog2.setContentView(R.layout.callforotplayout);
        Button call = myDialog2.findViewById(R.id.call_now);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpphone != null && !otpphone.equalsIgnoreCase("null") && !otpphone.equalsIgnoreCase("") && otpphone.length() > 0) {
                    Intent intentCall = new Intent(Intent.ACTION_CALL);
                    intentCall.setData(Uri.parse("tel:" + otpphone));
                    if (ActivityCompat.checkSelfPermission(loginActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    startActivity(intentCall);
                }
            }
        });
        Objects.requireNonNull(myDialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.closeOptionsMenu();
        myDialog2.show();
    }

    private void stafflogin() {
        SharedPrefManagerGuest.getInstance(loginActivity.this).clear();
        SharedPrefManager.getInstance(loginActivity.this).clear();
        SharedPrefManager2.getInstance(loginActivity.this).clear();
        Call<staffloginresponse> call = RetrofitClient.getInstance().getApi().staffloginbyphone(phone);
        call.enqueue(new Callback<staffloginresponse>() {
            @Override
            public void onResponse(Call<staffloginresponse> call, Response<staffloginresponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    staffloginresponse staffloginresponses = response.body();
                    if (!staffloginresponses.isError()) {
                        mProg.dismiss();
                        SharedPrefManager2.getInstance(loginActivity.this)
                                .saveStaff(staffloginresponses.getStaff());
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(KEY_OTP, String.valueOf(otp));
                        editor.commit();
                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("user_type", mainintent.getStringExtra("user_type"));
                        intent.putExtra("usertype", "staff");
                        startActivity(intent);
                    } else {
                        mProg.dismiss();
                        Toast.makeText(loginActivity.this, "This Mobile number is not registered , Please contact School for more details.", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(loginActivity.this, GuestMainActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<staffloginresponse> call, Throwable t) {
                mProg.dismiss();
            }
        });
    }

    private void userlogin() {
        SharedPrefManagerGuest.getInstance(loginActivity.this).clear();
        SharedPrefManager.getInstance(loginActivity.this).clear();
        SharedPrefManager2.getInstance(loginActivity.this).clear();
        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi().userlogin(phone);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                //Log.d(TAG, "response: " + response.body().getUser());
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (!loginResponse.isError()) {
                        mProg.dismiss();
                        SharedPrefManager.getInstance(loginActivity.this)
                                .saveUser(loginResponse.getUser());
                        Log.d("otp",String.valueOf(otp));
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(KEY_OTP, String.valueOf(otp));
                        editor.commit();
                        Intent intent = new Intent(loginActivity.this, loginByUser.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        mProg.dismiss();
                        Toast.makeText(loginActivity.this, "This Mobile number is not registered with No students, Please contact School for more details.", Toast.LENGTH_SHORT).show();
                       /* SharedPrefManagerGuest.getInstance(loginActivity.this)
                                .saveUser(loginResponse.getPhone());
                        startActivity(new Intent(loginActivity.this, GuestMainActivity.class));*/
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mProg.dismiss();
                Log.d("error; ", " " + t);
            }
        });

    }

    private void guestlogin() {
        SharedPrefManagerGuest.getInstance(loginActivity.this).clear();
        SharedPrefManager.getInstance(loginActivity.this).clear();
        SharedPrefManager2.getInstance(loginActivity.this).clear();
        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi().userlogin(phone);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
              //  Log.d(TAG, "response: " + response.body().getUser().getPhone_number());
                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse loginResponse = response.body();
                    if (!loginResponse.isError()) {
                        mProg.dismiss();
                        //Log.d(TAG, "response: " + loginResponse.getPhone());
                        SharedPrefManagerGuest.getInstance(loginActivity.this)
                                .saveUser(response.body().getUser().getPhone_number());
                        startActivity(new Intent(loginActivity.this, GuestMainActivity.class));
                    }else
                    {
                        mProg.dismiss();
                        Log.d(TAG, "response: " + loginResponse.getPhone());
                        SharedPrefManagerGuest.getInstance(loginActivity.this)
                                .saveUser(loginResponse.getPhone());
                        startActivity(new Intent(loginActivity.this, GuestMainActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mProg.dismiss();
                Log.d("error; ", " " + t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (verify_layout.getVisibility() == View.VISIBLE) {
            login_btn.setVisibility(View.VISIBLE);
            phonenum_layout.setVisibility(View.VISIBLE);
            verify_layout.setVisibility(View.GONE);
        } else {
            finish();
        }
    }

    private void showsnackbar() {
        if (mProg.isShowing()) {
            mProg.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(loginActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void login()
    {
        phone = Phonenum.getText().toString().trim();
        Random rand = new Random();
        if (strmob.equals("9999999999")) {
            otp = 999999;
        } else {
            otp = 100000 + rand.nextInt(888888);
        }
        Log.d("otp",otp+" otp");
        if (InternetCheck.isInternetOn(loginActivity.this) == true) {
            Call<OtpResponse> call = RetrofitClient.getInstance().getApi().sendotp(strmob, String.valueOf(otp));
            call.enqueue(new Callback<OtpResponse>() {
                @Override
                public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                    mProg.dismiss();
                    if (response.body() != null && response.isSuccessful()) {
                        Log.d(TAG, response.body().toString() + " ");
                        if (!response.body().isError()) {
                            login_btn.setVisibility(View.GONE);
                            phonenum_layout.setVisibility(View.GONE);
                            verify_layout.setVisibility(View.VISIBLE);
                            otpphone = response.body().getHelpnum();
                            school_phone = response.body().getAdmission_helpline();
                            Log.d(TAG, school_phone + " ");
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(getResources().getString(R.string.call_phone), school_phone).apply();
                            ShowNotificationPop("An OTP has been generated & sent. Please enter OTP in next Screen. If you do not receive OTP even after 5 Minutes. Please call help center & ask for OTP.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<OtpResponse> call, Throwable t) {
                    Log.d(TAG, t.getMessage() + " ");
                    mProg.dismiss();
                }
            });
        } else {
            showsnackbar();
        }
    }
}
