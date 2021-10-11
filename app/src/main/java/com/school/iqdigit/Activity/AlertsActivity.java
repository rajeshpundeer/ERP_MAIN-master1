package com.school.iqdigit.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.AlertsResponse;
import com.school.iqdigit.Modeldata.Alerts;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Alerts> alertsList;
    private ProgressDialog mProg;
    private ImageView backbtn;
    private LinearLayout noavailable;
    private boolean checkinternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        recyclerView = findViewById(R.id.alertsrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        mProg.show();
        checkinternet = InternetCheck.isInternetOn(AlertsActivity.this);
        noavailable = findViewById(R.id.noavailable);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        User user = SharedPrefManager.getInstance(this).getUser();
        Call<AlertsResponse> call = RetrofitClient.getInstance().getApi().getAlerts(user.getId());
        call.enqueue(new Callback<AlertsResponse>() {
            @Override
            public void onResponse(Call<AlertsResponse> call, Response<AlertsResponse> response) {
                mProg.dismiss();
                if(InternetCheck.isInternetOn(AlertsActivity.this) == true) {
                    alertsList = response.body().getAlerts();
                    if (alertsList.size() > 0) {
                        noavailable.setVisibility(View.GONE);
                    } else {
                        noavailable.setVisibility(View.VISIBLE);
                    }
                    alertsadapter adaper = new alertsadapter(AlertsActivity.this, alertsList);
                    recyclerView.setAdapter(adaper);
                }else {
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<AlertsResponse> call, Throwable t) {
                mProg.dismiss();
                if(InternetCheck.isInternetOn(AlertsActivity.this) != true) {
                    showsnackbar();
                }
            }
        });
    }

    private class alertsadapter extends RecyclerView.Adapter<alertsadapter.alertsholder>{
        private Context mCtx;
        private List<Alerts> alertsList;

        public alertsadapter(Context mCtx, List<Alerts> alertsList) {
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
            Alerts alerts = alertsList.get(i);
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
