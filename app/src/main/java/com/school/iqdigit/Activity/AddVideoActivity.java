package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.StudyAddResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVideoActivity extends AppCompatActivity {
    private EditText ed_title,ed_lesson,ed_youtube_link;
    private Intent mainintent;
    private String chapterid = "",subject_id="",chapter_name = "",action ="";
    private String title = "" ,description = "",running_id = "";
    private String TAG = "AddVideoActivity";
    private ProgressDialog progressDialog;
    private Button btnSubTutorial;
    private String _staffid = "";
    private ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        ed_title = findViewById(R.id.ed_book_title);
        ed_lesson = findViewById(R.id.ed_lesson);
        backbtn = findViewById(R.id.backbtn);
        btnSubTutorial = findViewById(R.id.btnSubTutorial);
        ed_youtube_link = findViewById(R.id.ed_youtube_link);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.app_name_main));
        progressDialog.setMessage("Loading....");
        progressDialog.create();
        mainintent = getIntent();
        final Staff staff = SharedPrefManager2.getInstance(AddVideoActivity.this).getStaff();
        _staffid = staff.getId();
        title = mainintent.getStringExtra("title");
        description = mainintent.getStringExtra("lesson");
        action = mainintent.getStringExtra("action");
        running_id = mainintent.getStringExtra("running_id");
        chapterid = mainintent.getStringExtra("chapterid");
        subject_id = mainintent.getStringExtra("subject_id");
        chapter_name = mainintent.getStringExtra("chapter_name");
        Log.d(TAG , "chapterid "+chapterid+"subjectid"+subject_id);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if(action.equals("edit"))
        {
            ed_title.setText(title);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                ed_lesson.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
                ed_lesson.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                ed_lesson.setText(Html.fromHtml(description));
                ed_lesson.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
        btnSubTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_title.getText().toString().trim().equalsIgnoreCase("")) {
                    if (!ed_lesson.getText().toString().trim().equalsIgnoreCase("")) {
                        if (!ed_youtube_link.getText().toString().trim().equalsIgnoreCase("")) {
                            if (InternetCheck.isInternetOn(AddVideoActivity.this) == true) {
                                if(action.equals("add")) {
                                    uploadBooklet();
                                }else if(action.equals("edit")){
                                    uploadEditedBooklet();
                                }
                            } else {
                                showsnackbar();
                            }
                        } else {
                            ed_lesson.setError("Please enter YouTube Link");
                        }
                    }else {
                        ed_lesson.setError("Please enter Book Description");
                    }
                } else {
                    ed_title.setError("Please enter Book Title");
                }
            }
        });
    }

    private void uploadEditedBooklet() {
        progressDialog.show();

        Log.d(TAG, "1" + subject_id + "2" + ed_title.getText().toString() + "4" + chapterid + "5 " + ed_lesson.getText().toString() + "6 " + _staffid);
        RequestBody a_book_name = RequestBody.create(MediaType.parse("text/plain"), ed_title.getText().toString());
        RequestBody a_chapter_id = RequestBody.create(MediaType.parse("text/plain"), chapterid);
        RequestBody a_subject_id = RequestBody.create(MediaType.parse("text/plain"), subject_id);
        RequestBody a_book_desc = RequestBody.create(MediaType.parse("text/plain"), ed_lesson.getText().toString());
        RequestBody a_status = RequestBody.create(MediaType.parse("text/plain"), "active");
        RequestBody a_user_type = RequestBody.create(MediaType.parse("text/plain"), "staff");
        RequestBody a_user_id = RequestBody.create(MediaType.parse("text/plain"), _staffid);
        RequestBody a_youtube_link = RequestBody.create(MediaType.parse("text/plain"), ed_youtube_link.getText().toString());
        RequestBody a_running_id = RequestBody.create(MediaType.parse("text/plain"), running_id);

        Call<StudyAddResponse> call = RetrofitClient.getInstance().getApi().edit_lms_video(a_subject_id,a_book_name,a_youtube_link,a_chapter_id, a_book_desc, a_status, a_user_type, a_user_id,a_running_id);
        call.enqueue(new Callback<StudyAddResponse>() {
            @Override
            public void onResponse(Call<StudyAddResponse> call, Response<StudyAddResponse> response) {
                StudyAddResponse defaultResponse = response.body();
                Log.d(TAG, " response: " + response);
                progressDialog.dismiss();
                if (defaultResponse.getError() == false) {
                    Intent intent = new Intent(AddVideoActivity.this,StudyMaterialStaffTutActivity.class);
                    intent.putExtra("chapterid",chapterid);
                    intent.putExtra("subject_id",subject_id);
                    intent.putExtra("chaptername",chapter_name);
                    intent.putExtra("type","video");
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), defaultResponse.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), defaultResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StudyAddResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, t.getMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadBooklet() {
        progressDialog.show();

        Log.d(TAG, "1" + subject_id + "2" + ed_title.getText().toString() + "4" + chapterid + "5 " + ed_lesson.getText().toString() + "6 " + _staffid);
        RequestBody a_book_name = RequestBody.create(MediaType.parse("text/plain"), ed_title.getText().toString());
        RequestBody a_chapter_id = RequestBody.create(MediaType.parse("text/plain"), chapterid);
        RequestBody a_subject_id = RequestBody.create(MediaType.parse("text/plain"), subject_id);
        RequestBody a_book_desc = RequestBody.create(MediaType.parse("text/plain"), ed_lesson.getText().toString());
        RequestBody a_status = RequestBody.create(MediaType.parse("text/plain"), "active");
        RequestBody a_user_type = RequestBody.create(MediaType.parse("text/plain"), "staff");
        RequestBody a_user_id = RequestBody.create(MediaType.parse("text/plain"), _staffid);
        RequestBody a_youtube_link = RequestBody.create(MediaType.parse("text/plain"), ed_youtube_link.getText().toString());

        Call<StudyAddResponse> call = RetrofitClient.getInstance().getApi().add_lms_video(a_subject_id,a_book_name,a_youtube_link,a_chapter_id, a_book_desc, a_status, a_user_type, a_user_id);
        call.enqueue(new Callback<StudyAddResponse>() {
            @Override
            public void onResponse(Call<StudyAddResponse> call, Response<StudyAddResponse> response) {
                StudyAddResponse defaultResponse = response.body();
                Log.d(TAG, " response: " + response);
                progressDialog.dismiss();
                if (defaultResponse.getError() == false) {
                    Intent intent = new Intent(AddVideoActivity.this,StudyMaterialStaffTutActivity.class);
                    intent.putExtra("chapterid",chapterid);
                    intent.putExtra("subject_id",subject_id);
                    intent.putExtra("chaptername",chapter_name);
                    intent.putExtra("type","video");
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), defaultResponse.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), defaultResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StudyAddResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, t.getMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
                        if (InternetCheck.isInternetOn(getApplicationContext()) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
