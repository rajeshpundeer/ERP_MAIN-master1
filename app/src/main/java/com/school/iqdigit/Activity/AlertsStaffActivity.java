package com.school.iqdigit.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.AlertsPreviewResponse;
import com.school.iqdigit.Model.AlertsResponse;
import com.school.iqdigit.Model.StaffAlert;
import com.school.iqdigit.Modeldata.Alerts;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.utility.InternetCheck;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertsStaffActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<StaffAlert> alertsList = new ArrayList<>();
    private ProgressDialog mProg;
    private ImageView backbtn;
    private LinearLayout noavailable;
    private ImageView imgAddAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts_staff);
        recyclerView = findViewById(R.id.alertsrecycleview);
        imgAddAlert = findViewById(R.id.imgAddAlert);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        mProg.show();
        noavailable = findViewById(R.id.noavailable);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgAddAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AlertsStaffActivity.this,Add_Alerts.class));
                finish();
            }
        });
        final Staff staff = SharedPrefManager2.getInstance(AlertsStaffActivity.this).getStaff();
        Call<AlertsPreviewResponse> call = RetrofitClient.getInstance().getApi().getalerts_staffResponse(staff.getId());
        call.enqueue(new Callback<AlertsPreviewResponse>() {
            @Override
            public void onResponse(Call<AlertsPreviewResponse> call, Response<AlertsPreviewResponse> response) {
                mProg.dismiss();
                if(InternetCheck.isInternetOn(AlertsStaffActivity.this) == true) {
                    if(response.body().getError() == false) {
                        alertsList = response.body().getStaffAlerts();
                        if (alertsList.size() > 0) {
                            noavailable.setVisibility(View.GONE);
                        } else {
                            noavailable.setVisibility(View.VISIBLE);
                        }
                        alertsadapter adaper = new alertsadapter(AlertsStaffActivity.this, alertsList);
                        recyclerView.setAdapter(adaper);
                    }else
                    {
                        noavailable.setVisibility(View.VISIBLE);
                    }
                }else {
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<AlertsPreviewResponse> call, Throwable t) {
                mProg.dismiss();
                if(InternetCheck.isInternetOn(AlertsStaffActivity.this) != true) {
                    showsnackbar();
                }
            }
        });
    }

    private class alertsadapter extends RecyclerView.Adapter<alertsadapter.alertsholder>{
        private Context mCtx;
        private List<StaffAlert> alertsList;

        public alertsadapter(Context mCtx, List<StaffAlert> alertsList) {
            this.mCtx = mCtx;
            this.alertsList = alertsList;
        }

        @NonNull
        @Override
        public alertsholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_alert_,viewGroup,false);
            return new alertsholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull alertsholder holder, int i) {
            StaffAlert alerts = alertsList.get(i);
            holder.title.setText(alerts.getMtitle());
            holder.date.setText(alerts.getCreateon());
            holder.message.setText(alerts.getMessage());

        }

        @Override
        public int getItemCount() {
            return alertsList.size();
        }

        class alertsholder extends RecyclerView.ViewHolder{
            TextView title,date,message;
            public alertsholder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.al_title);
                date = itemView.findViewById(R.id.al_createdon);
                message = itemView.findViewById(R.id.al_description);
            }
        }
    }

    private void showsnackbar(){
        if(mProg.isShowing())
        {
            mProg.dismiss();
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(getIntent());
                        finish();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
