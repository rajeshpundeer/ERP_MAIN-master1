package com.school.iqdigit.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.school.iqdigit.Activity.IcardActivity;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Modeldata.StudentsPhoto;
import com.school.iqdigit.R;
import com.school.iqdigit.interfaces.IcardStudentClicked;
import com.squareup.picasso.Picasso;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IcardAdapter extends RecyclerView.Adapter<IcardAdapter.UsersViewHolder> {
    private Context mCtx;
    private List<StudentsPhoto> StudentsList;
    private ProgressDialog progressDialog;
    private String TAG = "IcardAdapter";
    private IcardStudentClicked icardStudentClicked;

    public IcardAdapter(Context mCtx, List<StudentsPhoto> StudentsList,IcardStudentClicked icardStudentClicked) {
        this.mCtx = mCtx;
        this.StudentsList = StudentsList;
        this.icardStudentClicked = icardStudentClicked;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_students_icard, viewGroup, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position) {
        Log.d(TAG, position + " position");
        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setTitle("Photo Status Update");
        progressDialog.setMessage("Wait a moment...");
        progressDialog.create();
        final StudentsPhoto studentdetail = StudentsList.get(position);
        usersViewHolder.student_name.setText(studentdetail.getStu_name());

        if (studentdetail.getPhoto_editable_stud() == 1) {
            usersViewHolder.btnStatusLock.setVisibility(View.VISIBLE);
            usersViewHolder.btnStatusUnlock.setVisibility(View.GONE);
        } else {
            usersViewHolder.btnStatusLock.setVisibility(View.GONE);
            usersViewHolder.btnStatusUnlock.setVisibility(View.VISIBLE);
        }
        //"https://media.glamour.com/photos/5695aa8e93ef4b09520dfd8f/master/w_1600%2Cc_limit/sex-love-life-2009-12-1207-01_profile_pic_li.jpg
        Log.d("photo",studentdetail.getPre_image());

        Glide.with(mCtx)
                .load(studentdetail.getPre_image())
                .override(50, 50)
                .error(R.drawable.profile_color)
                .into(usersViewHolder.student_image);
        usersViewHolder.btnStatusUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().photo_status_update(String.valueOf(studentdetail.stu_id), "1");
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body().isErr() == false) {
                            progressDialog.dismiss();
                            icardStudentClicked.getId(String.valueOf(studentdetail.stu_id));
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, "Failed to change Status", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        usersViewHolder.btnStatusLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().photo_status_update(String.valueOf(studentdetail.stu_id), "0");
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body().isErr() == false) {
                            progressDialog.dismiss();
                            icardStudentClicked.getId(String.valueOf(studentdetail.stu_id));
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, "Failed to change Status", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        usersViewHolder.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, IcardActivity.class);
                intent.putExtra("info",String.valueOf(studentdetail.stu_id));
                intent.putExtra("type","student");
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return StudentsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {
        private TextView student_name;
        private Button btnStatusUnlock, btnStatusLock, btnUpload;
        private ImageView student_image;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            student_name = itemView.findViewById(R.id.tvStudentName);
            btnStatusUnlock = itemView.findViewById(R.id.btnStatusUnlock);
            btnStatusLock = itemView.findViewById(R.id.btnStatusLock);
            btnUpload = itemView.findViewById(R.id.btnUpload);
            student_image = itemView.findViewById(R.id.student_image);
        }
    }
}
