package com.school.iqdigit.Activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.AssessmentStaffAdapter;
import com.school.iqdigit.Adapter.IcardAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.ClassResponse;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.ExamStaffResponse;
import com.school.iqdigit.Model.GetexamlistMarksentry;
import com.school.iqdigit.Model.SubjectResponse;
import com.school.iqdigit.Modeldata.Classes;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.StudentsIcardResponse;
import com.school.iqdigit.Modeldata.StudentsPhoto;
import com.school.iqdigit.Modeldata.Subjects;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.interfaces.IcardStudentClicked;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IcardStaffActivity extends AppCompatActivity implements IcardStudentClicked {
    private Spinner spClass;
    private String _classid = "", _staffid = "";
    private ImageView backbtn;
    private ProgressDialog progressDialog;
    private String TAG = "IcardStaffActivity";
    boolean userSelect = false;
    private RecyclerView rvIcard;
    private IcardAdapter iCardAdapter;
    private Button btnUnLock,btnLock;
    private LinearLayout llNoStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icard_staff);
        spClass = findViewById(R.id.spClass);
        backbtn = findViewById(R.id.backbtn);
        rvIcard = findViewById(R.id.rvIcard);
        btnUnLock = findViewById(R.id.btnUnLock);
        btnLock = findViewById(R.id.btnLock);
        llNoStudents = findViewById(R.id.llNoStudents);
        final Staff staff = SharedPrefManager2.getInstance(IcardStaffActivity.this).getStaff();
        _staffid = staff.getId();
        progressDialog = new ProgressDialog(IcardStaffActivity.this);
        progressDialog.setTitle("Icard");
        progressDialog.setMessage("Wait a moment while \nIcard module is Uploading....");
        if (InternetCheck.isInternetOn(IcardStaffActivity.this) == true) {
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
        btnUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().photo_status_update_class("1", _classid);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body().isErr() == false) {
                            progressDialog.dismiss();
                            getStudentslist();
                            Toast.makeText(IcardStaffActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(IcardStaffActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            }
        });

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().photo_status_update_class("0", _classid);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body().isErr() == false) {
                            progressDialog.dismiss();
                            getStudentslist();
                            Toast.makeText(IcardStaffActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(IcardStaffActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
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
                Log.d(TAG, classes.size() + " classlist");
                //Creating an String array for the ListView
                String[] totalclasses = new String[classes.size()];
                totalclassesid[0] = new String[classes.size()];

                //looping through all the heroes and inserting the names inside the string array
                for (int i = 0; i < classes.size(); i++) {
                    totalclasses[i] = classes.get(i).getClass_name();
                    totalclassesid[0][i] = classes.get(i).getClass_id();
                }

                ArrayAdapter<String> adapter;
                adapter = new ArrayAdapter<String>(IcardStaffActivity.this, android.R.layout.simple_list_item_1, totalclasses);
                //setting adapter to spinner
                spClass.setAdapter(adapter);

                _classid = totalclassesid[0][0];
                getStudentslist();

                Log.d(TAG, _classid + " classId");
            }

            @Override
            public void onFailure(Call<ClassResponse> call, Throwable t) {

            }
        });

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (userSelect) {
                    String item = (String) adapterView.getItemAtPosition(position);
                    String classid = totalclassesid[0][position];
                    _classid = classid;
                    getStudentslist();
                } else {
                    userSelect = true;
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getStudentslist() {
        progressDialog.show();
        Call<StudentsIcardResponse> call = RetrofitClient.getInstance().getApi().getStudentsbyclass_photo(_classid);
        call.enqueue(new Callback<StudentsIcardResponse>() {
            @Override
            public void onResponse(Call<StudentsIcardResponse> call, Response<StudentsIcardResponse> response) {
                Log.d(TAG, response.body().error + " error" + response.body().getStudents_photo().size() + "size");
                if (response.body().isError() == false) {
                    progressDialog.dismiss();
                    List<StudentsPhoto> studentsPhotoList = response.body().getStudents_photo();
                    if (studentsPhotoList.size() == 0) {
                        llNoStudents.setVisibility(View.VISIBLE);
                    } else {
                        llNoStudents.setVisibility(View.GONE);
                    }
                    rvIcard.setLayoutManager(new LinearLayoutManager(IcardStaffActivity.this));
                    iCardAdapter = new IcardAdapter(IcardStaffActivity.this, studentsPhotoList,IcardStaffActivity.this);
                    rvIcard.setAdapter(iCardAdapter);
                } else {
                    progressDialog.dismiss();
                    llNoStudents.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<StudentsIcardResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void showsnackbar() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(IcardStaffActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void getId(String getId) {
        getStudentslist();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStudentslist();
    }
}
