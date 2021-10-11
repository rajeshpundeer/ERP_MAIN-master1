package com.school.iqdigit.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Activity.MainActivity;
import com.school.iqdigit.Activity.loginByUser;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.Model.userloginresponse;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UsersViewHolder> {

    private Context mCtx;
    private List<User> userList;

    public UserAdapter(Context mCtx, List<User> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_login_by_users, viewGroup,false);
        return  new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int i) {
        final User user = userList.get(i);
        final String login_with_student = "Login With " + user.getName();
        usersViewHolder.student_name.setText(login_with_student);
        usersViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = user.getId();
                userlogin(id,v);
            }
        });

    }
    private void userlogin(String id,View view) {
        SharedPrefManager.getInstance(mCtx).clear();
        SharedPrefManager.getInstance(mCtx).clear();
        Call<userloginresponse> call = RetrofitClient
                .getInstance().getApi().userloginbyid(id);
        call.enqueue(new Callback<userloginresponse>() {
            @Override
            public void onResponse(Call<userloginresponse> call, Response<userloginresponse> response) {
                if (InternetCheck.isInternetOn(mCtx) == true) {
                    userloginresponse Userloginresponse = response.body();
                    if (!Userloginresponse.isError()) {
                        SharedPrefManager.getInstance(mCtx)
                                .saveUser(Userloginresponse.getUser());
                        Intent intent = new Intent(mCtx, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("user_type", "student");
                        intent.putExtra("usertype", "student");
                        mCtx.startActivity(intent);
                    } else {
                        Toast.makeText(mCtx, Userloginresponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar snackbar1 = Snackbar.make(view, R.string.internetproblem, Snackbar.LENGTH_LONG);
                    View sbView = snackbar1.getView();
                    TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar1.show();
                }
            }

            @Override
            public void onFailure(Call<userloginresponse> call, Throwable t) {
               // Toast.makeText(mCtx, "Error in Login"+t, Toast.LENGTH_LONG).show();
                if (InternetCheck.isInternetOn(mCtx) != true) {
                    Snackbar snackbar1 = Snackbar.make(view, R.string.internetproblem, Snackbar.LENGTH_LONG);
                    View sbView = snackbar1.getView();
                    TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar1.show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder{
            TextView student_name;
            CardView cardView;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            student_name = itemView.findViewById(R.id.student_name);
        }
    }
}
