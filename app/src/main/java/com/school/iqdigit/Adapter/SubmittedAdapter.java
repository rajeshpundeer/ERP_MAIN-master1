package com.school.iqdigit.Adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.school.iqdigit.Activity.PdfWebViewActivity;
import com.school.iqdigit.Activity.ViewAnswerKey;
import com.school.iqdigit.Activity.imageActivity1;
import com.school.iqdigit.Model.Assessment_list;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;

import java.util.List;
import java.util.StringTokenizer;

public class SubmittedAdapter extends RecyclerView.Adapter<SubmittedAdapter.Pendingholder>{
        private Context mCtx;
        private List<Assessment_list> submittedList;

        public SubmittedAdapter(Context mCtx, List<Assessment_list> submittedList) {
            this.mCtx = mCtx;
            this.submittedList = submittedList;
        }

        @NonNull
        @Override
        public Pendingholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_assessment_submitted_student,viewGroup,false);
            return new Pendingholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull  Pendingholder holder, int i) {
            Assessment_list submittedAssessment = submittedList.get(i);
            holder.title.setText(submittedAssessment.getAs_name());
            holder.date.setText(submittedAssessment.getCreated_at());
            holder.description.setText(submittedAssessment.getEs_detail());
            if(submittedAssessment.getAssessment().equals("2"))
            {
                holder.btnDownloadAss.setVisibility(View.GONE);
                holder.btnViewAssessment.setText("Answer Sheet");
            }
           // holder.tvSubject.setText("English");
            holder.tvSubject.setText(submittedAssessment.getEs_subjectname()+" / "+submittedAssessment.getTeacher_name());
            if(submittedAssessment.getAssessment_unit().equals("Marks")) {
                holder.al_assessment_type.setText(submittedAssessment.getAssessment_unit() + "( " + submittedAssessment.getMax() + ")");
            }else
            {
                holder.al_assessment_type.setText(submittedAssessment.getAssessment_unit());
            }
            holder.btnViewHomework.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (submittedAssessment.getAs_description().endsWith("pdf")) {
                        Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                        intent.putExtra("pdf",submittedAssessment.getAs_description());
                        intent.putExtra("title","Assessment");
                        mCtx.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mCtx, imageActivity1.class);
                        intent.putExtra("imgurl", submittedAssessment.getAs_description());
                        mCtx.startActivity(intent);
                    }
                }
            });
            holder.btnViewAssessment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(submittedAssessment.getAssessment().equals("2"))
                    {
                        User user = SharedPrefManager.getInstance(mCtx).getUser();
                        Intent intent = new Intent(mCtx, ViewAnswerKey.class);
                        intent.putExtra("student_id",user.getId());
                        intent.putExtra("hw_id", String.valueOf(submittedAssessment.getHw_id()));
                        intent.putExtra("user_type","student");
                        intent.putExtra("status","submitted_student");
                        mCtx.startActivity(intent);
                    }else {
                        Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                        intent.putExtra("pdf", submittedAssessment.getPath());
                        intent.putExtra("title","Assessment");
                        mCtx.startActivity(intent);
                    }
                }
            });

            holder.btnDownloadAss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    download(submittedAssessment.getPath());
                }
            });
        }

        @Override
        public int getItemCount() {
            return submittedList.size();
        }

        class Pendingholder extends RecyclerView.ViewHolder{
            TextView title,date,description,tvSubject,al_assessment_type;
            Button btnViewHomework, btnViewAssessment ;
            ImageView btnDownloadAss;
            public Pendingholder(@NonNull View itemView) {
                super(itemView);
                tvSubject = itemView.findViewById(R.id.tvSubject);
                title = itemView.findViewById(R.id.al_title);
                date = itemView.findViewById(R.id.al_createdon);
                description = itemView.findViewById(R.id.al_description);
                al_assessment_type = itemView.findViewById(R.id.al_assessment_type);
                btnViewHomework = (Button) itemView.findViewById(R.id.btnViewHomework);
                btnViewAssessment = itemView.findViewById(R.id.btnViewAssessment);
                btnDownloadAss = (ImageView) itemView.findViewById(R.id.btnDownloadAss);
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