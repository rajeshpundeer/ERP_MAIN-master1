package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.AchievementAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.AchievementResponse;
import com.school.iqdigit.Model.Achievement_;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchievementActivity extends AppCompatActivity {
    private RecyclerView rvAchievement;
    private AchievementAdapter achievementAdapter;
    private LinearLayout noachievements;
    private ProgressDialog mProg;
    List<Achievement_> achievementsList;
    private ImageView backbtnAch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        rvAchievement = findViewById(R.id.rvAchievement);
        noachievements = findViewById(R.id.noachievements);
        backbtnAch = findViewById(R.id.backbtnAch);
        backbtnAch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        getachievements();
    }

    private void getachievements() {
        mProg.show();
        Call<AchievementResponse> call2 = RetrofitClient.getInstance().getApi().getachievements();

        call2.enqueue(new Callback<AchievementResponse>() {

            @Override
            public void onResponse(Call<AchievementResponse> call, Response<AchievementResponse> response) {
                if (InternetCheck.isInternetOn(AchievementActivity.this) == true) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getError()== false) {
                            mProg.dismiss();
                            noachievements.setVisibility(View.GONE);
                            achievementsList = response.body().getAchievements();
                            rvAchievement.setLayoutManager(new LinearLayoutManager(AchievementActivity.this));
                            achievementAdapter = new AchievementAdapter(AchievementActivity.this, achievementsList);
                            rvAchievement.setAdapter(achievementAdapter);
                        }
                    }else {
                        mProg.dismiss();
                        noachievements.setVisibility(View.VISIBLE);
                    }
                } else {
                    mProg.dismiss();
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<AchievementResponse> call, Throwable t) {
                mProg.dismiss();
                noachievements.setVisibility(View.VISIBLE);
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
                        if (InternetCheck.isInternetOn(AchievementActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
