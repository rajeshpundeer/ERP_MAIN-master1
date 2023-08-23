package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.GetStudentsbyexamlist;
import com.school.iqdigit.Model.MarksStaffResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamMarksStaffActivity extends AppCompatActivity {
    private Intent mainIntent;
    private String class_id = "", subject = "";
    private String class_name = "", exam_id = "",exam_name = "";
    private String TAG = "ExamMarksStaffActivity";
    private TextView tvTitle;
    private ProgressDialog progressDialog;
    private List<GetStudentsbyexamlist> marksList = new ArrayList<>();
    private ArrayList<String> marksList1 = new ArrayList<>();
    private ArrayList<String> studentsid = new ArrayList<>();
    private RecyclerView rvExamMarks;
    private UserAdapter adapter;
    private Button btnSubmitMarks,btnLock,btnExamName;
    private ImageView backbtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_marks_staff);
        tvTitle = findViewById(R.id.tvTitle);
        backbtn1 = findViewById(R.id.backbtn1);
        rvExamMarks = findViewById(R.id.rvExam);
        btnSubmitMarks = findViewById(R.id.btnSubmitMarks);
        btnExamName = findViewById(R.id.btnExamName);
        btnLock = findViewById(R.id.btnLock);
        mainIntent = getIntent();
        class_id = mainIntent.getStringExtra("class_id");
        subject = mainIntent.getStringExtra("subject");
        class_name = mainIntent.getStringExtra("class_name");
        exam_id = mainIntent.getStringExtra("exam_id");
        exam_name = mainIntent.getStringExtra("exam_name");
        btnExamName.setText(exam_name);
        tvTitle.setText(class_name + "[" + subject + "]");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Exam Marks");
        progressDialog.setMessage("Wait a moment...");
        progressDialog.create();
        backbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Log.d(TAG, class_id + " classid " + subject + " subjectid " + exam_id + " exam_id");
        if (InternetCheck.isInternetOn(ExamMarksStaffActivity.this) == true) {
            getMarks();
        } else {
            showsnackbar();
        }
        btnSubmitMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().marks_entered(exam_id,studentsid,marksList1);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body().isErr() == false) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                           // startActivity(new Intent(ExamMarksStaffActivity.this,ExaminationStaffActivity.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Marks Submittion Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNotificationPop("Marks will not be editable, once locked!  Are you sure you want to Lock the Marks?");
            }
        });
    }

    private void getMarks() {
        progressDialog.show();
       // Call<MarksStaffResponse> call = RetrofitClient.getInstance().getApi().getStudentsbyexamlist("35", "5133");
        Call<MarksStaffResponse> call = RetrofitClient.getInstance().getApi().getStudentsbyexamlist(class_id, exam_id);
        call.enqueue(new Callback<MarksStaffResponse>() {
            @Override
            public void onResponse(Call<MarksStaffResponse> call, Response<MarksStaffResponse> response) {
                if (!response.body().equals(null)) {
                    if (response.body().getError() == false) {
                        marksList = response.body().getGetStudentsbyexamlist();
                        if(marksList.get(0).getStatus().equals("locked")){
                            btnSubmitMarks.setVisibility(View.GONE);
                        }
                        for (int i= 0; i < marksList.size();i++) {
                            marksList1.add(marksList.get(i).getMarks());
                            studentsid.add(String.valueOf(marksList.get(i).getStuId()));
                        }
                        Log.d(TAG ,marksList1.size()+ "size of marks list");
                        rvExamMarks.setLayoutManager(new LinearLayoutManager(ExamMarksStaffActivity.this));
                        adapter = new UserAdapter(getApplicationContext(), marksList,marksList1,studentsid);
                        rvExamMarks.setAdapter(adapter);
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<MarksStaffResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    //exam marks entry adapter
    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder> {
        private Context mCtx;
        private List<GetStudentsbyexamlist> StudentsbyexamList;
        private List<String> marksList1 = new ArrayList<>();
        private List<String> studentsid = new ArrayList<>();

        public UserAdapter(Context mCtx, List<GetStudentsbyexamlist> StudentsbyexamList,List<String> marksList1,List<String> studentsid) {
            this.mCtx = mCtx;
            this.StudentsbyexamList = StudentsbyexamList;
            this.marksList1 = marksList1;
            this.studentsid = studentsid;
        }


        @NonNull
        @Override
        public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_exam_entry, viewGroup, false);
            return new UsersViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position) {
            Log.d(TAG , position +" position");
            final GetStudentsbyexamlist studentdetail = StudentsbyexamList.get(position);
            usersViewHolder.student_name.setText(studentdetail.getStuName());
            if (!studentdetail.getMarks().equals("")) {
                usersViewHolder.edMarks.setText(studentdetail.getMarks());
            }
            if (studentdetail.getStatus().equals("locked")) {
                usersViewHolder.edMarks.setEnabled(false);
            }
            usersViewHolder.edMarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    marksList1.set(usersViewHolder.getAdapterPosition() , charSequence.toString());
                    StudentsbyexamList.get(position).setMarks(usersViewHolder.edMarks.getText().toString());

                    Log.d(TAG , marksList1.size()+ " size");
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return StudentsbyexamList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        class UsersViewHolder extends RecyclerView.ViewHolder {
            private TextView student_name;
            private EditText edMarks;

            public UsersViewHolder(@NonNull View itemView) {
                super(itemView);
                student_name = itemView.findViewById(R.id.tvStudentName);
                edMarks = itemView.findViewById(R.id.edMarks);
            }
        }
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
                        if (InternetCheck.isInternetOn(getApplicationContext()) == true) {
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
        dialog = new Dialog(ExamMarksStaffActivity.this);
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
                progressDialog.show();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().lock_marks(exam_id);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body().isErr() == false) {
                            progressDialog.dismiss();
                            btnSubmitMarks.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ExamMarksStaffActivity.this,ExaminationStaffActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Marks Lock Failed", Toast.LENGTH_SHORT).show();
                    }
                });
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
