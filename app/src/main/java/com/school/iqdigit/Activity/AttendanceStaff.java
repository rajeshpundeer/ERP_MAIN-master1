package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.ClassResponse;
import com.school.iqdigit.Model.SubjectResponse;
import com.school.iqdigit.Modeldata.Classes;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.Subjects;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceStaff extends AppCompatActivity implements View.OnClickListener {
    private Button choose_date;
    private NiceSpinner spinnerSubject;
    private Spinner spinnerClass;
    private ProgressDialog progressDialog;
    int year;
    int month;
    int day;
    private ImageView submit, backbtn;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private String _ldate = "", _cdate = "", _classid = "", _subjectid = "", _staffid = "";
    private String TAG = "AttendanceStaff";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_staff);
        submit = findViewById(R.id.next);
        backbtn = findViewById(R.id.backbtn);
        submit.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        spinnerClass = findViewById(R.id.spClass);
        spinnerSubject = (NiceSpinner) findViewById(R.id.spSubject);
        choose_date = findViewById(R.id.date_btn);
        choose_date.setOnClickListener(this);
        final Staff staff = SharedPrefManager2.getInstance(AttendanceStaff.this).getStaff();
        _staffid = staff.getId();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Attendance");
        progressDialog.setMessage("Wait a moment while \nattendance is Uploading....");
        progressDialog.create();
        getcurrentdate();
        if (InternetCheck.isInternetOn(AttendanceStaff.this) == true) {
            getClasses();
        }
        else {
            showsnackbar();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.next:
            {
                        if (!_ldate.equals(null) && !_cdate.equals(null)) {
                            if (!_classid.equals(null)) {
                                if (!_subjectid.equals(null)) {
                                    Intent intent = new Intent(AttendanceStaff.this , AttendanceStaff1.class);
                                    intent.putExtra("classid" ,_classid);
                                    intent.putExtra("subjectid",_subjectid);
                                    intent.putExtra("date",_ldate);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(AttendanceStaff.this, "Please select subject", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AttendanceStaff.this, "Please select class", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(AttendanceStaff.this, "Please select Date", Toast.LENGTH_SHORT).show();
                            }
                break;
            }
            case R.id.backbtn:
            {
                onBackPressed();
                break;
            }
            case R.id.date_btn:
            {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AttendanceStaff.this,
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
                                _ldate = yy + "-" + mm + "-" + dd;
                                _cdate = yy + "-" + mm + "-" + dd;
                                choose_date.setText(_ldate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            }
        }

    }

    private void getClasses() {
        final String[][] totalclassesid = new String[1][1];
        Call<ClassResponse> call = RetrofitClient.getInstance().getApi().getClasses(_staffid);
        call.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {
                List<Classes> classes;

                ClassResponse classResponse = response.body();

                classes = classResponse.getClasses();
                Log.d(TAG ,classes.size() +" classlist");
                //Creating an String array for the ListView
                String[] totalclasses = new String[classes.size()];
                totalclassesid[0] = new String[classes.size()];

                //looping through all the heroes and inserting the names inside the string array
                for (int i = 0; i < classes.size(); i++) {
                    totalclasses[i] = classes.get(i).getClass_name();
                    totalclassesid[0][i] = classes.get(i).getClass_id();
                }

                ArrayAdapter<String> adapter;
                adapter = new ArrayAdapter<String>(AttendanceStaff.this, android.R.layout.simple_list_item_1, totalclasses);
                //setting adapter to spinner
                spinnerClass.setAdapter(adapter);
                _classid = totalclassesid[0][0];
            }

            @Override
            public void onFailure(Call<ClassResponse> call, Throwable t) {

            }
        });
        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adapterView.getItemAtPosition(position);
                String classid = totalclassesid[0][position];
                _classid = classid;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getcurrentdate() {
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
        choose_date.setText(getdate);
        _ldate = getdate;
        _cdate = getdate;
    }

    private void showsnackbar(){
        if(progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if( InternetCheck.isInternetOn(AttendanceStaff.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
