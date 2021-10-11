package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.ErrorResponse;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sun.bob.mcalendarview.MCalendarView;

public class LeaveActivity extends AppCompatActivity {
    private ViewPager viewPager;
   /* private EditText edReason;
    private ImageView imgBack;
    private Button btnLeavefrom, btnLeaveto, btnSubmittLeave;
    private ProgressDialog mProg;
    private Calendar calendar;
    private String f_date, e_date;
    int year;
    int month;
    int day;
    MCalendarView calendarView;
    private Date sdat, edat;
    private DatePickerDialog datePickerDialog;
    private Integer approval = 0;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        viewPager = findViewById(R.id.viewPager);
       /* edReason = findViewById(R.id.edReason);
        imgBack = findViewById(R.id.imgbackbtn);
        btnLeavefrom = findViewById(R.id.btnLeaveFrom);
        btnLeaveto = findViewById(R.id.btnLeaveTo);
        btnSubmittLeave = findViewById(R.id.btnSubmitLeave);
        btnLeaveto.setOnClickListener(this);
        btnLeavefrom.setOnClickListener(this);
        btnSubmittLeave.setOnClickListener(this);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");*/
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLeaveFrom:
            {
                calendar = Calendar.getInstance();
                sdat = calendar.getTime();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(LeaveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String yy = String.valueOf(year);
                                String dd = String.valueOf(dayOfMonth);
                                month = month + 1;
                                String mm = String.valueOf(month);
                                if (month < 10) {
                                    mm = "0" + mm;
                                }
                                if (dayOfMonth < 10) {
                                    dd = "0" + dd;
                                }
                                f_date = yy + "-" + mm + "-" + dd;
                                btnLeavefrom.setText(f_date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            }
            case R.id.btnLeaveTo:
            {
                calendar = Calendar.getInstance();
                edat = calendar.getTime();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(LeaveActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String yy = String.valueOf(year);
                                String dd = String.valueOf(dayOfMonth);
                                month = month + 1;
                                String mm = String.valueOf(month);
                                if (month < 10) {
                                    mm = "0" + mm;
                                }
                                if (dayOfMonth < 10) {
                                    dd = "0" + dd;
                                }
                                e_date = yy + "-" + mm + "-" + dd;
                                btnLeaveto.setText(e_date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            }
            case R.id.btnSubmitLeave: {
                if (!btnLeavefrom.getText().toString().trim().equalsIgnoreCase("From")) {
                    if (!btnLeaveto.getText().toString().trim().equalsIgnoreCase("To")) {
                        if (!edReason.getText().toString().trim().equalsIgnoreCase("")) {
                            final User user = SharedPrefManager.getInstance(this).getUser();

                            Call<ErrorResponse> call = RetrofitClient.getInstance().getApi().applyleave(user.getIncharge_id(), user.getP_class(), user.getId(),user.getName(),btnLeavefrom.getText().toString(), btnLeaveto.getText().toString(),edReason.getText().toString(), approval);
                            call.enqueue(new Callback<ErrorResponse>() {
                                @Override
                                public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                                    if (!response.body().isError()) {
                                        mProg.dismiss();
                                        Toast.makeText(LeaveActivity.this, "Applied For Leave Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mProg.dismiss();
                                        Toast.makeText(LeaveActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                }
                                }
                                @Override
                                public void onFailure(Call<ErrorResponse> call, Throwable t) {
                                    Toast.makeText(LeaveActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    mProg.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(LeaveActivity.this, "Please Enter Specified Reason For Leave", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LeaveActivity.this, "Please Enter Leave Applied To Date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LeaveActivity.this, "Please Enter Leave Applied From Date", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.imgbackbtn: {
                onBackPressed();
                break;
            }
        }
    }*/

}
