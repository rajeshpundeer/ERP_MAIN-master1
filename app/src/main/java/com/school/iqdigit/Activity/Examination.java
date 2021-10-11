package com.school.iqdigit.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.ExamsResponse;
import com.school.iqdigit.Model.MarksResponse;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Modeldata.Exams;
import com.school.iqdigit.Modeldata.Markslist;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class Examination extends AppCompatActivity {
    private Spinner spinner;
    private String _examid = "";
    private RecyclerView recyclerView,recycleexamGrade;
    private ImageView next_btn;
    private List<Markslist> markslistList;
    private TextView student_name, studenr_class, student_addmno, student_rollno, status, tvSchoolName,tvPercentage,tvRank;
    private ImageView backbtn, userimage;
    private Bitmap bitmap;
    private LinearLayout exammarkslist;
    private boolean checkinternet;
    private ProgressDialog mProg;
    private LinearLayout exammarkslistlayout,exammarksgradelayout;
    private String resulttype;
    private String TAG= "Examination";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        spinner = findViewById(R.id.exams_spinner);
        recyclerView = findViewById(R.id.recycleexammarks);
        recycleexamGrade = findViewById(R.id.recycleexamGrade);
        next_btn = findViewById(R.id.next_btn);
        backbtn = findViewById(R.id.backbtn);
        exammarkslistlayout = findViewById(R.id.exammarkslistlayout);
        exammarksgradelayout = findViewById(R.id.exammarksgradelayout);
        student_name = findViewById(R.id.student_name_e);
        studenr_class = findViewById(R.id.student_clas_e);
        student_addmno = findViewById(R.id.student_admn_e);
        student_rollno = findViewById(R.id.student_rolln_e);
        status = findViewById(R.id.status_e);
        userimage = findViewById(R.id.user_images_e);
        backbtn = findViewById(R.id.backbtn);
        tvSchoolName = findViewById(R.id.tvSchoolName);
        tvPercentage = findViewById(R.id.tvPercentage);
        exammarkslist = findViewById(R.id.exammarkslistlayout);
        tvRank = findViewById(R.id.tvRank);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycleexamGrade.setLayoutManager(new LinearLayoutManager(this));
        checkinternet = InternetCheck.isInternetOn(Examination.this);
        tvSchoolName.setText(R.string.app_name_main);
        mProg = new ProgressDialog(this);
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);
        mProg.show();
        if (checkinternet == true) {
            getExams();
        } else {
            showsnackbar();
        }
        final User user = SharedPrefManager.getInstance(this).getUser();
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProg.setMessage("Loading.....");
                mProg.setTitle(R.string.app_name_main);
                mProg.show();
                Call<MarksResponse> call = RetrofitClient.getInstance().getApi().getExamMarks(user.getId(), _examid, user.getP_class());
                call.enqueue(new Callback<MarksResponse>() {
                    @Override
                    public void onResponse(Call<MarksResponse> call, Response<MarksResponse> response) {
                        mProg.dismiss();
                        if (InternetCheck.isInternetOn(Examination.this) == true) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (!response.body().isError()) {
                                    markslistList = response.body().getExamsmlist();

                                    if(!response.body().getRank().equals(""))
                                    {
                                        tvRank.setVisibility(View.VISIBLE);
                                        tvRank.setText("Rank: "+response.body().getRank());
                                    }else {
                                        tvRank.setVisibility(View.GONE);
                                    }
                                    if (markslistList.size() > 0) {

                                        if(resulttype.equals("grades"))
                                        {
                                            exammarkslistlayout.setVisibility(View.GONE);
                                            tvPercentage.setVisibility(View.GONE);
                                            exammarksgradelayout.setVisibility(View.VISIBLE);
                                            exammarksadapter1 adapter1 = new exammarksadapter1(Examination.this, markslistList);
                                            recycleexamGrade.setAdapter(adapter1);
                                        }else if(resulttype.equals("marks"))
                                        {
                                            exammarksgradelayout.setVisibility(View.GONE);
                                            exammarkslistlayout.setVisibility(View.VISIBLE);
                                            if(response.body().getPercent() != null) {
                                                tvPercentage.setVisibility(View.VISIBLE);
                                                tvPercentage.setText("Percentage : " + response.body().getPercent());
                                            }
                                            exammarksadapter adapter = new exammarksadapter(Examination.this, markslistList);
                                            recyclerView.setAdapter(adapter);
                                        }

                                       // bitmap = loadBitmapFromView(exammarkslist, exammarkslist.getWidth(), exammarkslist.getHeight());
                                        //createPdf();
                                    } else {
                                        exammarkslistlayout.setVisibility(View.GONE);
                                        tvPercentage.setVisibility(View.GONE);
                                        Toast.makeText(Examination.this, "ExamMarks not available for this exam.", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    exammarkslistlayout.setVisibility(View.GONE);
                                    Toast.makeText(Examination.this, "" + response.body().isError(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                exammarkslistlayout.setVisibility(View.GONE);
                                Toast.makeText(Examination.this, "No Data Available", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showsnackbar();
                        }

                    }

                    @Override
                    public void onFailure(Call<MarksResponse> call, Throwable t) {
                        mProg.dismiss();
                        exammarkslistlayout.setVisibility(View.GONE);
                        if (InternetCheck.isInternetOn(Examination.this) != true) {
                            showsnackbar();
                        }
                    }
                });

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Get Data of User to show at Header of This page.
        Call<ProfileResponse> call2 = RetrofitClient
                .getInstance().getApi().getUserProfile(user.getId());
        call2.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(mProg.isShowing()){
                    mProg.dismiss();
                }
                mProg.dismiss();
                if (InternetCheck.isInternetOn(Examination.this) == true) {
                    ProfileResponse profileResponse = response.body();
                    student_name.setText(profileResponse.getUserprofile().getName() + " " + profileResponse.getUserprofile().getStud_lname());
                    studenr_class.setText("Class: " + profileResponse.getUserprofile().getClass_r());
                    student_addmno.setText("Adm. No.: " + profileResponse.getUserprofile().getAdm_no());
                    student_rollno.setText("Roll No.: " + profileResponse.getUserprofile().getRoll_no());
                    status.setText(profileResponse.getUserprofile().getStud_status());
                    final String imgeurl = BASE_URL2 + "office_admin/maintenance_image/student_photos/" + profileResponse.getUserprofile().getPhoto();
                    Glide.with(Examination.this).load(imgeurl)
                            .placeholder(ContextCompat.getDrawable(Examination.this, R.drawable.user_icon)).into(userimage);
                } else {
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                if(mProg.isShowing()){
                    mProg.dismiss();
                }
                mProg.dismiss();
                //Toast.makeText(Examination.this, "Error :"+ t , Toast.LENGTH_SHORT).show();
                if (InternetCheck.isInternetOn(Examination.this) != true) {
                    showsnackbar();
                }
            }
        });

    }


    private class exammarksadapter extends RecyclerView.Adapter<exammarksadapter.exammarksholder> {
        private Context mCtx;
        private List<Markslist> markslistList;

        public exammarksadapter(Context mCtx, List<Markslist> markslistList) {
            this.mCtx = mCtx;
            this.markslistList = markslistList;
        }

        @NonNull
        @Override
        public exammarksholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_marks_of_exams, viewGroup, false);
            return new exammarksholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull exammarksholder exammarksholder, int i) {
            Markslist markslist = markslistList.get(i);

                exammarksholder.exam_total_m.setText(markslist.getM_total());
                exammarksholder.exam_pass_m.setText(markslist.getM_pass());
                exammarksholder.exam_obtain_m.setText(markslist.getM_obtain());
                exammarksholder.exam_subject.setText(markslist.getSub_name());

        }

        @Override
        public int getItemCount() {
            return markslistList.size();
        }

        class exammarksholder extends RecyclerView.ViewHolder {
            TextView exam_subject, exam_total_m, exam_pass_m, exam_obtain_m, exam_status;

            public exammarksholder(@NonNull View itemView) {
                super(itemView);
                exam_subject = itemView.findViewById(R.id.exam_sub_name);
                exam_obtain_m = itemView.findViewById(R.id.exam_sub_marksobtained);
                exam_pass_m = itemView.findViewById(R.id.exam_sub_passmarks);
                exam_total_m = itemView.findViewById(R.id.exam_sub_totalmarks);
               // exam_status = itemView.findViewById(R.id.exam_sub_status);
            }
        }
    }

    private class exammarksadapter1 extends RecyclerView.Adapter<exammarksadapter1.exammarksholder1> {
        private Context mCtx;
        private List<Markslist> markslistList;

        public exammarksadapter1(Context mCtx, List<Markslist> markslistList) {
            this.mCtx = mCtx;
            this.markslistList = markslistList;
        }

        @NonNull
        @Override
        public exammarksholder1 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_grade_of_exams, viewGroup, false);
            return new exammarksholder1(view);
        }

        @Override
        public void onBindViewHolder(@NonNull exammarksholder1 exammarksholder, int i) {
            Markslist markslist = markslistList.get(i);
                exammarksholder.exam_total_m1.setText(markslist.getM_total());
                exammarksholder.exam_obtain_m1.setText(markslist.getM_obtain());
                exammarksholder.exam_subject1.setText(markslist.getSub_name());

        }

        @Override
        public int getItemCount() {
            return markslistList.size();
        }

        class exammarksholder1 extends RecyclerView.ViewHolder {
            TextView exam_subject1, exam_total_m1, exam_obtain_m1;

            public exammarksholder1(@NonNull View itemView) {
                super(itemView);
                exam_subject1 = itemView.findViewById(R.id.exam_sub_name11);
                exam_obtain_m1 = itemView.findViewById(R.id.exam_sub_marksobtained11);
                exam_total_m1 = itemView.findViewById(R.id.exam_sub_totalmarks11);
                // exam_status = itemView.findViewById(R.id.exam_sub_status);
            }
        }
    }
    private void getExams() {
        final String[][] totalexamsid = new String[1][1];
        User user = SharedPrefManager.getInstance(Examination.this).getUser();
        Call<ExamsResponse> call = RetrofitClient.getInstance().getApi().getExamList(user.getId());
        call.enqueue(new Callback<ExamsResponse>() {
            @Override
            public void onResponse(Call<ExamsResponse> call, Response<ExamsResponse> response) {
                mProg.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        List<Exams> examsList = new ArrayList<>();
                        ExamsResponse examsResponse = response.body();
                        examsList = examsResponse.getExamslist();
                        Log.d(TAG , examsList.size()+" examlist size");
                        if (examsList.size() > 0) {
                            String[] totalexams = new String[examsList.size()];
                            resulttype = examsResponse.getResulttype();
                            Log.d(TAG, resulttype + " resulttype");
                            totalexamsid[0] = new String[examsList.size()];
                            for (int i = 0; i < examsList.size(); i++) {
                                totalexams[i] = examsList.get(i).getExamname();
                                totalexamsid[0][i] = examsList.get(i).getExamid();
                            }
                            ArrayAdapter<String> adapter;
                            adapter = new ArrayAdapter<String>(Examination.this, android.R.layout.simple_list_item_1, totalexams);
                            spinner.setAdapter(adapter);
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
                            Toast.makeText(Examination.this, "No Exam Result Available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ExamsResponse> call, Throwable t) {
                mProg.dismiss();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String examid = totalexamsid[0][position];
                _examid = examid;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showsnackbar() {
        if(mProg.isShowing()){
            mProg.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(Examination.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }


}
