package com.school.iqdigit.Adapter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.iqdigit.Activity.PdfWebViewActivity;
import com.school.iqdigit.Activity.ViewAnswerKey;
import com.school.iqdigit.Activity.imageActivity1;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.GetAssessmentListRoot;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;

import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluatedStaffAdapter extends RecyclerView.Adapter<EvaluatedStaffAdapter.Pendingholder> {
    private Context mCtx;
    private List<GetAssessmentListRoot> pendingList;
    private ProgressDialog mProg;
    public EvaluatedStaffAdapter(Context mCtx, List<GetAssessmentListRoot> pendingList) {
        this.mCtx = mCtx;
        this.pendingList = pendingList;
    }

    @NonNull
    @Override
    public Pendingholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_assessment_checked, viewGroup, false);
        return new Pendingholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pendingholder holder, int i) {
        GetAssessmentListRoot pendingAssessment = pendingList.get(i);
        if(pendingAssessment.getAssessment().equals("2"))
        {
            holder.btnDownloadAss.setVisibility(View.GONE);
            holder.btnCheck.setVisibility(View.VISIBLE);
        }else {
            holder.btnDownloadAss.setVisibility(View.VISIBLE);
            holder.btnCheck.setVisibility(View.GONE);
        }
        holder.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProg = new ProgressDialog(mCtx);
                mProg.setTitle(R.string.app_name_main);
                mProg.setMessage("Loading...");
                mProg.show();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().publish_result_mcq_single(String.valueOf(pendingAssessment.getHwId()),String.valueOf(pendingAssessment.getStudId()));
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body().isErr() == false) {
                            mProg.dismiss();
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            mProg.dismiss();
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        mProg.dismiss();
                    }
                });
            }
        });
        holder.StudentDetail.setText("[ "+pendingAssessment.getAdmissionId()+" ] "+pendingAssessment.getStudentName());
        holder.btnViewAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pendingAssessment.getAssessment().equals("2"))
                {
                    Intent intent = new Intent(mCtx, ViewAnswerKey.class);
                    intent.putExtra("student_id",String.valueOf(pendingAssessment.getStudId()));
                    intent.putExtra("hw_id", String.valueOf( pendingAssessment.getHwId()));
                    intent.putExtra("user_type","staff");
                    intent.putExtra("status","evaluated");
                    mCtx.startActivity(intent);
                }else {
                    Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                    intent.putExtra("pdf", pendingAssessment.getPath());
                    intent.putExtra("title","Assessment");
                    mCtx.startActivity(intent);
                }
            }
        });

        if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Marks")) {
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.btnSubmit.setText("Score: " + pendingAssessment.getScore() +"/"+pendingAssessment.getMax());
        } else if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Rating")) {
            holder.btnSubmit.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.VISIBLE);
            if (pendingAssessment.getScore().equals("1"))
                holder.ratingBar.setRating(1);
            else if (pendingAssessment.getScore().equals("2")) {
                holder.ratingBar.setRating(2);
            } else if (pendingAssessment.getScore().equals("3")) {
                holder.ratingBar.setRating(3);
            } else if (pendingAssessment.getScore().equals("4")) {
                holder.ratingBar.setRating(4);
            } else if (pendingAssessment.getScore().equals("5")) {
                holder.ratingBar.setRating(5);
            }
        } else if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Percentage")) {
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.btnSubmit.setText("Percentage: " + pendingAssessment.getScore() + "%");
        } else if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Grade")) {
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.btnSubmit.setText("Grade: " + pendingAssessment.getScore());
        } else {
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.btnSubmit.setText("N/A");
        }
        holder.btnDownloadAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download(pendingAssessment.getPath());
            }
        });

    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    class Pendingholder extends RecyclerView.ViewHolder {
        TextView StudentDetail;
        Button  btnViewAssessment, btnSubmit,btnCheck;
        ImageView btnDownloadAss;
        RatingBar ratingBar;

        public Pendingholder(@NonNull View itemView) {
            super(itemView);
            btnViewAssessment = itemView.findViewById(R.id.btnViewAssessment);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);
            ratingBar = itemView.findViewById(R.id.rating);
            StudentDetail = itemView.findViewById(R.id.StudentDetail);
            btnDownloadAss = itemView.findViewById(R.id.btnDownloadAss);
            btnCheck = itemView.findViewById(R.id.btnCheck);
        }
    }

    public void download(String path) {
        DownloadManager downloadmanager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(path);
        final String urldata = path;
        String last = urldata.substring(urldata.lastIndexOf("/") + 1);
        StringTokenizer tokens = new StringTokenizer(last, ".");
        String filename = tokens.nextToken();
        String extension = tokens.nextToken();
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(last);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, last);
        downloadmanager.enqueue(request);
        Toast.makeText(mCtx, "File is downloaded successfully and saved in downloads", Toast.LENGTH_LONG).show();
    }
}