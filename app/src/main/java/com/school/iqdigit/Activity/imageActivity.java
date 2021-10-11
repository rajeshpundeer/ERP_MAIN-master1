package com.school.iqdigit.Activity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.R;

public class imageActivity extends AppCompatActivity {
    PhotoView imageView;
    private ImageView backbtn,btnDownloadAss;
    private TextView tvTitle;
    DownloadManager downloadManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.app_name_main));
        progressDialog.setMessage("Loading....");
        progressDialog.create();
        imageView = findViewById(R.id.image_view);
        backbtn = findViewById(R.id.backbtn);
        tvTitle = findViewById(R.id.tvTitle);
        btnDownloadAss = findViewById(R.id.btnDownloadAss);
        Bundle extras = getIntent().getExtras();
        String imgurl = extras.getString("imgurl");
        String title = extras.getString("title");
        tvTitle.setText(title);
        if(imgurl.contains("https")){
            progressDialog.show();
            Glide.with(this)
                    .load(imgurl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressDialog.dismiss();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressDialog.dismiss();
                            return false;
                        }})
                    .into(imageView);
        }else {
            String imgnewurl = BuildConfig.BASE_UR+extras.getString("imgurl");
            progressDialog.show();
            Glide.with(this)
                    .load(imgnewurl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressDialog.dismiss();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressDialog.dismiss();
                            return false;
                        }})
                    .into(imageView);
        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        btnDownloadAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgurl.contains("https")) {
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri;
                    uri = Uri.parse(imgurl);

                    DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long refrence = downloadManager.enqueue(request);
                    final String urldata = imgurl;
                    String last = urldata.substring(urldata.lastIndexOf("/") + 1);
                    request.setTitle(last);
                    request.setDescription("Downloading");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, last);
                    downloadmanager.enqueue(request);
                    Toast.makeText(getApplicationContext(), "File is downloaded successfully and saved in downloads", Toast.LENGTH_LONG).show();
                } else {
                    String imgurlnew = BuildConfig.BASE_UR + imgurl;
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri;
                    uri = Uri.parse(imgurlnew);

                    DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long refrence = downloadManager.enqueue(request);
                    final String urldata = imgurlnew;
                    String last = urldata.substring(urldata.lastIndexOf("/") + 1);
                    request.setTitle(last);
                    request.setDescription("Downloading");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, last);
                    downloadmanager.enqueue(request);
                    Toast.makeText(getApplicationContext(), "File is downloaded successfully and saved in downloads", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
