package com.school.iqdigit.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.iqdigit.Activity.PdfWebViewActivity;
import com.school.iqdigit.Activity.imageActivity;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.StaffAssignment;
import com.school.iqdigit.Modeldata.AssSub;
import com.school.iqdigit.R;

import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class AssSubStaffAdapter extends RecyclerView.Adapter<AssSubStaffAdapter.AssSubHolder> {
    private Context mCtx;
    private List<StaffAssignment> assSubList;
    DownloadManager downloadManager;

    public AssSubStaffAdapter(Context mCtx, List<StaffAssignment> assSubList) {
        this.mCtx = mCtx;
        this.assSubList = assSubList;
    }

    @NonNull
    @Override
    public AssSubHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_asssub_of_users, viewGroup, false);
        return new AssSubHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AssSubHolder assSubHolder, int i) {
        final StaffAssignment assSub = assSubList.get(i);
        assSubHolder.subject.setText("Subject :" + assSub.getSubjectName());
        assSubHolder.title.setText(assSub.getSubjectTitle());
        assSubHolder.description.setText(assSub.getSubjectDescription());
        final String imgeurl = assSub.getSubjectAddress();
        if (assSub.getSubjectAddress().contains("https") && assSub.getSubjectAddress().endsWith(".jpg")) {
            Glide.with(mCtx).load(imgeurl).into(assSubHolder.imageView);
        } else if (assSub.getSubjectAddress().contains("https") && assSub.getSubjectAddress().endsWith(".pdf")) {
            Glide.with(mCtx).load(getImage("homework_pic")).into(assSubHolder.imageView);
        } else {
            Glide.with(mCtx).load(BASE_URL2 + imgeurl).into(assSubHolder.imageView);
        }

        assSubHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assSub.getSubjectAddress().endsWith("pdf")) {
                    Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                    intent.putExtra("pdf", assSub.getSubjectAddress());
                    intent.putExtra("title","Homework");
                    mCtx.startActivity(intent);
                } else {
                    Intent intent = new Intent(mCtx, imageActivity.class);
                    intent.putExtra("imgurl", imgeurl);
                    intent.putExtra("title","Homework");
                    mCtx.startActivity(intent);
                }
            }
        });
        assSubHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assSub.getSubjectAddress().endsWith("pdf")) {
                    download(assSub.getSubjectAddress());
                } else {
                    downloadManager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri;
                    if (assSub.getSubjectAddress().contains("https")) {
                        uri = Uri.parse(assSub.getSubjectAddress());

                    } else {
                        uri = Uri.parse(BASE_URL2 + assSub.getSubjectAddress());

                    }
                    DownloadManager downloadmanager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long refrence = downloadManager.enqueue(request);
                    final String urldata = assSub.getSubjectAddress();
                    String last = urldata.substring(urldata.lastIndexOf("/") + 1);
                    request.setTitle(last);
                    request.setDescription("Downloading");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, last);
                    downloadmanager.enqueue(request);
                    Toast.makeText(mCtx, "File is downloaded successfully and saved in downloads", Toast.LENGTH_LONG).show();
                }
            }
        });


     /*   assSubHolder.tvDeleteHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNotificationPop(String.valueOf(assSub.get));
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return assSubList.size();
    }

    class AssSubHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, description;
        Button subject;
        ImageButton download;

        public AssSubHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.assSubject);
            title = itemView.findViewById(R.id.ass_title);
            description = itemView.findViewById(R.id.ass_deription);
            download = itemView.findViewById(R.id.ass_download);
            imageView = itemView.findViewById(R.id.download_image);
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

    public int getImage(String imageName) {

        int drawableResourceId = mCtx.getResources().getIdentifier(imageName, "drawable", mCtx.getPackageName());

        return drawableResourceId;
    }

    /*public void ShowNotificationPop(String hwid){
        Dialog dialog;
        TextView textView;
        FloatingActionButton imgcancel;
        Button btnOk,btnCancel;
        dialog = new Dialog(mCtx);
        dialog.setContentView(R.layout.ic_common_text_popup1);
        textView = dialog.findViewById(R.id.textmaintain);
        imgcancel = dialog.findViewById(R.id.img_cancel_dialog);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnOk = dialog.findViewById(R.id.btnOk);
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().delete_assessment(hwid);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body().isErr() == false) {
                            progressDialog.dismiss();
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            ((Activity) mCtx).finish();
                            mCtx.startActivity( ((Activity) mCtx).getIntent());
                        } else {
                            progressDialog.dismiss();
                            dialog.dismiss();
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mCtx, "Homework Deletion Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        textView.setText("Homework will not be recovered back, once deleted!  Are you sure you want to delete the Homework?");
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.closeOptionsMenu();
        dialog.setCancelable(true);
        dialog.show();
    }*/
}
