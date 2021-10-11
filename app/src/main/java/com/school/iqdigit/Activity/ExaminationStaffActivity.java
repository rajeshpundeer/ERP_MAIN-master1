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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.ClassResponse;
import com.school.iqdigit.Model.ExamStaffResponse;
import com.school.iqdigit.Model.ExamsResponse;
import com.school.iqdigit.Model.GetexamlistMarksentry;
import com.school.iqdigit.Model.SubjectResponse;
import com.school.iqdigit.Modeldata.Classes;
import com.school.iqdigit.Modeldata.Exams;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.Subjects;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExaminationStaffActivity extends AppCompatActivity {
    private Spinner spClass;
    private Spinner spExam;
    private Spinner spSubject;
    private String _classid = "", _examid = "", _subjectid = "", _staffid = "";
    private ImageView next_btn,backbtn;
    private ProgressDialog progressDialog;
    private String TAG = "ExaminationStaffActivity";
    private LinearLayout llNext;
    boolean userSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_staff);
        spClass = findViewById(R.id.spClass);
        spExam = findViewById(R.id.spExam);
        spSubject = findViewById(R.id.spSubject);
        next_btn = findViewById(R.id.next_btn);
        backbtn = findViewById(R.id.backbtn);
        llNext = findViewById(R.id.llNext);
        final Staff staff = SharedPrefManager2.getInstance(ExaminationStaffActivity.this).getStaff();
        _staffid = staff.getId();
        progressDialog = new ProgressDialog(ExaminationStaffActivity.this);
        progressDialog.setTitle("Examination");
        progressDialog.setMessage("Wait a moment while \nExamination modules are Uploading....");
        if (InternetCheck.isInternetOn(ExaminationStaffActivity.this) == true) {
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
                if(spClass != null && spClass.getSelectedItem() !=null &&spSubject != null && spSubject.getSelectedItem() !=null &&spExam != null && spExam.getSelectedItem() !=null){
                    Intent intent = new Intent(ExaminationStaffActivity.this,ExamMarksStaffActivity.class);
                    intent.putExtra("class_id",_classid);
                    intent.putExtra("subject",spSubject.getSelectedItem().toString());
                    intent.putExtra("class_name",spClass.getSelectedItem().toString());
                    intent.putExtra("exam_id",_examid);
                    intent.putExtra("exam_name",spExam.getSelectedItem().toString());
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"You can not proceed further until Class,Subject and Exam type are not available",Toast.LENGTH_LONG).show();
                }
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
                adapter = new ArrayAdapter<String>(ExaminationStaffActivity.this, android.R.layout.simple_list_item_1, totalclasses);
                //setting adapter to spinner
                spClass.setAdapter(adapter);

                _classid = totalclassesid[0][0];
                Log.d(TAG ,_classid +" classId");
               // getSubjects(_classid);
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
                    if (InternetCheck.isInternetOn(ExaminationStaffActivity.this) == true) {
                        getSubjects(_classid);
                    } else {
                        showsnackbar();
                    }
                }else {
                    userSelect = true;
                    return;
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
                List<Subjects> subjects;
                SubjectResponse subjectResponse = response.body();
                subjects = subjectResponse.getSubjects();
                Log.d(TAG, subjects.size() + " subjectslist");
                //Creating an String array for the ListView
                String[] totalSubjects = new String[subjects.size()];

                if(subjectResponse.getSubjects().size() > 0) {
                    /*subjects = subjectResponse.getSubjects();
                    Log.d(TAG, subjects.size() + " subjectslist");
                    //Creating an String array for the ListView
                    String[] totalSubjects = new String[subjects.size()];*/
                    totalSubjectsid[0] = new String[subjects.size()];

                    //looping through all the heroes and inserting the names inside the string array
                    for (int i = 0; i < subjects.size(); i++) {
                        totalSubjects[i] = subjects.get(i).getSubject_name();
                        totalSubjectsid[0][i] = subjects.get(i).getSubject_id();
                    }
                    ArrayAdapter<String> adapter2;
                    adapter2 = new ArrayAdapter<String>(ExaminationStaffActivity.this, android.R.layout.simple_list_item_1, totalSubjects);
                    //setting adapter to spinner
                    spSubject.setAdapter(adapter2);
                    _subjectid = totalSubjectsid[0][0];
                    Log.d(TAG ,_subjectid +" subjectid");
                    getExams(_classid,_subjectid);
                }else {
                    ArrayAdapter<String> adapter2;
                    adapter2 = new ArrayAdapter<String>(ExaminationStaffActivity.this, android.R.layout.simple_list_item_1, totalSubjects);
                    //setting adapter to spinner
                    spSubject.setAdapter(adapter2);
                    ShowNotificationPop("Sorry! No related Subjects found.");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adapterView.getItemAtPosition(position);
                String subbjectid = totalSubjectsid[0][position];
                _subjectid = subbjectid;
                if (InternetCheck.isInternetOn(ExaminationStaffActivity.this) == true) {
                    //getUnits("8");
                    getExams(_classid,_subjectid);
                } else {
                    showsnackbar();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void getExams(String es_class,String es_subject) {
        progressDialog.show();
        final String[][] totalexamsid = new String[1][1];
       // Call<ExamStaffResponse> call = RetrofitClient.getInstance().getApi().getexamlist_marksentry("26","147");
        Call<ExamStaffResponse> call = RetrofitClient.getInstance().getApi().getexamlist_marksentry(es_class,es_subject);
        call.enqueue(new Callback<ExamStaffResponse>() {
            @Override
            public void onResponse(Call<ExamStaffResponse> call, Response<ExamStaffResponse> response) {
                List<GetexamlistMarksentry> examsList = new ArrayList<>();
                ExamStaffResponse examsResponse = response.body();
                examsList = examsResponse.getGetexamlistMarksentry();
                Log.d(TAG, examsList.size() + " Examlist");
                String[] totalexams = new String[examsList.size()];
                if(examsResponse.getGetexamlistMarksentry().size() > 0) {

                    totalexamsid[0] = new String[examsList.size()];
                    for (int i = 0; i < examsList.size(); i++) {
                        totalexams[i] = examsList.get(i).getExamNameDate();
                        totalexamsid[0][i] = examsList.get(i).getExamDetailsid();
                    }
                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<String>(ExaminationStaffActivity.this, android.R.layout.simple_list_item_1, totalexams);
                    spExam.setAdapter(adapter);
                    if (totalexams.length == 0) {
                        next_btn.setEnabled(false);
                    }
                    boolean notNull = false;
                    for (String[] array : totalexamsid) {
                        for (String val : array) {
                            if (val != null) {
                                _examid = totalexamsid[0][0];
                                break;
                            }
                        }
                    }
                }else {
                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<String>(ExaminationStaffActivity.this, android.R.layout.simple_list_item_1, totalexams);
                    spExam.setAdapter(adapter);
                    ShowNotificationPop("Sorry! No related Exam found.");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ExamStaffResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
        spExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String examid = totalexamsid[0][position];
                _examid = examid;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                        if( InternetCheck.isInternetOn(ExaminationStaffActivity.this) == true) {
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
        dialog = new Dialog(ExaminationStaffActivity.this);
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
