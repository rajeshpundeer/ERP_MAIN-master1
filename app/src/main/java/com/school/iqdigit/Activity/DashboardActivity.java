package com.school.iqdigit.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import static com.school.iqdigit.Activity.loginActivity.KEY_OTP;
import static com.school.iqdigit.Activity.loginActivity.USER_PREF;

public class DashboardActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView back;
    private ProgressDialog mProg;
    private String otp , mobileno ,teachetid;
    private String myBaseUrl = BuildConfig.BASE_UR;
    private String myURL;
    private SharedPreferences sp;
    private String TAG = "DashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final Staff staff = SharedPrefManager2.getInstance(DashboardActivity.this).getStaff();
        mobileno = staff.getPhone_number();
        teachetid = staff.getId();
        sp = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        otp = sp.getString(KEY_OTP, "");
        myURL = BuildConfig.BASE_UR + "reports/app_dashboard.php?teacher_id="+teachetid+"&mobile="+mobileno+"&otp="+otp;
        Log.d(TAG , myURL);
        webView = findViewById(R.id.webViewGuest);
        back = findViewById(R.id.backbtn);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (InternetCheck.isInternetOn(DashboardActivity.this) == true) {
            WebViewClients webViewClient = new WebViewClients(this);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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
            if (url.startsWith(myBaseUrl)) {
                webView.loadUrl(url);
                mProg.dismiss();
                return true;
            }
            else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                        if (InternetCheck.isInternetOn(DashboardActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}