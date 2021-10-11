package com.school.iqdigit.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.UserAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.Model.UserResponse;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginByUser extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private ImageView layout;
    private boolean checkinternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_user);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(loginByUser.this));
        User user = SharedPrefManager.getInstance(this).getUser();
        checkinternet = InternetCheck.isInternetOn(loginByUser.this);
        Call<UserResponse> call = RetrofitClient.getInstance().getApi().getUsers(user.getPhone_number());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (InternetCheck.isInternetOn(loginByUser.this) == true) {
                    userList = response.body().getUsers();
                    adapter = new UserAdapter(loginByUser.this, userList);
                    recyclerView.setAdapter(adapter);
                }
                else
                {
                    showsnackbar();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                if (InternetCheck.isInternetOn(loginByUser.this) != true) {
                    showsnackbar();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!SharedPrefManager.getInstance(this).isloggedin()){
            Intent intent = new Intent(loginByUser.this, Splash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    private void showsnackbar(){
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if( InternetCheck.isInternetOn(loginByUser.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

}
