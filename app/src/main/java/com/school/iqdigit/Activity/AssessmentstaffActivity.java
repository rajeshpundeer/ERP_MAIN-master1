package com.school.iqdigit.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.instacart.library.truetime.TrueTime;
import com.school.iqdigit.Adapter.AssessmentStaffAdapter;
import com.school.iqdigit.Adapter.EvaluatedAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Model.AssessmentResponse;
import com.school.iqdigit.Model.AssessmentStaffResponse;
import com.school.iqdigit.Model.Assessment_list_staff;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Activity.loginActivity.KEY_OTP;
import static com.school.iqdigit.Activity.loginActivity.USER_PREF;

public class AssessmentstaffActivity extends AppCompatActivity {
    private ProgressDialog mProg;
    private String TAG = "AssessmentstaffActivity";
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private RecyclerView rvAssessmentStaff;
    private String _staffid = "";
    private List<Assessment_list_staff> assessmentstaffList = new ArrayList<>();
    private AssessmentStaffAdapter assessmentStaffAdapter;
    private ImageView backbtn, imgAddAssessment;
    private Timer timer;
    private Date currentdatetime;
    private SwipeRefreshLayout swiperefresh;
    private LinearLayout noavailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_staff);
        checkAndRequestPermissions();
        currentdatetime = new Date();
        rvAssessmentStaff = findViewById(R.id.rvAssessmentStaff);
        imgAddAssessment = findViewById(R.id.imgAddAssessment);
        noavailable = findViewById(R.id.noavailable);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");
        swiperefresh = findViewById(R.id.swiperefresh);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                final Staff staff = SharedPrefManager2.getInstance(AssessmentstaffActivity.this).getStaff();
                _staffid = staff.getId();
                Log.d(TAG, _staffid + " staffid");
                getAssessmentList();
            }
        });
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AssessmentstaffActivity.this, AddAssessmentActivity.class));
                finish();
            }
        });
        final Staff staff = SharedPrefManager2.getInstance(AssessmentstaffActivity.this).getStaff();
        _staffid = staff.getId();
        Log.d(TAG, _staffid + " staffid");
        getAssessmentList();
    }

    private void getAssessmentList() {
        if (InternetCheck.isInternetOn(AssessmentstaffActivity.this) == true) {
            mProg.show();
            Call<AssessmentStaffResponse> call2 = RetrofitClient.getInstance().getApi().getstaffAssessmentList(_staffid);
            // Call<AssessmentStaffResponse> call2 = RetrofitClient.getInstance().getApi().getstaffAssessmentList("2");
            call2.enqueue(new Callback<AssessmentStaffResponse>() {
                @Override
                public void onResponse(Call<AssessmentStaffResponse> call, Response<AssessmentStaffResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getError() == false) {
                            noavailable.setVisibility(View.GONE);
                            mProg.dismiss();
                            assessmentstaffList = response.body().getAssessment_list_staff();
                            Log.d(TAG, response.body().getAssessment_list_staff() + " Assessment_list_staff");
                            if (assessmentstaffList.size() > 0) {
                                Collections.reverse(assessmentstaffList);
                                timer = new Timer();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                timer = new Timer();
                                TimerTask timerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        (AssessmentstaffActivity.this).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                                StrictMode.setThreadPolicy(policy);
                                                try {
                                                    TrueTime.build().initialize();
                                                    Date TIMENOW = TrueTime.now();
                                                    String DATEFORMAT = "dd-MM-yyyy HH:mm:ss";
                                                    SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
                                                    String currentdate = sdf.format(TIMENOW);
                                                    String newcurrentdate = currentdate.replace("-", "/");
                                                    currentdatetime = simpleDateFormat.parse(newcurrentdate);
                                                } catch (ParseException | IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if(currentdatetime!= null) {
                                                    rvAssessmentStaff.setLayoutManager(new LinearLayoutManager(AssessmentstaffActivity.this));
                                                    assessmentStaffAdapter = new AssessmentStaffAdapter(AssessmentstaffActivity.this, assessmentstaffList, currentdatetime);
                                                    rvAssessmentStaff.setAdapter(assessmentStaffAdapter);
                                                }
                                            }
                                        });
                                    }
                                };
                                timer.scheduleAtFixedRate(timerTask, 0, 50000);
                            } else {
                                noavailable.setVisibility(View.VISIBLE);
                                //Toast.makeText(getApplicationContext(), "Yet! No Assessment Assigned by you.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AssessmentStaffResponse> call, Throwable t) {
                    mProg.dismiss();
                    noavailable.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "Yet! No Assessment Assigned by you.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showsnackbar();
        }
    }

    private boolean checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRecordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for both permissions
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "sms & location services permission granted");
                } else {
                    Log.d(TAG, "Some permissions are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                        showDialogOK("Service Permissions are required for this app",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                finish();
                                                break;
                                        }
                                    }
                                });
                    }
                    //permission is denied (and never ask again is  checked)
                    //shouldShowRequestPermissionRationale will return false
                    else {
                        explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                        //                            //proceed with logic by disabling the related features or quit the app.
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.exampledemo.parsaniahardik.marshmallowpermission")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
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
                        if (InternetCheck.isInternetOn(AssessmentstaffActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

}