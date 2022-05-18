package com.school.iqdigit.Activity;

import static com.school.iqdigit.Activity.loginActivity.KEY_OTP;
import static com.school.iqdigit.Activity.loginActivity.USER_PREF;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Model.LiveClass1Response;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayFeeAlt extends AppCompatActivity {
    private ImageView back, imgHome;
    private ProgressDialog mProg;
    private String otp, mobileno, studentid;
    private String myBaseUrl = BuildConfig.BASE_UR;
    private String myURL;
    private SharedPreferences sp;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fee_webview);


        webView = findViewById(R.id.webViewLiveClass);
        back = findViewById(R.id.backbtn);
        imgHome = findViewById(R.id.imgHome);
        User user = SharedPrefManager.getInstance(this).getUser();
        mobileno = user.getPhone_number();
        studentid = user.getId();
        imgHome = findViewById(R.id.imgHome);
        sp = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        otp = sp.getString(KEY_OTP, "");
        mProg = new ProgressDialog(this);
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);
        mProg.show();



        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);



        if (InternetCheck.isInternetOn(this) == true) {
            geturl();
        } else {
            showsnackbar();
        }

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
                myURL = response.body().url_payfeenow+"&stud_id=" + studentid + "&mobile=" + mobileno + "&otp=" + otp;

                if (InternetCheck.isInternetOn(PayFeeAlt.this) == true) {
                    WebViewClients webViewClient = new WebViewClients(PayFeeAlt.this);
                    webView.setWebViewClient(webViewClient);
                    webView.loadUrl(myURL);
                    mProg.dismiss();
                } else {
                    showsnackbar();
                }

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(webView.getUrl().equals(myURL))
                        {
                            finish();
                        }else {
                            webView.goBack();
                        }
                    }
                });

            }
            @Override
            public void onFailure(Call<LiveClass1Response> call, Throwable t) {

            }
        });
    }


    private class WebViewClients extends WebViewClient {

        private Activity activity = null;

        public WebViewClients(Activity activity) {
            this.activity = activity;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProg.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mProg != null) {
                mProg.dismiss();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url.contains("https")) {
                webView.loadUrl(url);
                mProg.dismiss();
                return true;
            }
            else {
                webView.loadUrl(url);
                mProg.dismiss();
                return true;
            }
        }
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
                        if (InternetCheck.isInternetOn(PayFeeAlt.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}