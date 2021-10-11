package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.SchoolUrlResponse;
import com.school.iqdigit.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompayWebsiteActivity extends AppCompatActivity {
    private WebView webView = null;
    private ImageView back;
    public String WEBURL;
    private ProgressDialog mProg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_website);
        this.webView = (WebView) findViewById(R.id.webview);
        back = findViewById(R.id.backbtn);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");

        WEBURL =  "https://iqwing.in/iqdigit/";
        webView.loadUrl(WEBURL);
        mProg.show();

        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);

        WebViewClients webViewClient = new WebViewClients(this);
        webView.setWebViewClient(webViewClient);

        webView.loadUrl(WEBURL);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
            if (url.indexOf(WEBURL) > -1) return false;

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
