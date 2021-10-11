package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.AchievementAdapter;
import com.school.iqdigit.Adapter.FeeStructureAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Model.AchievementResponse;
import com.school.iqdigit.Model.Achievement_;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeeStructureActivity1 extends AppCompatActivity {
    private RecyclerView rvFeestructure;
    private FeeStructureAdapter achievementAdapter;
    private LinearLayout nofeestructure;
    private ProgressDialog mProg;
    List<Achievement_> achievementsList;
    private ImageView backbtnAch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_structure1);

        rvFeestructure = findViewById(R.id.rvFeestructure);
        nofeestructure = findViewById(R.id.nofeestructure);
        backbtnAch = findViewById(R.id.backbtnAch);
        backbtnAch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");
        getfeestructure();
    }

    private void getfeestructure() {
        mProg.show();
        Call<AchievementResponse> call2 = RetrofitClient.getInstance().getApi().getfeestructure();

        call2.enqueue(new Callback<AchievementResponse>() {

            @Override
            public void onResponse(Call<AchievementResponse> call, Response<AchievementResponse> response) {
                if (InternetCheck.isInternetOn(FeeStructureActivity1.this) == true) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getError()== false) {
                            mProg.dismiss();
                            nofeestructure.setVisibility(View.GONE);
                            achievementsList = response.body().getAchievements();
                            rvFeestructure.setLayoutManager(new LinearLayoutManager(FeeStructureActivity1.this));
                            achievementAdapter = new FeeStructureAdapter(FeeStructureActivity1.this, achievementsList);
                            rvFeestructure.setAdapter(achievementAdapter);
                        }
                    }else {
                        mProg.dismiss();
                        nofeestructure.setVisibility(View.VISIBLE);
                    }
                } else {
                    mProg.dismiss();
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<AchievementResponse> call, Throwable t) {
                mProg.dismiss();
                nofeestructure.setVisibility(View.VISIBLE);
            }
        });
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
                        if (InternetCheck.isInternetOn(FeeStructureActivity1.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

}
