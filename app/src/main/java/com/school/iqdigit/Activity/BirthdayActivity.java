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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.BirthdayAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.Birthday;
import com.school.iqdigit.Model.BirthdaysResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BirthdayActivity extends AppCompatActivity {
    private RecyclerView rvBirthday;
    private BirthdayAdapter birthdayAdapter;
    private ProgressDialog mProg;
    private ImageView backbtn;
    private List<Birthday> birthdayList = new ArrayList<>();
    private LinearLayout llNoBirthdays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        rvBirthday = findViewById(R.id.rvBirthdays);
        llNoBirthdays = findViewById(R.id.llNoBirthdays);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");
        getBirthdays();
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void getBirthdays() {
        mProg.create();
        if (InternetCheck.isInternetOn(BirthdayActivity.this) == true) {
            Call<BirthdaysResponse> call = RetrofitClient
                    .getInstance().getApi().getBirthdays();
            call.enqueue(new Callback<BirthdaysResponse>() {
                @Override
                public void onResponse(Call<BirthdaysResponse> call, Response<BirthdaysResponse> response) {
                    birthdayList = response.body().getBirthdays();
                    mProg.dismiss();
                    if (birthdayList.size() > 0) {
                        llNoBirthdays.setVisibility(View.GONE);
                        rvBirthday.setLayoutManager(new LinearLayoutManager(BirthdayActivity.this));
                        birthdayAdapter = new BirthdayAdapter(BirthdayActivity.this, birthdayList);
                        rvBirthday.setAdapter(birthdayAdapter);
                    } else {
                        llNoBirthdays.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<BirthdaysResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showsnackbar();
        }

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
                        if (InternetCheck.isInternetOn(BirthdayActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
