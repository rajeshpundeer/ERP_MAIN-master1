package com.school.iqdigit.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.ActivitiesResponse;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activities extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<com.school.iqdigit.Modeldata.Activities> activitiesList;
    private ProgressDialog mProg;
    private ImageView backbtn;
    private LinearLayout noavailable;
    private boolean checkinternet;

    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        recyclerView = findViewById(R.id.activitiesrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        mProg.show();
        checkinternet = InternetCheck.isInternetOn(Activities.this);
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        noavailable = findViewById(R.id.noavailable);
        User user = SharedPrefManager.getInstance(this).getUser();
        if (InternetCheck.isInternetOn(Activities.this) == true) {
            Call<ActivitiesResponse> call = RetrofitClient.getInstance().getApi().getActivities(user.getP_class());
            call.enqueue(new Callback<ActivitiesResponse>() {
                @Override
                public void onResponse(Call<ActivitiesResponse> call, Response<ActivitiesResponse> response) {
                    mProg.dismiss();

                    if (!response.body().isError()) {
                        activitiesList = response.body().getUser_activities();
                        if (activitiesList.size() > 0) {
                            noavailable.setVisibility(View.GONE);
                        } else {
                            noavailable.setVisibility(View.VISIBLE);
                        }
                        activitiesadapter adapter = new activitiesadapter(Activities.this, activitiesList);
                        recyclerView.setAdapter(adapter);
                    }
                }
                @Override
                public void onFailure(Call<ActivitiesResponse> call, Throwable t) {
                    mProg.dismiss();
                    showsnackbar();
                    Log.v("Activities", "" + t);
                }
            });
        } else {
            showsnackbar();
        }
    }
    private class activitiesadapter extends RecyclerView.Adapter<activitiesadapter.activitiesholder>{
        private Context mCtx;
        private List<com.school.iqdigit.Modeldata.Activities> activitiesList;

        public activitiesadapter(Context mCtx, List<com.school.iqdigit.Modeldata.Activities> activitiesList) {
            this.mCtx = mCtx;
            this.activitiesList = activitiesList;
        }
        @NonNull
        @Override
        public activitiesholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_all_activities,viewGroup,false);
            return new activitiesholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull activitiesholder holder, int i) {
            final com.school.iqdigit.Modeldata.Activities activities = activitiesList.get(i);
            holder.title.setText(activities.getTitle());
            holder.description.setText(activities.getDescription());
            holder.createdon.setText(activities.getCreateon());
            holder.upto.setText(activities.getUpto());
            holder.activitieslayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Activities.this, Activitydetails.class);
                    intent.putExtra("title",activities.getTitle());
                    intent.putExtra("description",activities.getDescription());
                    intent.putExtra("createdon",activities.getCreateon());
                    intent.putExtra("img",activities.getImgaddress());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return activitiesList.size();
        }

        class activitiesholder extends RecyclerView.ViewHolder{
            TextView title, description, createdon, upto;
            LinearLayout activitieslayout;
            public activitiesholder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.acti_title);
                description = itemView.findViewById(R.id.acti_description);
                createdon = itemView.findViewById(R.id.acti_createdon);
                upto = itemView.findViewById(R.id.acti_upto);
                activitieslayout = itemView.findViewById(R.id.acti_layout);
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
                        if( InternetCheck.isInternetOn(Activities.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                        else {
                            showsnackbar();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
