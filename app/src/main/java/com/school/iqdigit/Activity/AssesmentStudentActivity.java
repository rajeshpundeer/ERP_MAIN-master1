package com.school.iqdigit.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.instacart.library.truetime.TrueTime;
import com.school.iqdigit.Adapter.EvaluatedAdapter;
import com.school.iqdigit.Adapter.PendingAdapter;
import com.school.iqdigit.Adapter.SubmittedAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.AssessmentResponse;
import com.school.iqdigit.Model.Assessment_list;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
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


public class AssesmentStudentActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgBack;
    private RecyclerView rvPending, rvSubmitted, rvEvaluated;
    private ProgressDialog mProg;
    private String TAG = "AssesmentStudentActivity";
    private String classid = "";
    User user = SharedPrefManager.getInstance(this).getUser();
    private List<Assessment_list> assessmentListPending = new ArrayList<>();
    private List<Assessment_list> assessmentListSubmitted = new ArrayList<>();
    private List<Assessment_list> assessmentListEvaluated = new ArrayList<>();
    private PendingAdapter pendingAdapter;
    private SubmittedAdapter submittedAdapter;
    private EvaluatedAdapter evaluatedAdapter;
    private TextView text_pending, text_submitted, text_checked, acti_title;
    private LinearLayout view_pending, view_submitted, view_checked,noavailable;
    private Date currentdatetime = new Date();
    private Timer timer;
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_student);
        checkAndRequestPermissions();
        /*String dateInString = "10-Jan-2016";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            currentdatetime = formatter.parse(dateInString);
            Log.d(TAG , currentdatetime + " currentdatetime");
        } catch (ParseException e) {
            //handle exception if date is not in "dd-MMM-yyyy" format
        }*/
        timer = new Timer();
        imgBack = findViewById(R.id.imgbackstudy);
        imgBack.setOnClickListener(this);
        rvPending = findViewById(R.id.rvPending);
        rvSubmitted = findViewById(R.id.rvSubmitted);
        rvEvaluated = findViewById(R.id.rvEvaluated);

        text_checked = findViewById(R.id.text_checked);
        text_submitted = findViewById(R.id.text_submitted);
        text_pending = findViewById(R.id.text_pending);
        view_pending = findViewById(R.id.view_pending);
        view_submitted = findViewById(R.id.view_submitted);
        view_checked = findViewById(R.id.view_checked);

        acti_title = findViewById(R.id.acti_title);
        noavailable = findViewById(R.id.noavailable);

        settopbar();
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        if (InternetCheck.isInternetOn(AssesmentStudentActivity.this) == true) {
            getclassid();
        } else {
            showsnackbar();
        }

    }

    private void settopbar() {
        text_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getevaluated();
                rvPending.setVisibility(View.GONE);
                rvSubmitted.setVisibility(View.GONE);
                rvEvaluated.setVisibility(View.VISIBLE);
                view_pending.setVisibility(View.GONE);
                view_submitted.setVisibility(View.GONE);
                view_checked.setVisibility(View.VISIBLE);

            }
        });
        text_submitted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getsubmitted();
                rvPending.setVisibility(View.GONE);
                rvSubmitted.setVisibility(View.VISIBLE);
                rvEvaluated.setVisibility(View.GONE);
                view_pending.setVisibility(View.GONE);
                view_submitted.setVisibility(View.VISIBLE);
                view_checked.setVisibility(View.GONE);

            }
        });
        text_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpending();
                rvPending.setVisibility(View.VISIBLE);
                rvSubmitted.setVisibility(View.GONE);
                rvEvaluated.setVisibility(View.GONE);
                view_pending.setVisibility(View.VISIBLE);
                view_submitted.setVisibility(View.GONE);
                view_checked.setVisibility(View.GONE);

            }
        });
    }

    private void getpending() {
        if (assessmentListPending.size() > 0)
            assessmentListPending.clear();
        if (InternetCheck.isInternetOn(AssesmentStudentActivity.this) == true) {
            mProg.show();
            Call<AssessmentResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentList(user.getId(), classid, "pending");
            call2.enqueue(new Callback<AssessmentResponse>() {
                @Override
                public void onResponse(Call<AssessmentResponse> call, Response<AssessmentResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            assessmentListPending = response.body().getAssessment_list();
                            Log.d(TAG, response.body().getAssessment_list() + " assessmentList");
                            if (assessmentListPending.size() > 0) {
                                noavailable.setVisibility(View.GONE);
                                Collections.reverse(assessmentListPending);
                                timer = new Timer();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                TimerTask timerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        (AssesmentStudentActivity.this).runOnUiThread(new Runnable() {
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
                                                    Log.d(TAG ,currentdate+ "currentdatetime");
                                                } catch (ParseException | IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if(currentdatetime != null) {
                                                    rvPending.setLayoutManager(new LinearLayoutManager(AssesmentStudentActivity.this));
                                                    pendingAdapter = new PendingAdapter(AssesmentStudentActivity.this, assessmentListPending, currentdatetime);
                                                    rvPending.setAdapter(pendingAdapter);
                                                    mProg.dismiss();
                                                }
                                            }
                                        });
                                    }
                                };
                                timer.scheduleAtFixedRate(timerTask, 0, 50000);
                            } else {
                                mProg.dismiss();
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Pending Assessment Available");                            }
                        }else {
                            mProg.dismiss();
                            noavailable.setVisibility(View.VISIBLE);
                            acti_title.setText("No Pending Assessment Available");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AssessmentResponse> call, Throwable t) {
                    mProg.dismiss();
                    if (t instanceof IOException) {
                        showsnackbar();
                    } else {
                        noavailable.setVisibility(View.VISIBLE);
                        acti_title.setText("No Pending Assessment Available");                    }
                }
            });
        } else {
            showsnackbar();
        }
    }

    private void getsubmitted() {
        timer.cancel();
        rvPending.setAdapter(null);
        if (assessmentListSubmitted.size() > 0)
            assessmentListSubmitted.clear();

        if (InternetCheck.isInternetOn(AssesmentStudentActivity.this) == true) {
            mProg.show();
            Call<AssessmentResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentList(user.getId(), classid, "submitted");
            //Call<AssessmentResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentList("3528", "21", "submitted");
            call2.enqueue(new Callback<AssessmentResponse>() {
                @Override
                public void onResponse(Call<AssessmentResponse> call, Response<AssessmentResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            assessmentListSubmitted = response.body().getAssessment_list();
                            Log.d(TAG, response.body().getAssessment_list() + " assessmentLissubmitted");
                            if (assessmentListSubmitted.size() > 0) {
                                noavailable.setVisibility(View.GONE);
                                Collections.reverse(assessmentListSubmitted);
                                rvSubmitted.setLayoutManager(new LinearLayoutManager(AssesmentStudentActivity.this));
                                submittedAdapter = new SubmittedAdapter(AssesmentStudentActivity.this, assessmentListSubmitted);
                                rvSubmitted.setAdapter(submittedAdapter);
                            } else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Submitted Assessment Available");                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AssessmentResponse> call, Throwable t) {
                    mProg.dismiss();
                    if (t instanceof IOException) {
                        Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    } else {
                        noavailable.setVisibility(View.VISIBLE);
                        acti_title.setText("No Submitted Assessment Available");                    }
                }
            });
        } else {
            showsnackbar();
        }
    }

    private void getevaluated() {
        timer.cancel();
        rvPending.setAdapter(null);
        if (InternetCheck.isInternetOn(AssesmentStudentActivity.this) == true) {
            mProg.show();
            if (assessmentListEvaluated.size() > 0)
                assessmentListEvaluated.clear();
            Call<AssessmentResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentList(user.getId(), classid, "checked");
            //Call<AssessmentResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentList("3528", "21", "checked");
            call2.enqueue(new Callback<AssessmentResponse>() {
                @Override
                public void onResponse(Call<AssessmentResponse> call, Response<AssessmentResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            assessmentListEvaluated = response.body().getAssessment_list();
                            Log.d(TAG, response.body().getAssessment_list() + " assessmentListchecked");
                            if (assessmentListEvaluated.size() > 0) {
                                noavailable.setVisibility(View.GONE);
                                Collections.reverse(assessmentListEvaluated);
                                rvEvaluated.setLayoutManager(new LinearLayoutManager(AssesmentStudentActivity.this));
                                evaluatedAdapter = new EvaluatedAdapter(AssesmentStudentActivity.this, assessmentListEvaluated);
                                rvEvaluated.setAdapter(evaluatedAdapter);
                            } else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Evaluated Assessment Available");                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AssessmentResponse> call, Throwable t) {
                    mProg.dismiss();
                    if (t instanceof IOException) {
                        Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    } else {
                        noavailable.setVisibility(View.VISIBLE);
                        acti_title.setText("No Evaluated Assessment Available");                    }
                }
            });
        } else {
            showsnackbar();
        }
    }

    private void getclassid() {
        User user = SharedPrefManager.getInstance(this).getUser();
        Call<ProfileResponse> call2 = RetrofitClient
                .getInstance().getApi().getUserProfile(user.getId());
        call2.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse profileResponse = response.body();
                Log.d(TAG, profileResponse.getUserprofile().getClassid() + " classid");
                classid = profileResponse.getUserprofile().getClassid();
                getpending();
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                if (mProg.isShowing()) {
                    mProg.dismiss();
                }
                if (t instanceof IOException) {
                    Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                //Toast.makeText(Examination.this, "Error :"+ t , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbackstudy: {
                onBackPressed();
            }
        }
    }

