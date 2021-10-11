package com.school.iqdigit.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.IcardAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.StaffProfileResponse;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.StudentsIcardResponse;
import com.school.iqdigit.Modeldata.StudentsPhoto;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class ProfileActivityStaff extends AppCompatActivity {
    private TextView name, post, status, joindate, dob, gender, fathername, mobile, address,  depname;
    private ImageView imageView, backbtn, imgAddPic;
    private ProgressDialog progressDialog;
    private boolean checkinternet;
    private String TAG = "ProfileActivityStaff";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_staff);
        backbtn = findViewById(R.id.backbtn);
        name = findViewById(R.id.teacher_name);
        post = findViewById(R.id.teacher_admno);
        status = findViewById(R.id.teacher_status);
        imgAddPic = findViewById(R.id.imgAddPic);
        joindate = findViewById(R.id.teacher_addmdate);
        dob = findViewById(R.id.teacher_dob);
        gender = findViewById(R.id.teacher_gender);
        fathername = findViewById(R.id.teacher_father_sname);
        mobile = findViewById(R.id.teacher_mobile);
        address = findViewById(R.id.teacher_address);
        //salery = findViewById(R.id.teacher_addm_type);
        depname = findViewById(R.id.deapartmentname);
        imageView = findViewById(R.id.staff_user_image);
        checkinternet = InternetCheck.isInternetOn(ProfileActivityStaff.this);
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
                final Staff staff = SharedPrefManager2.getInstance(ProfileActivityStaff.this).getStaff();
                String _staffid = staff.getId();
                Intent intent = new Intent(ProfileActivityStaff.this, IcardActivity.class);
                intent.putExtra("info", _staffid);
                intent.putExtra("type","staff");
                startActivity(intent);
                finish();
            }
        });
        getstaffprofile();
    }


    private void showsnackbar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(ProfileActivityStaff.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    public void getstaffprofile(){
        progressDialog.show();
        Staff staff = SharedPrefManager2.getInstance(this).getStaff();
        Call<StaffProfileResponse> call = RetrofitClient.getInstance().getApi().getgetstaffprofile(staff.getId());
        call.enqueue(new Callback<StaffProfileResponse>() {
            @Override
            public void onResponse(Call<StaffProfileResponse> call, Response<StaffProfileResponse> response) {
                progressDialog.dismiss();
                if (response.body().isError() == false) {
                    name.setText(response.body().getStaffprofile().getSt_firstname()+response.body().getStaffprofile().getSt_lastname());
                    post.setText("POST : " + staff.getPostname());
                    if (staff.getStatus().equals("added")) {
                        status.setText("active");
                        status.setTextColor(Color.GREEN);
                    } else {
                        status.setText("Inactive");
                        status.setTextColor(Color.RED);
                    }
                    joindate.setText(staff.getDojoining());
                    dob.setText(response.body().getStaffprofile().getSt_dob());
                    gender.setText(response.body().getStaffprofile().getSt_gender());
                    fathername.setText(response.body().getStaffprofile().getSt_fthname());
                    mobile.setText(response.body().getStaffprofile().getSt_pemobileno());
                    address.setText(response.body().getStaffprofile().getSt_pradress());
                    // salery.setText(staff.getSalery());
                    depname.setText(staff.getDepname());
                    progressDialog.dismiss();

                    final String imgeurl = staff.getImgpath();
                    Log.d(TAG , imgeurl+" url");
                    // final String imgeurl = BASE_URL2 + "office_admin/maintenance_image/staff/" + staff.getUser_type();
                    Glide.with(ProfileActivityStaff.this).load(response.body().getStaffprofile().getImage()).into(imageView);
                }
            }

            @Override
            public void onFailure(Call<StaffProfileResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}


