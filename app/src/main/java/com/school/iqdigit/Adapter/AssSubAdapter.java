package com.school.iqdigit.Adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.school.iqdigit.Activity.PdfWebViewActivity;
import com.school.iqdigit.Activity.imageActivity;
import com.school.iqdigit.Modeldata.AssSub;
import com.school.iqdigit.R;

import java.util.List;
import java.util.StringTokenizer;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class AssSubAdapter extends RecyclerView.Adapter<AssSubAdapter.AssSubHolder> {
    private Context mCtx;
    private List<AssSub> assSubList;
    DownloadManager downloadManager;

    public AssSubAdapter(Context mCtx, List<AssSub> assSubList) {
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
        final AssSub assSub = assSubList.get(i);
        assSubHolder.subject.setText("Subject :" + assSub.getSubject_name());
        assSubHolder.title.setText(assSub.getSubject_title());
        assSubHolder.description.setText(assSub.getSubject_description());
        final String imgeurl = assSub.getSubject_address();
        if (assSub.getSubject_address().contains("https") && assSub.getSubject_address().endsWith(".jpg")) {
            Glide.with(mCtx).load(imgeurl).into(assSubHolder.imageView);
        } else if (assSub.getSubject_address().contains("https") && assSub.getSubject_address().endsWith(".pdf")) {
            Glide.with(mCtx).load(getImage("homework_pic")).into(assSubHolder.imageView);
        } else {
            Glide.with(mCtx).load(BASE_URL2 + imgeurl).into(assSubHolder.imageView);
        }

        assSubHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assSub.getSubject_address().endsWith("pdf")) {
                    Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                    intent.putExtra("pdf", assSub.getSubject_address());
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
                if (assSub.getSubject_address().endsWith("pdf")) {
                    download(assSub.getSubject_address());
                } else {
                    downloadManager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri;
                    if (assSub.getSubject_address().contains("https")) {
                        uri = Uri.parse(assSub.getSubject_address());
                    } else {
                        uri = Uri.parse(BASE_URL2 + assSub.getSubject_address());
                    }

                    DownloadManager downloadmanager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long refrence = downloadManager.enqueue(request);
                    final String urldata = assSub.getSubject_address();
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
}
