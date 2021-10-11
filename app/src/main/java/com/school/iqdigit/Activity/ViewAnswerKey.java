package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.AnsKeyadapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.McqResultResponse;
import com.school.iqdigit.Model.McqStudAnsWithKey;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAnswerKey extends AppCompatActivity {
    private RecyclerView rvViewAns;
    private ProgressDialog mProg;
    private String hw_id = "", user_type = "",student_id= "",status = "";
    private String TAG = "ViewAnswerKey";
    private List<McqStudAnsWithKey> McqStudAnsWithKeyList = new ArrayList<>();
    private ImageView back;
    private LinearLayout noAnsKeyavailable,llLocate,llanskey,llLine2,llLine1;
    private TextView tvAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer_key);
        mProg = new ProgressDialog(this);
        tvAns = findViewById(R.id.tvAns);
        Intent intent = getIntent();
        back = findViewById(R.id.backbtn1);
        hw_id = intent.getStringExtra("hw_id");
        user_type = intent.getStringExtra("user_type");
        student_id = intent.getStringExtra("student_id");
        status =   intent.getStringExtra("status");
        llLocate = findViewById(R.id.llLocate);
        llanskey = findViewById(R.id.llanskey);
        llLine2 = findViewById(R.id.llLine2);
        llLine1 = findViewById(R.id.llLine1);
        if(status.equals("submitted_student"))
        {
            llLine1.setVisibility(View.GONE);
            llLine2.setVisibility(View.GONE);
            tvAns.setVisibility(View.GONE);
            llLocate.setVisibility(View.GONE);
            llanskey.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_btn_view_two));
        }
        Log.d(TAG, hw_id + student_id);
        rvViewAns = findViewById(R.id.rvViewAns);
        noAnsKeyavailable = findViewById(R.id.noAnsKeyavailable);
        rvViewAns.setLayoutManager(new LinearLayoutManager(this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAnsKey();
    }

    private void getAnsKey() {
        if(InternetCheck.isInternetOn(ViewAnswerKey.this) == true) {
            mProg.setMessage("Loading.....");
            mProg.setTitle(R.string.app_name_main);
            mProg.show();
            Log.d(TAG, hw_id + " hw_id " + student_id + " student_id");
            Call<McqResultResponse> call = RetrofitClient.getInstance().getApi().getMcqResultResponse(hw_id, student_id);
            call.enqueue(new Callback<McqResultResponse>() {
                @Override
                public void onResponse(Call<McqResultResponse> call, Response<McqResultResponse> response) {
                    Log.d(TAG, response + " response");
                    if (response.body().getError() == false) {
                        if (response.body().getMcqStudAnsWithKey().size()>0) {
                            noAnsKeyavailable.setVisibility(View.GONE);
                            McqStudAnsWithKeyList = response.body().getMcqStudAnsWithKey();
                            AnsKeyadapter adapter = new AnsKeyadapter(getApplicationContext(), McqStudAnsWithKeyList,status);
                            rvViewAns.setAdapter(adapter);
                        } else {
                            noAnsKeyavailable.setVisibility(View.VISIBLE);
                        }
                    }else {
                        noAnsKeyavailable.setVisibility(View.VISIBLE);
                    }
                    mProg.dismiss();
                }

                @Override
                public void onFailure(Call<McqResultResponse> call, Throwable t) {
                    mProg.dismiss();
                    Log.d(TAG, t.getMessage());
                    noAnsKeyavailable.setVisibility(View.VISIBLE);
                }
            });
        }else {
            showsnackbar();
        }
    }
    private void showsnackbar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(ViewAnswerKey.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
