package com.school.iqdigit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.SchoolUrlResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewClientImpl extends WebViewClient {
    private String WEBURL;
    private Activity activity = null;

    public WebViewClientImpl(Activity activity) {
        this.activity = activity;

        Call<SchoolUrlResponse> call = RetrofitClient.getInstance().getApi().getschoollurl();
        call.enqueue(new Callback<SchoolUrlResponse>() {
            @Override
            public void onResponse(Call<SchoolUrlResponse> call, Response<SchoolUrlResponse> response) {
                WEBURL = response.body().getSchoolurl();
            }

            @Override
            public void onFailure(Call<SchoolUrlResponse> call, Throwable t) {

            }
        });

    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if(url.indexOf(WEBURL) > -1 ) return false;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        return true;
    }

}