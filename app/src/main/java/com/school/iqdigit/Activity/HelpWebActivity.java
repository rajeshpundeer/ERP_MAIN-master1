package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

public class HelpWebActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView back,imgHome;
    private ProgressDialog mProg;
    private String myURL = "http://iqwing.in/iqdigit/support/index.php";
    private SharedPreferences sp;
    private String TAG = "HelpWebActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_web);
        webView = findViewById(R.id.webViewGuest);
        back = findViewById(R.id.backbtn);
        imgHome = findViewById(R.id.imgHome);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (InternetCheck.isInternetOn(HelpWebActivity.this) == true) {
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

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(HelpWebActivity.this,MainActivity.class));
                finish();
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
            if (url.contains("https")) {
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
                        if (InternetCheck.isInternetOn(HelpWebActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
