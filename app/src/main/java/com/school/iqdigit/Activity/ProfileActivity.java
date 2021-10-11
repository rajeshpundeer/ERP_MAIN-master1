package com.school.iqdigit.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class ProfileActivity extends AppCompatActivity {
    private TextView student_name, studenr_class, student_addmno, student_rollno, status, addmdate, student_dob, student_gender, student_father, student_mother;
    private TextView student_mobile, student_caste, blood_group, student_address, aadhar_number, student_addmtype, result_status, pickup_point;
    private ImageView backbtn, imageView, imgAddPic;
    private ProgressDialog progressDialog;
    private boolean checkinternet;
    private String TAG = "ProfileActivity";
    private Intent intent;
    private String status_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Define Ids
        intent = getIntent();
        status_photo = intent.getStringExtra("photo_status");

        checkinternet = InternetCheck.isInternetOn(ProfileActivity.this);
        backbtn = findViewById(R.id.backbtn);
        student_name = findViewById(R.id.student_name);
        studenr_class = findViewById(R.id.student_class);
        student_addmno = findViewById(R.id.student_admno);
        student_rollno = findViewById(R.id.student_rollno);
        imgAddPic = findViewById(R.id.imgAddPic);
        if (status_photo.equals("1")) {
            imgAddPic.setVisibility(View.VISIBLE);
        } else {
            imgAddPic.setVisibility(View.GONE);
        }
        status = findViewById(R.id.status);
        addmdate = findViewById(R.id.addmdate);
        student_dob = findViewById(R.id.dob);
        student_gender = findViewById(R.id.gender);
        student_father = findViewById(R.id.father_sname);
        student_mother = findViewById(R.id.mother_sname);
        student_mobile = findViewById(R.id.mobile);
        student_caste = findViewById(R.id.caste);
        blood_group = findViewById(R.id.blood_group);
        student_address = findViewById(R.id.address);
        // student_aadhar = findViewById(R.id.aadhar_number);
        student_addmtype = findViewById(R.id.addm_type);
        aadhar_number = findViewById(R.id.aadhar_number);
        pickup_point = findViewById(R.id.pickup_point);
        imageView = findViewById(R.id.user_image);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.app_name_main);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.create();
        progressDialog.show();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = SharedPrefManager.getInstance(ProfileActivity.this).getUser();
                Intent intent = new Intent(ProfileActivity.this, IcardActivity.class);
                intent.putExtra("info", user.getId());
                intent.putExtra("type","student");
                startActivity(intent);
                finish();
            }
        });
        if (InternetCheck.isInternetOn(ProfileActivity.this) == true) {
            final User user = SharedPrefManager.getInstance(this).getUser();
            Call<ProfileResponse> call = RetrofitClient
                    .getInstance().getApi().getUserProfile(user.getId());
            call.enqueue(new Callback<ProfileResponse>() {
                @SuppressLint("ResourceType")
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                    ProfileResponse profileResponse = response.body();
                    Log.d(TAG, profileResponse.toString());
                    student_name.setText(profileResponse.getUserprofile().getName() + " " + profileResponse.getUserprofile().getStud_lname());
                    Log.d("ProfileActivity", profileResponse.getUserprofile().getClass_r());
                    studenr_class.setText("Class: " + profileResponse.getUserprofile().getClass_r());
                    student_addmno.setText(profileResponse.getUserprofile().getAdm_no());
                    student_rollno.setText(profileResponse.getUserprofile().getRoll_no());
                    status.setText(profileResponse.getUserprofile().getStud_status());
                    addmdate.setText(profileResponse.getUserprofile().getAdm_no());
                    student_gender.setText(profileResponse.getUserprofile().getGender());
                    student_father.setText(profileResponse.getUserprofile().getFather_name());
                    student_mother.setText(profileResponse.getUserprofile().getMother_name());
                    student_mobile.setText(profileResponse.getUserprofile().getPhonenumber());
                    student_caste.setText(profileResponse.getUserprofile().getCaste());
                    blood_group.setText(profileResponse.getUserprofile().getBloodgroup());
                    aadhar_number.setText(profileResponse.getUserprofile().getAadhar_no());
                    student_address.setText("India");
                    // student_aadhar.setText(profileResponse.getUserprofile().getAadhar_no());
                    student_dob.setText(profileResponse.getUserprofile().getDob());
                    student_addmtype.setText(profileResponse.getUserprofile().getAdm_type());
                    //  result_status.setText(profileResponse.getUserprofile().getResult_status());
                    pickup_point.setText(profileResponse.getUserprofile().getPickup_point());
                    final String imgeurl = profileResponse.getUserprofile().getPhoto();
                    Log.d(TAG, imgeurl);
                    Glide.with(ProfileActivity.this)
                            .load(imgeurl)
                            .placeholder(ContextCompat.getDrawable(ProfileActivity.this, R.drawable.user_icon))
                            .into(imageView);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Error :" + t, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                }
            });
        } else {
            showsnackbar();
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
                        if (InternetCheck.isInternetOn(ProfileActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
