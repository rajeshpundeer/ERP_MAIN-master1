package com.school.iqdigit.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.EvaluatedStaffAdapter;
import com.school.iqdigit.Adapter.PendingStaffAdapter;
import com.school.iqdigit.Adapter.SubmittedStaffAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.AssessmentStaffListResponse;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.GetAssessmentListRoot;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.interfaces.RemoveSubmittedAssessment;
import com.school.iqdigit.utility.InternetCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AssesmentStaffListActivity extends AppCompatActivity implements View.OnClickListener, RemoveSubmittedAssessment {
    private ImageView imgBack;
    private BottomNavigationView navigationView;
    private RecyclerView rvPending, rvSubmitted, rvEvaluated;
    private ProgressDialog mProg;
    private String TAG = "AssesmentStaff";
    private List<GetAssessmentListRoot> assessmentListPending = new ArrayList<>();
    private List<GetAssessmentListRoot> assessmentListSubmitted = new ArrayList<>();
    private List<GetAssessmentListRoot> assessmentListEvaluated = new ArrayList<>();
    private PendingStaffAdapter pendingAdapter;
    private SubmittedStaffAdapter submittedAdapter;
    private EvaluatedStaffAdapter evaluatedAdapter;
    private String _staffid = "";
    private Intent mainintent;
    private String hw_id = "",pendingcount = "",submittedcount = "";
    private String assessment_type = "",type = "pending";
    private TextView text_pending, text_submitted, text_checked;
    private TextView tvClassName, al_assessment_type, al_title, acti_title;
    private ImageView tvViewHomeWork;
    private LinearLayout view_pending, view_submitted, view_checked,noavailable;
    private Button btnPublishResult;
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_staff_new);
        checkAndRequestPermissions();
        imgBack = findViewById(R.id.imgbackstudy);
        imgBack.setOnClickListener(this);
        rvPending = findViewById(R.id.rvPending);
        rvSubmitted = findViewById(R.id.rvSubmitted);
        rvEvaluated = findViewById(R.id.rvEvaluated);
        btnPublishResult = findViewById(R.id.btnPublishResult);
        noavailable = findViewById(R.id.noavailable);

        tvClassName = findViewById(R.id.tvClassName);
        al_assessment_type = findViewById(R.id.al_assessment_type);
        al_title = findViewById(R.id.al_title);
        tvViewHomeWork = findViewById(R.id.tvViewHomeWork);
        acti_title = findViewById(R.id.acti_title);

        text_checked = findViewById(R.id.text_checked);
        text_submitted = findViewById(R.id.text_submitted);
        text_pending = findViewById(R.id.text_pending);
        view_pending = findViewById(R.id.view_pending);
        view_submitted = findViewById(R.id.view_submitted);
        view_checked = findViewById(R.id.view_checked);

        final Staff staff = SharedPrefManager2.getInstance(AssesmentStaffListActivity.this).getStaff();
        _staffid = staff.getId();
        mainintent = getIntent();
        type = mainintent.getStringExtra("type");
        hw_id = mainintent.getStringExtra("hw_id");
        pendingcount = mainintent.getStringExtra("pendingcount");
        submittedcount = mainintent.getStringExtra("submittedcount");
        assessment_type = mainintent.getStringExtra("assessment_type");
        Log.d(TAG, _staffid + " staffid " + hw_id + " hw_id");
        settopbar();
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        if (!InternetCheck.isInternetOn(AssesmentStaffListActivity.this)) {
            showsnackbar();
            return;
        }
        tvClassName.setText(mainintent.getStringExtra("a_class"));
        al_title.setText(mainintent.getStringExtra("a_title"));
        al_assessment_type.setText(mainintent.getStringExtra("a_type"));


        if (type.equals("pending")) {
            getpending();
            rvPending.setVisibility(View.VISIBLE);
            rvSubmitted.setVisibility(View.GONE);
            rvEvaluated.setVisibility(View.GONE);
            view_pending.setVisibility(View.VISIBLE);
            view_submitted.setVisibility(View.GONE);
            view_checked.setVisibility(View.GONE);
        } else if (type.equals("submitted")) {
            getsubmitted();
            rvPending.setVisibility(View.GONE);
            rvSubmitted.setVisibility(View.VISIBLE);
            rvEvaluated.setVisibility(View.GONE);
            view_pending.setVisibility(View.GONE);
            view_submitted.setVisibility(View.VISIBLE);
            view_checked.setVisibility(View.GONE);

        } else if (type.equals("checked")) {
            getevaluated();
            rvPending.setVisibility(View.GONE);
            rvSubmitted.setVisibility(View.GONE);
            rvEvaluated.setVisibility(View.VISIBLE);
            view_pending.setVisibility(View.GONE);
            view_submitted.setVisibility(View.GONE);
            view_checked.setVisibility(View.VISIBLE);
        }

        tvViewHomeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssesmentStaffListActivity.this, imageActivity1.class);
                intent.putExtra("imgurl", mainintent.getStringExtra("imgurl"));
                startActivity(intent);
            }
        });
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
                btnPublishResult.setVisibility(View.GONE);

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
                btnPublishResult.setVisibility(View.GONE);

            }
        });

        btnPublishResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                TextView textView;
                FloatingActionButton imgcancel;
                Button btnOk,btnCancel;
                dialog = new Dialog(AssesmentStaffListActivity.this);
                dialog.setContentView(R.layout.ic_common_text_popup1);
                textView = dialog.findViewById(R.id.textmaintain);
                imgcancel = dialog.findViewById(R.id.img_cancel_dialog);
                btnCancel = dialog.findViewById(R.id.btnCancel);
                btnOk = dialog.findViewById(R.id.btnOk);
                textView.setText( "Pending MCQ Ans. Sheets - " +pendingcount+" & "+"Submitted MCQ Ans. Sheets - "
                        +submittedcount
                        +". Are you sure to Publish this result?");
                imgcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        mProg.show();
                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().setpublish_result_mcq(String.valueOf(hw_id));
                        // Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().checkassessment(finalScore, edRemarks.getText().toString(), "105");
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                mProg.dismiss();
                                if (response.body() != null) {
                                    if (response.body().isErr() == false) {
                                        Toast.makeText(getApplicationContext(), "Result Published, Successfully!", Toast.LENGTH_SHORT).show();
                                        getsubmitted();
                                        rvPending.setVisibility(View.GONE);
                                        rvSubmitted.setVisibility(View.VISIBLE);
                                        rvEvaluated.setVisibility(View.GONE);
                                        view_pending.setVisibility(View.GONE);
                                        view_submitted.setVisibility(View.VISIBLE);
                                        view_checked.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Result Publish , Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                mProg.dismiss();
                                //Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.closeOptionsMenu();
                dialog.setCancelable(true);
                dialog.show();
                new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        onBackPressed();
                    }
                };


            }
        });
    }

    private void getpending() {
        if (InternetCheck.isInternetOn(AssesmentStaffListActivity.this) == true) {
            mProg.show();
            Call<AssessmentStaffListResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentstaffList(_staffid, hw_id, "pending");
            // Call<AssessmentStaffListResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentstaffList("2", "117", "pending");
            call2.enqueue(new Callback<AssessmentStaffListResponse>() {
                @Override
                public void onResponse(Call<AssessmentStaffListResponse> call, Response<AssessmentStaffListResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            assessmentListPending.clear();
                            assessmentListPending = response.body().getGetAssessmentListRoot();
                            Log.d(TAG, response.body().getGetAssessmentListRoot() + " assessmentstaffList");
                            if (assessmentListPending.size() > 0) {
                                noavailable.setVisibility(View.GONE);
                                Collections.reverse(assessmentListPending);
                                rvPending.setLayoutManager(new LinearLayoutManager(AssesmentStaffListActivity.this));
                                pendingAdapter = new PendingStaffAdapter(AssesmentStaffListActivity.this, assessmentListPending);
                                rvPending.setAdapter(pendingAdapter);
                            } else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Pending Assessment Available");
                              //  Toast.makeText(getApplicationContext(), "No Pending Assessment Available", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AssessmentStaffListResponse> call, Throwable t) {
                    mProg.dismiss();
                    if (t instanceof IOException) {
                        showsnackbar();
                        // logging probably not necessary
                    } else {
                        noavailable.setVisibility(View.VISIBLE);
                        acti_title.setText("No Pending Assessment Available");}
                }
            });
        } else {
            showsnackbar();
        }
    }

    private void getsubmitted() {
        if (InternetCheck.isInternetOn(AssesmentStaffListActivity.this) == true) {
            mProg.show();
            // Call<AssessmentStaffListResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentstaffList("2","117", "submitted");
            Call<AssessmentStaffListResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentstaffList(_staffid, hw_id, "submitted");
            call2.enqueue(new Callback<AssessmentStaffListResponse>() {
                @Override
                public void onResponse(Call<AssessmentStaffListResponse> call, Response<AssessmentStaffListResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            assessmentListSubmitted.clear();
                            assessmentListSubmitted = response.body().getGetAssessmentListRoot();
                            Log.d(TAG, response.body().getGetAssessmentListRoot() + " assessmentLissubmitted");
                            if(assessmentListSubmitted.size()>0) {
                                noavailable.setVisibility(View.GONE);
                                if (assessmentListSubmitted.size() > 0 && assessment_type.equals("2")) {
                                    btnPublishResult.setVisibility(View.VISIBLE);
                                } else {
                                    btnPublishResult.setVisibility(View.GONE);
                                }
                                Collections.reverse(assessmentListSubmitted);
                                rvSubmitted.setLayoutManager(new LinearLayoutManager(AssesmentStaffListActivity.this));
                                submittedAdapter = new SubmittedStaffAdapter(AssesmentStaffListActivity.this, assessmentListSubmitted, AssesmentStaffListActivity.this);
                                rvSubmitted.setAdapter(submittedAdapter);
                            }else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Submitted Assessment Available");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AssessmentStaffListResponse> call, Throwable t) {
                    mProg.dismiss();
                    if (t instanceof IOException) {
                        showsnackbar();
                        // logging probably not necessary
                    } else {
                        noavailable.setVisibility(View.VISIBLE);
                        acti_title.setText("No Submitted Assessment Available");
                    }
                }
            });
        } else {
            showsnackbar();
        }
    }

    private void getevaluated() {
        if (InternetCheck.isInternetOn(AssesmentStaffListActivity.this) == true) {
            mProg.show();
            //Call<AssessmentStaffListResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentstaffList("2","117", "checked");
            Call<AssessmentStaffListResponse> call2 = RetrofitClient.getInstance().getApi().getAssessmentstaffList(_staffid, hw_id, "checked");
            call2.enqueue(new Callback<AssessmentStaffListResponse>() {
                @Override
                public void onResponse(Call<AssessmentStaffListResponse> call, Response<AssessmentStaffListResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getError() == false) {
                            mProg.dismiss();
                            assessmentListEvaluated.clear();
                            assessmentListEvaluated = response.body().getGetAssessmentListRoot();
                            Log.d(TAG, response.body().getGetAssessmentListRoot() + " assessmentListchecked");
                            if (assessmentListEvaluated.size() > 0) {
                                noavailable.setVisibility(View.GONE);
                                Collections.reverse(assessmentListEvaluated);
                                rvEvaluated.setLayoutManager(new LinearLayoutManager(AssesmentStaffListActivity.this));
                                evaluatedAdapter = new EvaluatedStaffAdapter(AssesmentStaffListActivity.this, assessmentListEvaluated);
                                rvEvaluated.setAdapter(evaluatedAdapter);
                            } else {
                                noavailable.setVisibility(View.VISIBLE);
                                acti_title.setText("No Evaluated Assessment Available");}
                        }
                    }
                }

                @Override
                public void onFailure(Call<AssessmentStaffListResponse> call, Throwable t) {
                    mProg.dismiss();
                    if (t instanceof IOException) {
                        showsnackbar();
                        // logging probably not necessary
                    } else {
                        noavailable.setVisibility(View.VISIBLE);
                        acti_title.setText("No Evaluated Assessment Available");}
                }
            });
        } else {
            showsnackbar();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbackstudy: {
                startActivity(new Intent(AssesmentStaffListActivity.this, AssessmentstaffActivity.class));
                finish();
            }
        }
    }
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
                        if (InternetCheck.isInternetOn(AssesmentStaffListActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void deleteCheckedAsessment(int deleteCheckedAssessmentPosition) {
        assessmentListSubmitted.remove(deleteCheckedAssessmentPosition);
        submittedAdapter.notifyDataSetChanged();
    }
}
