package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.R;

import java.io.File;
import java.util.Date;
import java.util.StringTokenizer;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class TutorialViewActivity extends AppCompatActivity {
    private String title = "", lesson = "" ,summery, description, fileurl = "";
    private TextView textViewTitle, tvLesson, tvSummery, tvDescription;
    private Button btnDownload;
    private ImageView imgbacktut;
    private int STORAGE_PERMISSION = 1;
    private String TAG = "TutorialViewActivity";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_view);

        textViewTitle = findViewById(R.id.tvTitleTutorial);
        tvLesson = findViewById(R.id.tvLesson);
       // tvSummery = findViewById(R.id.tvSummery);
        //tvDescription = findViewById(R.id.tvDescription);
        btnDownload = findViewById(R.id.btnDownload);
        imgbacktut = findViewById(R.id.imgbacktut);
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        lesson = bundle.getString("lesson");
        //summery = bundle.getString("summary");
       // description = bundle.getString("description");
        fileurl = bundle.getString("fileurl");
        textViewTitle.setText(title);
        if (!fileurl.equals("")) {
            btnDownload.setVisibility(View.VISIBLE);
        }else {
            btnDownload.setVisibility(View.GONE);
        }

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(TutorialViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(fileurl);
                    final String urldata = fileurl;
                    String last = urldata.substring(urldata.lastIndexOf("/") + 1);
                    Log.d(TAG ,last +" last");
                    StringTokenizer tokens = new StringTokenizer(last, ".");
                    String filename = tokens.nextToken();
                    String extension = tokens.nextToken();
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setTitle(last);
                    request.setDescription("Downloading");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,last);
                    downloadmanager.enqueue(request);
                    Toast.makeText(getApplicationContext(),"File is downloaded successfully and saved is downloads",Toast.LENGTH_LONG).show();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(TutorialViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(TutorialViewActivity.this)
                                .setTitle("Permission needed")
                                .setMessage("This permission is needed for downloading Tutorial")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(TutorialViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        }, STORAGE_PERMISSION);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .create().show();
                    } else {
                        ActivityCompat.requestPermissions(TutorialViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, STORAGE_PERMISSION);
                    }
                }

            }
        });

        imgbacktut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvLesson.setText(Html.fromHtml(lesson, Html.FROM_HTML_MODE_LEGACY));
            tvLesson.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            tvLesson.setText(Html.fromHtml(lesson));
            tvLesson.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(fileurl);
                final String urldata = fileurl;
                String last = urldata.substring(urldata.lastIndexOf("/") + 1);
                Log.d(TAG ,last +" last");
                StringTokenizer tokens = new StringTokenizer(last, ".");
                String filename = tokens.nextToken();
                String extension = tokens.nextToken();
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(last);
                request.setDescription("Downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,last);
                downloadmanager.enqueue(request);
                Toast.makeText(getApplicationContext(),"File is downloaded successfully and saved is downloads",Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
