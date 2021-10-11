package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.ExpandableListStaffAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.Chapters;
import com.school.iqdigit.Model.ClassResponse;
import com.school.iqdigit.Model.SubjectResponse;
import com.school.iqdigit.Model.Units;
import com.school.iqdigit.Modeldata.Classes;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.Subjects;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyStaff1Activity extends AppCompatActivity {
    private ProgressDialog mProg;
    private ImageView backbtn;
    private String TAG = "StudyMaterialActivity";
    private Spinner spSubject, spClass;
    private String _classid = "";
    private Staff staff;
    private String _subjectid = "";
    private LinearLayout llNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_staff1);
        staff = SharedPrefManager2.getInstance(StudyStaff1Activity.this).getStaff();
        spSubject = findViewById(R.id.spSubject);
        backbtn = findViewById(R.id.backbtn);
        spClass = findViewById(R.id.spClass);
        llNext = findViewById(R.id.llNext);
        mProg = new ProgressDialog(this);
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);
        mProg.show();
        if (InternetCheck.isInternetOn(StudyStaff1Activity.this) == true) {
            getClasses();
        } else {
            showsnackbar();
        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudyStaff1Activity.this,StudyMaterialStaff1Activity.class);
                intent.putExtra("classid",_classid);
                intent.putExtra("subjectid",_subjectid);
                intent.putExtra("classname",spClass.getSelectedItem().toString());
                intent.putExtra("subjectname",spSubject.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }

    private void getClasses() {
        final String[][] totalclassesid = new String[1][1];
        Call<ClassResponse> call = RetrofitClient.getInstance().getApi().getClasses_lms(staff.getId());
        call.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {
                List<Classes> classes;

                ClassResponse classResponse = response.body();

                classes = classResponse.getClasses();

                if (classes.size() > 0) {
                    //Creating an String array for the ListView
                    String[] totalclasses = new String[classes.size()];
                    totalclassesid[0] = new String[classes.size()];

                    //looping through all the heroes and inserting the names inside the string array
                    for (int i = 0; i < classes.size(); i++) {
                        totalclasses[i] = classes.get(i).getClass_name();
                        totalclassesid[0][i] = classes.get(i).getClass_id();
                    }

                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<String>(StudyStaff1Activity.this, android.R.layout.simple_list_item_1, totalclasses);
                    //setting adapter to spinner
                    spClass.setAdapter(adapter);
                    _classid = totalclassesid[0][0];
                    Log.d(TAG ,_classid +" classid");
                    getSubjects(_classid);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Add classes from ERP", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClassResponse> call, Throwable t) {

            }
        });
        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adapterView.getItemAtPosition(position);
                String classid = totalclassesid[0][position];
                _classid = classid;
                if (InternetCheck.isInternetOn(StudyStaff1Activity.this) == true) {
                    getSubjects(_classid);
                } else {
                    showsnackbar();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getSubjects(String classid) {
        final String[][] totalSubjectsid = new String[1][1];
        Call<SubjectResponse> call = RetrofitClient.getInstance().getApi().getSubjects(classid);
        call.enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                mProg.dismiss();
                if (response.body().isError() == false) {
                    List<Subjects> subjects;
                    SubjectResponse subjectResponse = response.body();
                    if(subjectResponse.getSubjects().size() > 0) {
                        subjects = subjectResponse.getSubjects();
                        Log.d(TAG, subjects + " subjects list");
                        //Creating an String array for the ListView
                        String[] totalSubjects = new String[subjects.size()];
                        totalSubjectsid[0] = new String[subjects.size()];

                        //looping through all the heroes and inserting the names inside the string array
                        for (int i = 0; i < subjects.size(); i++) {
                            Log.d(TAG, subjects.get(i).getSubject_name() + " subjects Name");
                            totalSubjects[i] = subjects.get(i).getSubject_name();
                            totalSubjectsid[0][i] = subjects.get(i).getSubject_id();
                        }

                        ArrayAdapter<String> adapter2;
                        adapter2 = new ArrayAdapter<String>(StudyStaff1Activity.this, android.R.layout.simple_list_item_1, totalSubjects);
                        //setting adapter to spinner
                         spSubject.setAdapter(adapter2);
                        _subjectid = totalSubjectsid[0][0];

                    }else {
                        ShowNotificationPop("Sorry!No Related Subjects Found");
                    }
                } else {
                    ShowNotificationPop("Sorry!No Related Subjects Found");
                }

            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sorry!No Related Subjects Found", Toast.LENGTH_LONG).show();
            }
        });
        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adapterView.getItemAtPosition(position);
                String subbjectid = totalSubjectsid[0][position];
                _subjectid = subbjectid;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                        if (InternetCheck.isInternetOn(StudyStaff1Activity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
    public void ShowNotificationPop(String txt){
        Dialog dialog;
        TextView textView;
        FloatingActionButton imgcancel;
        Button btnOk;
        dialog = new Dialog(StudyStaff1Activity.this);
        dialog.setContentView(R.layout.ic_common_text_popup);
        textView = dialog.findViewById(R.id.textmaintain);
        imgcancel = dialog.findViewById(R.id.img_cancel_dialog);
        btnOk = dialog.findViewById(R.id.btnOk);
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
            }
        });
        textView.setText(txt);
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
}
