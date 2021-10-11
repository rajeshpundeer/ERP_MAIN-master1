package com.school.iqdigit.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.instacart.library.truetime.TrueTime;
import com.school.iqdigit.Adapter.AnsStaffadapter;
import com.school.iqdigit.Adapter.AnsStudadapter;
import com.school.iqdigit.Adapter.TeacherAnsAdapter;
import com.school.iqdigit.Adapter.StudentAnsAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.AnsPreviewStaff;
import com.school.iqdigit.Model.AnsPreviewStaffResponse;
import com.school.iqdigit.Model.AnsPreviewStud;
import com.school.iqdigit.Model.AnsPreviewStudResponse;
import com.school.iqdigit.Model.CheckAnsStaffResponse;
import com.school.iqdigit.Model.CheckAnsStudResponse;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.GetCurrentTime;
import com.school.iqdigit.Model.McqLayout;
import com.school.iqdigit.Model.McqListResponse;
import com.school.iqdigit.Model.McqStaffAn;
import com.school.iqdigit.Model.McqStudAn;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;
import com.school.iqdigit.utils.SharedHelper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfWebViewActivity1 extends AppCompatActivity implements DownloadFile.Listener {
    private String TAG = "PdfWebViewActivity";
    private ProgressDialog mProg;
    private ImageView back;
    private String pdf_url;
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private PDFPagerAdapter adapter;
    private WebView PDFWEBVIEW;
    private PDFViewPager pdfViewPager;
    private RemotePDFViewPager remotePDFViewPager;
    private String hw_id = "", usertype = "";
    private RecyclerView rvMcqAns;
    private List<McqLayout> mcqlist = new ArrayList<>();
    private String mcq_count = "",timebound = "",start_date = "",end_date = "";
    private String mcq_marks = "";
    private List<String> mcqstudentlist = new ArrayList<>();
    private TextView tvNoAns,tv_timer;
    private List<McqStaffAn> McqStaffAnlist = new ArrayList<>();
    private List<McqStudAn> McqStudAnlist = new ArrayList<>();
    private Button btnDone;
    private Date currentdatetime;
    Date date1 = null;
    Date date2 = null;
    long different;
    private Timer timer;
    private List<AnsPreviewStud> AnsPreviewStudList = new ArrayList<>();
    private List<AnsPreviewStaff> AnsPreviewStaffList = new ArrayList<>();


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        pdfViewPager = findViewById(R.id.pdfViewPager);
        btnDone = findViewById(R.id.btnDone);
        ShowNotificationPop("Swipe LEFT (<--) or RIGHT (-->) for multiple pages.");
        checkAndRequestPermissions();
        tv_timer = findViewById(R.id.tv_timer);
        rvMcqAns = findViewById(R.id.rvMcqAns);
        tvNoAns = findViewById(R.id.tvNoAns);
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
        Log.d(TAG, "pdf_url " + pdf_url);
        hw_id = intent.getStringExtra("hw_id");
        usertype = intent.getStringExtra("usertype");
        timebound = intent.getStringExtra("timebound");
        start_date = intent.getStringExtra("start_date");
        end_date = intent.getStringExtra("end_date");
        Log.d(TAG, hw_id + " hw_id "+timebound+" timebound "+start_date+" startdate "+end_date+" enddate");
        if (timebound.equals("1") && (timebound != null)) {
            tv_timer.setVisibility(View.VISIBLE);
            Log.d(TAG, start_date + " start date " + end_date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                if (start_date != null && end_date != null) {
                    date1 = simpleDateFormat.parse(start_date);
                    date2 = simpleDateFormat.parse(end_date);

                    Log.d(TAG, date1 + " date1 " + date2 + " date2");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            timer = new Timer();
            Date finalDate = date2;
            Date finalDate1 = date1;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    ((Activity)PdfWebViewActivity1.this).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            try {
                                TrueTime.build().initialize();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Date TIMENOW = TrueTime.now();
                            String DATEFORMAT = "dd-MM-yyyy HH:mm:ss";
                            SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
                            String currentdate = sdf.format(TIMENOW);
                            String newcurrentdate = currentdate.replace("-", "/");
                                        try {
                                            currentdatetime = simpleDateFormat.parse(newcurrentdate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                     /*   Log.d(TAG, currentdatetime.getTime() + " curentdatetime" + date1.getTime() + " date1"
                                                + date2.getTime() + " date2");*/

                                        if (currentdatetime.getTime() >= finalDate1.getTime() && currentdatetime.getTime() < finalDate.getTime()) {

                                            different = finalDate.getTime() - currentdatetime.getTime();
                                            long difference = finalDate.getTime() - currentdatetime.getTime();
                                            Log.d(TAG, different + " difference");

                                            Log.d(TAG, "startDate : " + currentdatetime.getTime());
                                            Log.d(TAG, "endDate : " + finalDate.getTime());
                                            Log.d(TAG, "different : " + different);

                                            long secondsInMilli = 1000;
                                            long minutesInMilli = secondsInMilli * 60;
                                            long hoursInMilli = minutesInMilli * 60;
                                            long daysInMilli = hoursInMilli * 24;

                                            long elapsedDays = different / daysInMilli;
                                            different = different % daysInMilli;
                                            String elapsedDays1 = Long.toString(elapsedDays).replace("-", "");

                                            long elapsedHours = different / hoursInMilli;
                                            different = different % hoursInMilli;
                                            String elapsedHours1 = Long.toString(elapsedHours).replace("-", "");

                                            long elapsedMinutes = different / minutesInMilli;
                                            different = different % minutesInMilli;
                                            String elapsedMinutes1 = Long.toString(elapsedMinutes).replace("-", "");
                                            int min = Integer.parseInt(elapsedMinutes1) + 1;
                                            Log.d(TAG, min + " minutes");

                                            long elapsedSeconds = different / secondsInMilli;

                                            Log.d(TAG,
                                                    "%d days, %d hours, %d minutes, %d seconds%n" +
                                                            elapsedDays + " " + elapsedHours + " " + elapsedMinutes + " " + elapsedSeconds);

                                            if (difference > 0) {
                                                Log.d(TAG, "enabled " + difference);
                                                tv_timer.setBackgroundColor(Color.parseColor("#F4511E"));
                                                tv_timer.setText("Time to over " + elapsedDays1 + "days " + elapsedHours1 + "hr " + min + "min");
                                                btnDone.setVisibility(View.VISIBLE);
                                            } else {
                                                Log.d(TAG, "disabled1 " + difference);
                                                tv_timer.setText("Time Over");
                                                if (usertype.equals("student")){
                                                btnDone.setVisibility(View.GONE);}
                                            }

                                        } else {
                                            if (currentdatetime.getTime() < finalDate1.getTime()) {
                                                Log.d(TAG, "disabled2");

                                                different = currentdatetime.getTime() - finalDate1.getTime();
                                                Log.d(TAG, different + " difference");

                                                Log.d(TAG, "startDate : " + currentdatetime.getTime());
                                                Log.d(TAG, "endDate : " + finalDate1.getTime());
                                                Log.d(TAG, "different : " + different);

                                                long secondsInMilli = 1000;
                                                long minutesInMilli = secondsInMilli * 60;
                                                long hoursInMilli = minutesInMilli * 60;
                                                long daysInMilli = hoursInMilli * 24;

                                                long elapsedDays = different / daysInMilli;
                                                different = different % daysInMilli;
                                                String elapsedDays1 = Long.toString(elapsedDays).replace("-", "");

                                                long elapsedHours = different / hoursInMilli;
                                                different = different % hoursInMilli;
                                                String elapsedHours1 = Long.toString(elapsedHours).replace("-", "");

                                                long elapsedMinutes = different / minutesInMilli;
                                                different = different % minutesInMilli;
                                                String elapsedMinutes1 = Long.toString(elapsedMinutes).replace("-", "");
                                                int min = Integer.parseInt(elapsedMinutes1) + 1;
                                                Log.d(TAG, min + " minutes");

                                                long elapsedSeconds = different / secondsInMilli;
                                                String elapsedSeconds1 = Long.toString(elapsedSeconds).replace("-", "");

                                                tv_timer.setBackgroundColor(Color.parseColor("#43A047"));
                                                tv_timer.setText("Time to Start " + elapsedDays1 + "days " + elapsedHours1 + "hr " + min + "min");
                                                if (usertype.equals("student")){
                                                btnDone.setVisibility(View.GONE);}
                                            } else if (currentdatetime.getTime() > finalDate.getTime()) {
                                                Log.d(TAG, "disabled3");
                                                tv_timer.setText("Time Over");
                                                if (usertype.equals("student")){
                                                btnDone.setVisibility(View.GONE);}
                                            }
                                        }
                                    }
                    });
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 60000);
        } else {
            tv_timer.setVisibility(View.GONE);
            btnDone.setVisibility(View.VISIBLE);
        }
        if (InternetCheck.isInternetOn(PdfWebViewActivity1.this) == true) {
            if (usertype.equals("staff")) {
                rvMcqAns.setVisibility(View.VISIBLE);
                tvNoAns.setVisibility(View.GONE);
                mcq_count = intent.getStringExtra("mcq_count");
                mcq_marks = intent.getStringExtra("mcq_marks");
                if (Integer.parseInt(mcq_count) > 0) {
                    // for (int i = 1; i < 4; i++)
                    for (int i = 1; i < Integer.parseInt(mcq_count) + 1; i++) {
                        Log.d(TAG, "Q" + i);
                        mcqstudentlist.add(String.valueOf(i));

                    }
                    if (InternetCheck.isInternetOn(getApplicationContext()) == true) {
                        Call<CheckAnsStaffResponse> call = RetrofitClient.getInstance().getApi().getCheckAnsStaffResponse(hw_id);
                        call.enqueue(new Callback<CheckAnsStaffResponse>() {
                            @Override
                            public void onResponse(Call<CheckAnsStaffResponse> call, Response<CheckAnsStaffResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().getError() == false) {
                                        if (response.body().getMcqStaffAns() != null) {
                                            McqStaffAnlist = response.body().getMcqStaffAns();
                                        }
                                    }
                                }
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 5);
                                rvMcqAns.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
                                //  call the constructor of CustomAdapter to send the reference and data to Adapter
                                TeacherAnsAdapter teacherAnsAdapter = new TeacherAnsAdapter(PdfWebViewActivity1.this, mcqstudentlist, hw_id, McqStaffAnlist);
                                teacherAnsAdapter.setHasStableIds(true);
                                rvMcqAns.setAdapter(teacherAnsAdapter); // set the Adapter to RecyclerView
                            }

                            @Override
                            public void onFailure(Call<CheckAnsStaffResponse> call, Throwable t) {
                                if (t instanceof IOException) {
                                    Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                                    // logging probably not necessary
                                } else {
                                    Log.d(TAG, t.getMessage() + " error");
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.v("isAllSubmitted", SharedHelper.getKey(PdfWebViewActivity1.this, "isAllSubmitted"));
                            if (SharedHelper.getKey(PdfWebViewActivity1.this, "isAllSubmitted").equals("No")) {
                                ShowNotificationPop("Please submit all answers");
                                return;
                            }
                            Dialog dialog = new Dialog(PdfWebViewActivity1.this);
                            // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.dialog_and_key);
                            if (InternetCheck.isInternetOn(getApplicationContext()) == true) {
                                Call<AnsPreviewStaffResponse> call = RetrofitClient.getInstance().getApi().getAnsPreviewStaffResponse(hw_id);
                                call.enqueue(new Callback<AnsPreviewStaffResponse>() {
                                    @Override
                                    public void onResponse(Call<AnsPreviewStaffResponse> call, Response<AnsPreviewStaffResponse> response) {
                                        if (response.body() != null) {
                                            if (response.body().getError() == false) {
                                                if (response.body().getAnsPreviewStaff().size() > 0) {
                                                    AnsPreviewStaffList = response.body().getAnsPreviewStaff();
                                                    RecyclerView recyclerView = dialog.findViewById(R.id.rvViewAns);
                                                    AnsStaffadapter adapterRe = new AnsStaffadapter(getApplicationContext(),AnsPreviewStaffList);
                                                    recyclerView.setAdapter(adapterRe);
                                                    recyclerView.setHasFixedSize(true);
                                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AnsPreviewStaffResponse> call, Throwable t) {
                                        if (t instanceof IOException) {
                                            Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                                            // logging probably not necessary
                                        } else {
                                            Log.d(TAG, t.getMessage() + " error");
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                            Button btndialog = (Button) dialog.findViewById(R.id.btnOk);
                            btndialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   mProg.show();
                                    if (InternetCheck.isInternetOn(getApplicationContext()) == true) {
                                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().setans_done(hw_id);
                                        call.enqueue(new Callback<DefaultResponse>() {
                                            @Override
                                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                                mProg.dismiss();
                                                if (response.body() != null) {
                                                    if (response.body().isErr() == false) {
                                                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(PdfWebViewActivity1.this, AssessmentstaffActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                                mProg.dismiss();

                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Button btnCancle = (Button) dialog.findViewById(R.id.btnCancel);
                            btnCancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    });

                }
            } else if (usertype.equals("student")) {
                btnDone.setText("Finish");

                User user = SharedPrefManager.getInstance(PdfWebViewActivity1.this).getUser();
                Call<CheckAnsStudResponse> call = RetrofitClient.getInstance().getApi().getCheckAnsStudResponse(hw_id, user.getId());
                call.enqueue(new Callback<CheckAnsStudResponse>() {
                    @Override
                    public void onResponse(Call<CheckAnsStudResponse> call, Response<CheckAnsStudResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getError() == false) {
                                if (response.body().getMcqStudAns() != null) {
                                    McqStudAnlist = response.body().getMcqStudAns();
                                }
                            }
                        }
                        getmcqlist();
                    }

                    @Override
                    public void onFailure(Call<CheckAnsStudResponse> call, Throwable t) {
                       /* if (t instanceof IOException) {
                            Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                            // logging probably not necessary
                        }else {
                            Log.d(TAG ,t.getMessage()+" error");
                        }*/
                    }
                });

            }
        } else {
            showsnackbar();
        }
        remotePDFViewPager =
                new RemotePDFViewPager(getApplicationContext(), pdf_url, this);

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

    public void ShowNotificationPop(String txt) {
        Dialog dialog;
        TextView textView;
        FloatingActionButton imgcancel;
        Button btnOk;
        dialog = new Dialog(PdfWebViewActivity1.this);
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
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        pdfViewPager.setAdapter(adapter);
        mProg.dismiss();
    }

    @Override
    public void onFailure(Exception e) {
        mProg.dismiss();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        mProg.setMessage("Loading.." + progress);
    }

    private void getmcqlist() {
        mProg.show();
        Call<McqListResponse> call = RetrofitClient.getInstance().getApi().getMcqListResponse(hw_id);
        call.enqueue(new Callback<McqListResponse>() {
            @Override
            public void onResponse(Call<McqListResponse> call, Response<McqListResponse> response) {
                if (response.body().getError() == false) {
                    if (response.body().getMcqLayout().size() > 0) {
                        rvMcqAns.setVisibility(View.VISIBLE);
                        tvNoAns.setVisibility(View.GONE);
                        btnDone.setVisibility(View.VISIBLE);
                        mcqlist = response.body().getMcqLayout();
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 5);
                        rvMcqAns.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
                        rvMcqAns.setHasFixedSize(true);
                        //  call the constructor of CustomAdapter to send the reference and data to Adapter
                        StudentAnsAdapter studentAnsAdapter = new StudentAnsAdapter(PdfWebViewActivity1.this, mcqlist, hw_id, McqStudAnlist);
                        studentAnsAdapter.setHasStableIds(true);
                        rvMcqAns.setAdapter(studentAnsAdapter); // set the Adapter to RecyclerView
                    } else {
                        rvMcqAns.setVisibility(View.GONE);
                        tvNoAns.setVisibility(View.VISIBLE);
                        btnDone.setVisibility(View.GONE);
                        Log.d(TAG, "no data available");
                    }
                }
            }

            @Override
            public void onFailure(Call<McqListResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("isAnsSubmitted", SharedHelper.getKey(PdfWebViewActivity1.this, "isAnsSubmitted"));

                Dialog dialog = new Dialog(PdfWebViewActivity1.this);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_and_key);
                Button btnCancle = (Button) dialog.findViewById(R.id.btnCancel);
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                if (InternetCheck.isInternetOn(getApplicationContext()) == true) {
                    User user = SharedPrefManager.getInstance(PdfWebViewActivity1.this).getUser();
                    Call<AnsPreviewStudResponse> call = RetrofitClient.getInstance().getApi().getAnsPreviewStudResponse(hw_id,user.getId());
                    call.enqueue(new Callback<AnsPreviewStudResponse>() {
                        @Override
                        public void onResponse(Call<AnsPreviewStudResponse> call, Response<AnsPreviewStudResponse> response) {
                            if (response.body() != null) {
                                if (response.body().getError() == false) {
                                    if (response.body().getAnsPreviewStud().size() > 0) {
                                        AnsPreviewStudList = response.body().getAnsPreviewStud();
                                    }
                                    RecyclerView recyclerView = dialog.findViewById(R.id.rvViewAns);
                                    AnsStudadapter adapterRe = new  AnsStudadapter(getApplicationContext(),AnsPreviewStudList);
                                    recyclerView.setAdapter(adapterRe);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AnsPreviewStudResponse> call, Throwable t) {
                            if (t instanceof IOException) {
                                Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                                // logging probably not necessary
                            } else {
                                Log.d(TAG, t.getMessage() + " error");
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
                Button btndialog = (Button) dialog.findViewById(R.id.btnOk);
                btndialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProg.show();
                        if (InternetCheck.isInternetOn(getApplicationContext()) == true) {
                            User user = SharedPrefManager.getInstance(PdfWebViewActivity1.this).getUser();
                            Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().setmcq_completed_stud(hw_id, user.getId());
                            call.enqueue(new Callback<DefaultResponse>() {
                                @Override
                                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                    mProg.dismiss();
                                    if (response.body() != null) {
                                        if (response.body().isErr() == false) {
                                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(PdfWebViewActivity1.this, AssesmentStudentActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                   mProg.dismiss();
                                                    /*if (t instanceof IOException) {
                                                        Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                                                        // logging probably not necessary
                                                    }*/
                                }
                            });
                        } else {
                            showsnackbar();
                            // Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void showsnackbar() {
        if (mProg.isShowing()) {
            mProg.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(PdfWebViewActivity1.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

}