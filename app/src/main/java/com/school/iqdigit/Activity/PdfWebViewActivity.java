package com.school.iqdigit.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.school.iqdigit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class PdfWebViewActivity extends AppCompatActivity implements DownloadFile.Listener {
    private String TAG = "PdfWebViewActivity";
    private ProgressDialog mProg;
    private ImageView back,btnDownloadAss;
    private String pdf_url,title;
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private PDFPagerAdapter adapter;
    private WebView PDFWEBVIEW;
    private PDFViewPager pdfViewPager;
    private RemotePDFViewPager remotePDFViewPager;
    private TextView tvTitle;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_web_view);
        pdfViewPager = findViewById(R.id.pdfViewPager);
        tvTitle = findViewById(R.id.tvTitle);
        btnDownloadAss = findViewById(R.id.btnDownloadAss);
        ShowNotificationPop("Swipe LEFT (<--) or RIGHT (-->) for multiple pages.");
       // PDFWEBVIEW = new WebView(this);
       // PDFWEBVIEW = findViewById(R.id.PDFWEBVIEW);
        //PDFWEBVIEW.getSettings().setJavaScriptEnabled(true);
       // PDFWEBVIEW.getSettings().getDisplayZoomControls();
        checkAndRequestPermissions();
        back = findViewById(R.id.backbtn);
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name_main);
        mProg.setMessage("Loading...");
        mProg.show();
        Intent intent = getIntent();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pdf_url = intent.getStringExtra("pdf");
        title = intent.getStringExtra("title");
        tvTitle.setText(title);
        Log.d(TAG, "pdf_url " + pdf_url);
        remotePDFViewPager =
                new RemotePDFViewPager(getApplicationContext(), pdf_url, this);
          btnDownloadAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    download(pdf_url);
            }
        });


       // pdfViewPager = new PDFViewPager(this, pdf_url);
        //PDFWEBVIEW.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf_url);

    }


    private boolean checkAndRequestPermissions() {
        int readpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for both permissions
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "sms & location services permission granted");
                } else {
                    Log.d(TAG, "Some permissions are not granted ask again ");
                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showDialogOK("Service Permissions are required for this app",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                finish();
                                                break;
                                        }
                                    }
                                });
                    }
                    //permission is denied (and never ask again is  checked)
                    //shouldShowRequestPermissionRationale will return false
                    else {
                        explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                        //                            //proceed with logic by disabling the related features or quit the app.
                    }
                }
            }
        }

    }
    public void ShowNotificationPop(String txt){
        Dialog dialog;
        TextView textView;
        FloatingActionButton imgcancel;
        Button btnOk;
        dialog = new Dialog(PdfWebViewActivity.this);
        dialog.setContentView(R.layout.pdf_popup1);
        textView = dialog.findViewById(R.id.textmaintain);
        imgcancel = dialog.findViewById(R.id.img_cancel_dialog);
        btnOk = dialog.findViewById(R.id.btnOk);
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        textView.setText(txt);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.closeOptionsMenu();
        dialog.setCancelable(true);
        dialog.show();
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                onBackPressed();
            }
        };
    }
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.exampledemo.parsaniahardik.marshmallowpermission")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
      /*  adapter = new PDFPagerAdapter(this, destinationPath);
        remotePDFViewPager.setAdapter(adapter);
        setContentView(remotePDFViewPager);
       */
        adapter = new PDFPagerAdapter(this,FileUtil.extractFileNameFromURL(url));
        pdfViewPager.setAdapter(adapter);
        mProg.dismiss();
    }

    @Override
    public void onFailure(Exception e) {
      mProg.dismiss();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        mProg.setMessage("Loading.."+progress);
    }

    public void download(String path) {
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
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
        Toast.makeText(getApplicationContext(), "File is downloaded successfully and saved in downloads", Toast.LENGTH_LONG).show();
    }
   }


