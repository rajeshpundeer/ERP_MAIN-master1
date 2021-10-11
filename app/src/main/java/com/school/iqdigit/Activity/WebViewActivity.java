package com.school.iqdigit.Activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.OtpResponse;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Model.SubmitpaymentResponse;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.AvenuesParams;
import com.school.iqdigit.utility.Constants;
import com.school.iqdigit.utility.InternetCheck;
import com.school.iqdigit.utility.LoadingDialog;
import com.school.iqdigit.utility.RSAUtility;
import com.school.iqdigit.utility.ServiceUtility;

import retrofit2.Call;
import retrofit2.Callback;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class WebViewActivity extends AppCompatActivity {
    Intent mainIntent;
    String encVal;
    String vResponse;
    private Dialog myDialog2;
    private ProgressDialog mProg;
    private String strmob , Message;
    private String TAG = "WebViewActivity";
    private boolean checkinternet;
    private User user;
    private String ADDMNO;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_web_view);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        mainIntent = getIntent();
        myDialog2 = new Dialog(this);
        myDialog2.setCancelable(true);
        checkinternet = InternetCheck.isInternetOn(WebViewActivity.this);
        Log.d(TAG ,  mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
        //get rsa key method
        user = SharedPrefManager.getInstance(this).getUser();
        getprofile();
        get_RSA_key(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
    }

    private void getprofile() {
        Call<ProfileResponse> call = RetrofitClient
                .getInstance().getApi().getUserProfile(user.getId());
        call.enqueue(new Callback<ProfileResponse>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<ProfileResponse> call, retrofit2.Response<ProfileResponse> response) {
                ProfileResponse profileResponse = response.body();
                ADDMNO = profileResponse.getUserprofile().getAdm_no();
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(WebViewActivity.this, "Error :" + t, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           // Log.d(TAG ,"result"+ result.toString());
            // Dismiss the progress dialog
            LoadingDialog.cancelLoading();

            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    String orderidold =  mainIntent.getStringExtra(AvenuesParams.ORDER_ID);
                    Log.d(TAG , orderidold);
                    // process the html source code to get final status of transaction
                    String status = null;
                    if (html.indexOf("Failure") != -1) {
                        status = "Transaction Declined!";
                        finish();
                        Toast.makeText(WebViewActivity.this, ""+status, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else if (html.indexOf("Success") != -1 && html.indexOf(orderidold) != -1 ) {
                        feepaymentdone();
                        // ShowPopuplogout();
                    } else if (html.indexOf("Aborted") != -1) {
                        status = "Transaction Cancelled!";
                        finish();
                        Toast.makeText(WebViewActivity.this, ""+status, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        status = "Something went wrong ! Please Try Again !";
                        finish();
                        Toast.makeText(WebViewActivity.this, ""+status, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }

            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    LoadingDialog.cancelLoading();
                    if (url.indexOf("/ccavResponseHandler.php") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
                }
            });

            try {
                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), "UTF-8")
                        + "&" + AvenuesParams.NAME + "=" + URLEncoder.encode(user.getName() +" "+user.getId(), "UTF-8")
                        + "&" + AvenuesParams.ADDRESS + "=" + URLEncoder.encode(ADDMNO, "UTF-8")
                        + "&" + AvenuesParams.PHONE + "=" + URLEncoder.encode(user.getPhone_number(), "UTF-8")
                        + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID), "UTF-8")
                        + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ORDER_ID), "UTF-8") + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL), "UTF-8") + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.CANCEL_URL), "UTF-8") + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
                Log.v("ccpostdata", postData);
                webview.postUrl(Constants.TRANS_URL, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void feepaymentdone() {
        mProg.setCancelable(false);
        mProg.show();
        final Dialog dialog = new Dialog(WebViewActivity.this);
        Message = "Error..\nGet Receipt Failed, Please Contact Administrator\nOrder ID: "+mainIntent.getStringExtra(AvenuesParams.ORDER_ID)+"\nAmount: "+mainIntent.getStringExtra(AvenuesParams.AMOUNT);
        dialog.setTitle(Message);
        dialog.setCanceledOnTouchOutside(true);

        User user = SharedPrefManager.getInstance(this).getUser();
        strmob = user.getPhone_number();
        Call<SubmitpaymentResponse> call = RetrofitClient.getInstance().getApi().submitpayment(
                user.getId(),

                mainIntent.getStringExtra(AvenuesParams.FIDS),
                mainIntent.getStringExtra(AvenuesParams.AMOUNT),
                mainIntent.getStringExtra(AvenuesParams.ORDER_ID));

        call.enqueue(new Callback<SubmitpaymentResponse>() {
            @Override
            public void onResponse(Call<SubmitpaymentResponse> call, retrofit2.Response<SubmitpaymentResponse> response) {
                if(response.isSuccessful()){
                    if(!response.body().isError()){
                        if(mProg.isShowing()) {
                            mProg.dismiss();
                        }
                        ShowPopuplogout();
                    }else{
                        if(mProg.isShowing()) {
                            mProg.dismiss();
                        }
                        sendmessage();
                        dialog.show();
                    }
                }else{
                    if(mProg.isShowing()) {
                        mProg.dismiss();
                    }
                    sendmessage();
                    dialog.show();
                }
                if(mProg.isShowing())
                mProg.dismiss();
            }

            @Override
            public void onFailure(Call<SubmitpaymentResponse> call, Throwable t) {
                if(mProg.isShowing()) {
                    mProg.dismiss();
                }
                sendmessage();
                dialog.show();
            }
        });
    }

    private void sendmessage() {

        Call<OtpResponse> call = RetrofitClient.getInstance().getApi().sendotp(strmob, Message);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, retrofit2.Response<OtpResponse> response) {
                if( response.body() != null && response.isSuccessful()) {
                    if (!response.body().isError()) {
                    }
                }
                mProg.dismiss();
            }
            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                mProg.dismiss();
            }
        });
    }

    public void ShowPopuplogout() {
        myDialog2.setContentView(R.layout.transactionsuccess);
        Button yes = myDialog2.findViewById(R.id.yes_logout);
        Button no = myDialog2.findViewById(R.id.no_logout);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebViewActivity.this, feepayment.class);
                intent.putExtra("type", "Receipt");
                startActivity(intent);
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WebViewActivity.this, "Transaction Successful!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        Objects.requireNonNull(myDialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.closeOptionsMenu();
        myDialog2.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void get_RSA_key(final String ac, final String od) {
        LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(WebViewActivity.this,response,Toast.LENGTH_LONG).show();
                        LoadingDialog.cancelLoading();

                        if (response != null && !response.equals("")) {
                            vResponse = response;     ///save retrived rsa key
                            if (vResponse.contains("!ERROR!")) {
                                show_alert(vResponse);
                            } else {
                                new RenderView().execute();   // Calling async task to get display content
                            }


                        }
                        else
                        {
                            show_alert("No response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LoadingDialog.cancelLoading();
                        //Toast.makeText(WebViewActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AvenuesParams.ACCESS_CODE, ac);
                params.put(AvenuesParams.ORDER_ID, od);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                WebViewActivity.this).create();

        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);



        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        alertDialog.show();
    }
}