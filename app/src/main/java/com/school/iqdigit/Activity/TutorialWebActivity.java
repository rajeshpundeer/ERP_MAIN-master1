package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.school.iqdigit.R;

public class TutorialWebActivity extends AppCompatActivity {

    private String fileurl;
    private WebView webView1;
    private String TAG = "PdfWebViewActivity";
    private ProgressDialog mProg;
    private ImageView back;
    private String pdf_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_web);
        Bundle bundle = getIntent().getExtras();
        fileurl = bundle.getString("fileurl");
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        mProg.show();
        webView1 = (WebView)findViewById(R.id.containWebView);
        back = findViewById(R.id.backbtn);
        webView1.loadUrl("http://docs.google.com/gview?embedded=true&url=" + fileurl);
        webView1.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url){
                // do your stuff here
                mProg.dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
