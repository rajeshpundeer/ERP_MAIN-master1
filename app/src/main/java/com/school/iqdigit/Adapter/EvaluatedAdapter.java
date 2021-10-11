package com.school.iqdigit.Adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.school.iqdigit.Activity.BookletViewActivity;
import com.school.iqdigit.Activity.PdfWebViewActivity;
import com.school.iqdigit.Activity.ViewAnswerKey;
import com.school.iqdigit.Activity.imageActivity1;
import com.school.iqdigit.Model.Assessment_list;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;

import java.util.List;
import java.util.StringTokenizer;

public class EvaluatedAdapter extends RecyclerView.Adapter<EvaluatedAdapter.Pendingholder> {
    private Context mCtx;
    private List<Assessment_list> evaluatedList;

    public EvaluatedAdapter(Context mCtx, List<Assessment_list> evaluatedList) {
        this.mCtx = mCtx;
        this.evaluatedList = evaluatedList;
    }

    @NonNull
    @Override
    public Pendingholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_assessment_checked_student, viewGroup, false);
        return new Pendingholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pendingholder holder, int i) {
        Assessment_list pendingAssessment = evaluatedList.get(i);
        holder.title.setText(pendingAssessment.getAs_name());
        holder.date.setText(pendingAssessment.getChecked_at());
        holder.description.setText(pendingAssessment.getEs_detail());
        if(pendingAssessment.getAssessment().equals("2"))
        {
            holder.btnDownloadAss.setVisibility(View.GONE);
            holder.tvTeacherRemarks.setVisibility(View.GONE);
            holder.btnViewAssessment.setText("Ans.Key");
        }

        holder.al_assessment_type.setText(pendingAssessment.getAssessment_unit());
       // holder.tvSubject.setText("English");
        holder.tvSubject.setText(pendingAssessment.getEs_subjectname()+" / "+pendingAssessment.getTeacher_name());
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pendingAssessment.getAs_description().endsWith("pdf")) {
                    Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                    intent.putExtra("pdf", pendingAssessment.getAs_description());
                    intent.putExtra("title","Assessment");
                    mCtx.startActivity(intent);
                } else {
                    Intent intent = new Intent(mCtx, imageActivity1.class);
                    intent.putExtra("imgurl", pendingAssessment.getAs_description());
                    mCtx.startActivity(intent);
                }
            }
        });
        holder.btnViewAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pendingAssessment.getAssessment().equals("2"))
                {
                    User user = SharedPrefManager.getInstance(mCtx).getUser();
                    Intent intent = new Intent(mCtx, ViewAnswerKey.class);
                    intent.putExtra("student_id",user.getId());
                    intent.putExtra("hw_id", String.valueOf(pendingAssessment.getHw_id()));
                    intent.putExtra("user_type","student");
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
        if (pendingAssessment.getAssessment_unit().equalsIgnoreCase("Marks")) {
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.btnSubmit.setText("Score: " + pendingAssessment.getScore() +"/"+pendingAssessment.getMax());
        } else if (pendingAssessment.getAssessment_unit().equalsIgnoreCase("Rating")) {
            holder.btnSubmit.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.btnSubmit.setVisibility(View.GONE);
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
        } else if (pendingAssessment.getAssessment_unit().equalsIgnoreCase("Percentage")) {
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.btnSubmit.setText("Percentage: " + pendingAssessment.getScore() + "%");
        } else if (pendingAssessment.getAssessment_unit().equalsIgnoreCase("Grade")) {
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.btnSubmit.setText("Grade: " + pendingAssessment.getScore());
        } else if(pendingAssessment.getAssessment_unit().equalsIgnoreCase("N/A")) {
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.btnSubmit.setText("N/A");
        }

        holder.tvTeacherRemarks.setText(pendingAssessment.getCheck_remarks());
        holder.btnDownloadAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download(pendingAssessment.getPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return evaluatedList.size();
    }

    class Pendingholder extends RecyclerView.ViewHolder {
        TextView title, date, description, tvSubject,al_assessment_type,tvTeacherRemarks;
        Button  btnSubmit, btnViewAssessment;
        ImageView btnDownloadAss,btnView;
        RatingBar ratingBar;

        public Pendingholder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            title = itemView.findViewById(R.id.al_title);
            date = itemView.findViewById(R.id.al_createdon);
            description = itemView.findViewById(R.id.al_description);
            btnView = itemView.findViewById(R.id.btnView);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);
            btnViewAssessment = itemView.findViewById(R.id.btnViewAssessment);
            ratingBar = itemView.findViewById(R.id.rating);
            btnDownloadAss = itemView.findViewById(R.id.btnDownloadAss);
            al_assessment_type = itemView.findViewById(R.id.al_assessment_type);
            tvTeacherRemarks = itemView.findViewById(R.id.tvTeacherRemarks);
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