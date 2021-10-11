package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.school.iqdigit.R;

import java.io.File;
import java.util.StringTokenizer;

public class BookletViewActivity extends AppCompatActivity {
    private String name, description, fileurl;
    private TextView tvTitleBook, tvDescription1;
    private ImageView imgbackBook;
    private ImageView btnDownload;
    private Button viewPdf;
    private String TAG = "BookletViewActivity";
    private int STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklet_view);
        viewPdf = findViewById(R.id.viewPdf);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        description = bundle.getString("description");
        fileurl = bundle.getString("fileurl");
        tvTitleBook = findViewById(R.id.tvTitleBook);
        imgbackBook = findViewById(R.id.imgbackBook);
        btnDownload = findViewById(R.id.btnDownload);
        tvDescription1 = findViewById(R.id.tvDescription1);
        tvTitleBook.setText(name);

        if (!fileurl.equals("")) {
            btnDownload.setVisibility(View.VISIBLE);
        }

        viewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookletViewActivity.this, PdfWebViewActivity.class);
                intent.putExtra("pdf", fileurl);
                startActivity(intent);
            }
        });
        imgbackBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(BookletViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(fileurl);
                    final String urldata = fileurl;
                    String last = urldata.substring(urldata.lastIndexOf("/") + 1);
                    Log.d(TAG, last + " last");
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
                    Toast.makeText(getApplicationContext(),"File is downloaded successfully and saved in downloads",Toast.LENGTH_LONG).show();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(BookletViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(BookletViewActivity.this)
                                .setTitle("Permission needed")
                                .setMessage("This permission is needed for downloading Tutorial")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(BookletViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                        ActivityCompat.requestPermissions(BookletViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, STORAGE_PERMISSION);
                    }
                }
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvDescription1.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
            tvDescription1.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            tvDescription1.setText(Html.fromHtml(description));
            tvDescription1.setMovementMethod(LinkMovementMethod.getInstance());
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
                Log.d(TAG, last + " last");
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
                Toast.makeText(getApplicationContext(),"File is downloaded successfully and saved is downloads",Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
