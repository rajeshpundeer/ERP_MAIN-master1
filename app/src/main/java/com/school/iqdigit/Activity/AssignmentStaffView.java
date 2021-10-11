package com.school.iqdigit.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.AssSubAdapter;
import com.school.iqdigit.Adapter.AssSubStaffAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.AssSubResponse;
import com.school.iqdigit.Model.AssignmentPreviewResponse;
import com.school.iqdigit.Model.StaffAssignment;
import com.school.iqdigit.Modeldata.AssSub;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignmentStaffView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AssSubStaffAdapter adapter;
    private List<StaffAssignment> assSubList = new ArrayList<>();
    private Button date_btn;
    private ImageView next, backbtn;
    private String getdate;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private LinearLayout noassignment;
    private ImageView imgAddAssessment;
    int year;
    int month;
    int day;
    private String _staffid;
    private String TAG = "AssignmentStaffView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);
        noassignment = findViewById(R.id.nohomework);
        date_btn = findViewById(R.id.date_btn);
        next = findViewById(R.id.next);
        backbtn = findViewById(R.id.backbtn);
        imgAddAssessment = findViewById(R.id.imgAddAssessment);
        recyclerView = findViewById(R.id.asssubrecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getcurrentattent();
        imgAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AssignmentStaffView.this,AssignmentStaff.class));
                finish();
            }
        });
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AssignmentStaffView.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String yy = String.valueOf(year);  //2018+"-"
                                String dd = String.valueOf(dayOfMonth);
                                month = month + 1;
                                String mm = String.valueOf(month);
                                if (month < 10) {
                                    mm = "0" + mm;
                                }
                                if (dayOfMonth < 10) {
                                    dd = "0" + dd;
                                }
                                getdate = yy + "-" + mm + "-" + dd;
                                date_btn.setText(getdate);
                                next.setVisibility(View.VISIBLE);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSubjects();
            }
        });
        getSubjects();
    }

    private void getcurrentattent() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String yy = String.valueOf(year);
        String dd = String.valueOf(day);
        month = month + 1;
        String mm = String.valueOf(month);
        if (month < 10) {
            mm = "0" + mm;
        }
        if (day < 10) {
            dd = "0" + dd;
        }
        String getdate = yy + "-" + mm + "-" + dd;
        date_btn.setText(getdate);
    }

    public void getSubjects() {
        if (InternetCheck.isInternetOn(AssignmentStaffView.this) == true) {
            final Staff staff = SharedPrefManager2.getInstance(AssignmentStaffView.this).getStaff();
            _staffid = staff.getId();
            Log.d(TAG , _staffid+ " "+ date_btn.getText().toString());
            Call<AssignmentPreviewResponse> call = RetrofitClient
                    .getInstance().getApi().getassignments_staffResponse(_staffid, date_btn.getText().toString());
            call.enqueue(new Callback<AssignmentPreviewResponse>() {
                @Override
                public void onResponse(Call<AssignmentPreviewResponse> call, Response<AssignmentPreviewResponse> response) {

                    if(response.body().getError() == false) {
                        assSubList = response.body().getStaffAssignment();
                        if (assSubList.size() > 0) {
                            noassignment.setVisibility(View.GONE);
                        } else {
                            noassignment.setVisibility(View.VISIBLE);
                        }
                        adapter = new AssSubStaffAdapter(AssignmentStaffView.this, assSubList);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<AssignmentPreviewResponse> call, Throwable t) {

                }
            });
        } else {
            showsnackbar();
        }

    }

    private void showsnackbar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(AssignmentStaffView.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
