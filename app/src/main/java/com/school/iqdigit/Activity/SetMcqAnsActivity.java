package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.McqLayout;
import com.school.iqdigit.Model.McqListResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetMcqAnsActivity extends AppCompatActivity {
    private Intent mainintent;
    private String hw_id = "";
    private String TAG = "SetMcqAnsActivity";
    private RecyclerView rvMcqAns;
    private ProgressDialog progressDialog;
    private List<McqLayout> mcqlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mcq_ans);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Assignment");
        progressDialog.setMessage("Wait a moment while \nassignment is Uploading....");
        progressDialog.create();
        progressDialog.setCancelable(false);
        rvMcqAns = findViewById(R.id.rvMcqAns);
        mainintent = getIntent();
        hw_id = mainintent.getStringExtra("hw_id");
        Log.d(TAG , hw_id+" hw_id");
        if(InternetCheck.isInternetOn(SetMcqAnsActivity.this) == true)
        {
            getmcqlist();
        }else {
            showsnackbar();
        }

    }

    private void getmcqlist() {
        progressDialog.show();
       // Call<McqListResponse> call = RetrofitClient.getInstance().getApi().getMcqListResponse("176");
        Call<McqListResponse> call = RetrofitClient.getInstance().getApi().getMcqListResponse(hw_id);
        call.enqueue(new Callback<McqListResponse>() {
            @Override
            public void onResponse(Call<McqListResponse> call, Response<McqListResponse> response) {
                if (response.body().getError() == false) {
                    if(response.body().getMcqLayout() != null) {
                        mcqlist = response.body().getMcqLayout();

                    }else {
                        Log.d(TAG,"no data available");
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<McqListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, t.getMessage());
            }
        });

    }

    private void showsnackbar() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(SetMcqAnsActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
