package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.AssSubAdapter;
import com.school.iqdigit.Adapter.CircularsAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Model.AssSubResponse;
import com.school.iqdigit.Model.Circular;
import com.school.iqdigit.Model.CircularsResponse;
import com.school.iqdigit.Modeldata.Activities;
import com.school.iqdigit.Modeldata.AssSub;
import com.school.iqdigit.Modeldata.FileDownloader;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class CircularsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Activities> activitiesList;
    private ProgressDialog mProg;
    private ImageView backbtn;
    private LinearLayout llNoCirculars;
    private String TAG = "CircularsActivity";
    private int STORAGE_PERMISSION = 1;
    private boolean checkinternet;
    private List<Circular> circularList;
    private CircularsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulars);
        recyclerView = findViewById(R.id.activitiesrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        llNoCirculars = (LinearLayout) findViewById(R.id.llNoCirculars);
        mProg = new ProgressDialog(this);
        checkinternet = InternetCheck.isInternetOn(CircularsActivity.this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");
       // mProg.show();
        backbtn = findViewById(R.id.backbtn);
        if(ContextCompat.checkSelfPermission(CircularsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                == PackageManager.PERMISSION_GRANTED ){
            getCirculars();
        }else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed for downloading Circular")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(CircularsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }, STORAGE_PERMISSION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .create().show();
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, STORAGE_PERMISSION);
            }
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void getCirculars() {
        if (InternetCheck.isInternetOn(CircularsActivity.this) == true) {
            User user = SharedPrefManager.getInstance(this).getUser();
            Call<CircularsResponse> call = RetrofitClient
                    .getInstance().getApi().getCircular();
            call.enqueue(new Callback<CircularsResponse>() {
                @Override
                public void onResponse(Call<CircularsResponse> call, Response<CircularsResponse> response) {
                    circularList = response.body().getCirculars();
                    if (circularList.size() > 0) {
                        llNoCirculars.setVisibility(View.GONE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CircularsActivity.this));
                        adapter = new CircularsAdapter(CircularsActivity.this, circularList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        llNoCirculars.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<CircularsResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showsnackbar();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                getCirculars();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
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
                        if (InternetCheck.isInternetOn(CircularsActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