//
//    private void setTabview() {
//        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.navigation_pending:
//                        getpending();
//                        rvPending.setVisibility(View.VISIBLE);
//                        rvSubmitted.setVisibility(View.GONE);
//                        rvEvaluated.setVisibility(View.GONE);
//                        if (assessmentListPending.size() == 0) {
//                            Toast.makeText(getApplicationContext(), "No Pending Assessment Available", Toast.LENGTH_LONG).show();
//                        }
//                        return true;
//                    case R.id.navigation_submitted:
//                        getsubmitted();
//                        rvPending.setVisibility(View.GONE);
//                        rvSubmitted.setVisibility(View.VISIBLE);
//                        rvEvaluated.setVisibility(View.GONE);
//                        if (assessmentListSubmitted.size() == 0) {
//                            Toast.makeText(getApplicationContext(), "No Submitted Assessment Available", Toast.LENGTH_LONG).show();
//                        }
//                        return true;
//                    case R.id.navigation_evaluation:
//                        getevaluated();
//                        rvPending.setVisibility(View.GONE);
//                        rvSubmitted.setVisibility(View.GONE);
//                        rvEvaluated.setVisibility(View.VISIBLE);
//                        if (assessmentListEvaluated.size() == 0) {
//                            Toast.makeText(getApplicationContext(), "No Evaluated Assessment Available", Toast.LENGTH_LONG).show();
//                        }
//                        return true;
//                }
//                return true;
//            }
//        });
//    }
private  boolean checkAndRequestPermissions() {
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

        if(requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS){
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
    private void explain(String msg){
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
                        if (InternetCheck.isInternetOn(AssesmentStudentActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }


}
