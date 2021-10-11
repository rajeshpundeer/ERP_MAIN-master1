package com.school.iqdigit.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;
import com.school.iqdigit.Adapter.GalleryAdapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.BuildConfig;
import com.school.iqdigit.Model.AlertsResponse;
import com.school.iqdigit.Model.AllTodayNoticeResponse;
import com.school.iqdigit.Model.BannerResponse;
import com.school.iqdigit.Model.ErrorResponse;
import com.school.iqdigit.Model.GetAllTodayNoticeStaff;
import com.school.iqdigit.Model.GetPhoto;
import com.school.iqdigit.Model.IcardCheckResponse;
import com.school.iqdigit.Model.LoginResponse;
import com.school.iqdigit.Model.MaintenanceResponse;
import com.school.iqdigit.Model.NoticeStaffResponse;
import com.school.iqdigit.Model.PendingFeeResponse;
import com.school.iqdigit.Model.PhotoCheck;
import com.school.iqdigit.Model.PhotosRespose;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Model.noticeresponse;
import com.school.iqdigit.Model.staffloginresponse;
import com.school.iqdigit.Modeldata.Banner;
import com.school.iqdigit.Modeldata.Staff;
import com.school.iqdigit.Modeldata.TodayNall;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.Storage.SharedPrefManagerGuest;
import com.school.iqdigit.utility.InternetCheck;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class MainActivity extends AppCompatActivity implements ForceUpdateChecker.OnUpdateNeededListener {
    private BottomNavigationView navigationView;
    private ImageView shownotice;
    private Dialog myDialog, myDialog2,iCardDialog;
    private Intent mainintent;
    private List<Banner> bannerList;
    private Button btnLoginTeacher,btnLoginStudent;
    private Dialog dialog;
    private String ftoken;
    private String TAG = "MainActivity: ";
    //Today's Notice Adapter
    private RecyclerView recyclerView;
    private List<TodayNall> todayNallList;
    private List<GetAllTodayNoticeStaff> todayNoticeStaffList;
    private int current_version;
    AlertDialog.Builder builder;
    private TextView tvVersion, tvHelp, tvFeedback;
    private ImageView imgHelp, imgFeedback;
    //Layout of Modules Displayed on Homepage
    private FloatingActionButton lay_cir, layout_attendance_staff, layout_registration_staff, layout_timetable_staff, layout_circular_staff, lay_holidays, lay_www, layour_www_staff, lay_homework, lay_activities, lay_alerts, lay_profile, lay_attendance, lay_feepayment, lay_examination, lay_feereceipt, lay_timetable;
    private FloatingActionButton lay_add_activities, lay_add_alerts, lay_profile_staff, Lay_homework_staff;
    private FloatingActionButton lay_studymaterial, lay_gallery, lay_registration, lay_report_card, lay_birthday, lay_feedback;
    private LinearLayout main_lay_user, main_lay_staff, lay_home, lay_settings;
    private FloatingActionButton lay_liveclass_staff,layout_LiveclassStaff1, layout_Liveclass1,lay_liveclass_stud, lay_studymaterial_staff, layout_icard_staff, layout_calender_staff;
    private FloatingActionButton layout_help, layout_help_staff, lay_erp, lay_erp_staff, lay_gallery_staff, lay_birthdy_staff;
    private FloatingActionButton lay_more, lay_more_staff, lay_assessment, lay_assessment_staff,layout_examination_staff;
    public String version = "";
    public String minimumversion = "";
    private Splash splash = new Splash();
    ProgressDialog mProg;
    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProg = new ProgressDialog(MainActivity.this);
        btnLoginStudent = findViewById(R.id.btnLoginStudent);
        btnLoginTeacher = findViewById(R.id.btnLoginTeacher);
        if (InternetCheck.isInternetOn(MainActivity.this) == true) {
            get_app_details();
        } else {
            showsnackbar();
        }
        mainintent = getIntent();
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);

        ftoken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("alluser");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("student");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("teacher");

        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        //call function to open play store for app update
        btnLoginTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               stafflogin();
            }
        });

        btnLoginStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin();
            }
        });
        dialog = new Dialog(this);
        myDialog = new Dialog(this);
        myDialog2 = new Dialog(this);
        iCardDialog = new Dialog(this);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        Toolbar toolbar = findViewById(R.id.toolbar_dashboard);
        AppCompatButton logout_btn = findViewById(R.id.logout_btn);
        shownotice = findViewById(R.id.shownotice);
        LinearLayout discard = findViewById(R.id.discard);

        //Today's Notice Adapter
        recyclerView = findViewById(R.id.recycletodaynotice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Homepage Module's layout
        layout_attendance_staff = findViewById(R.id.layout_attendance_staff);
        layout_circular_staff = findViewById(R.id.layout_circular_staff);
        layout_registration_staff = findViewById(R.id.layout_registration_staff);
        layout_timetable_staff = findViewById(R.id.layout_timetable_staff);
        layout_examination_staff = findViewById(R.id.layout_examination_staff);

        lay_attendance = findViewById(R.id.layout_attendance);
        lay_feepayment = findViewById(R.id.layout_feepayment);
        lay_www = findViewById(R.id.layour_www);
        layour_www_staff = findViewById(R.id.layout_www_staff);
        lay_homework = findViewById(R.id.layour_homework);
        lay_profile = findViewById(R.id.layout_profile);
        //layout_leave = findViewById(R.id.layout_leave);
        lay_examination = findViewById(R.id.layout_examination);
        lay_activities = findViewById(R.id.layout_activity);
        lay_feereceipt = findViewById(R.id.layour_feereceipt);
        lay_alerts = findViewById(R.id.layout_alerts);
        lay_timetable = findViewById(R.id.layout_timetable);
        lay_cir = findViewById(R.id.layout_circular);
        lay_holidays = findViewById(R.id.layour_holidays);

        layout_circular_staff = findViewById(R.id.layout_circular_staff);
        layout_attendance_staff = findViewById(R.id.layout_attendance_staff);
        Lay_homework_staff = findViewById(R.id.layour_homework_staff);
        lay_profile_staff = findViewById(R.id.layout_profile_staff);
        main_lay_user = findViewById(R.id.main_layout_student);
        main_lay_staff = findViewById(R.id.main_layout_staff);
        lay_add_activities = findViewById(R.id.layout_activities_staff);
        lay_add_alerts = findViewById(R.id.layour_alerts_staff);
        lay_home = findViewById(R.id.HomeLayout);
        lay_settings = findViewById(R.id.SettingsLayout);
        lay_studymaterial = findViewById(R.id.layout_studymaterial);
        lay_gallery = findViewById(R.id.layout_Gallery);
        lay_registration = findViewById(R.id.layout_registration);
        lay_report_card = findViewById(R.id.layout_report_card);
        lay_birthday = findViewById(R.id.layout_birthday);
        lay_feedback = findViewById(R.id.layout_feedback);
        lay_liveclass_staff = findViewById(R.id.layout_Liveclass_staff);
        lay_liveclass_stud = findViewById(R.id.layout_Liveclass);
        layout_LiveclassStaff1 = findViewById(R.id.layout_LiveclassStaff1);
        layout_Liveclass1 = findViewById(R.id.layout_Liveclass1);
        lay_studymaterial_staff = findViewById(R.id.layout_studymaterial_staff);
        layout_calender_staff = findViewById(R.id.layout_calender_staff);
        layout_icard_staff = findViewById(R.id.layout_icard_staff);
        layout_help = findViewById(R.id.layout_help);
        layout_help_staff = findViewById(R.id.layout_help_staff);
        lay_erp = findViewById(R.id.layout_erp);
        lay_erp_staff = findViewById(R.id.layout_erp_staff);
        lay_gallery_staff = findViewById(R.id.layout_Gallery_staff);
        lay_birthdy_staff = findViewById(R.id.layout_birthday_staff);
        lay_more = findViewById(R.id.layout_more);
        lay_more_staff = findViewById(R.id.layout_more_staff);
        lay_assessment = findViewById(R.id.layout_assesment);
        lay_assessment_staff = findViewById(R.id.layout_assesment_staff);
        tvHelp = findViewById(R.id.tvHelp);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvVersion = findViewById(R.id.tvVersion);
        imgFeedback = findViewById(R.id.imgFeedback);
        imgHelp = findViewById(R.id.imgHelp);
        tvVersion.setText(BuildConfig.VERSION_NAME);
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HelpWebActivity.class));
            }
        });
        imgFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EnquiryActivity.class));
            }
        });
        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HelpWebActivity.class));
            }
        });
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EnquiryActivity.class));
            }
        });
        Log.d(TAG, " intent value:" + mainintent.getStringExtra("usertype"));
        if (InternetCheck.isInternetOn(MainActivity.this) == true) {
            if (mainintent.getStringExtra("usertype") != null && mainintent.getStringExtra("usertype").equals("staff")) {
                checkactiveteacher();
                Log.v("iliketest", "1");
                Staff staff = SharedPrefManager2.getInstance(this).getStaff();
                toolbar.setTitle("Hi, " + staff.getName());
                toolbar.setSubtitle("Welcome to " + getResources().getString(R.string.app_name_main));
                String user_ty = staff.getImgpath();
                if (!user_ty.equals("student")) {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.menu__staffbottom_navigation_basic);
                    main_lay_user.setVisibility(View.GONE);
                    main_lay_staff.setVisibility(View.VISIBLE);
                    NoticeStaff();
                    userloginCheck();
                }
                FirebaseMessaging.getInstance().subscribeToTopic("teacher");
            } else {
                User user = SharedPrefManager.getInstance(this).getUser();
                toolbar.setTitle("Hi, " + user.getName());
                toolbar.setSubtitle("Welcome to " + getResources().getString(R.string.app_name_main));
                getCheckPhoto();
                NoticeStudent();
                staffloginCheck();
                getPendingFeeStatus();
                checkactiveuser();
                FirebaseMessaging.getInstance().subscribeToTopic("student");
            }
        } else {
            showsnackbar();
        }

        layout_examination_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ExaminationStaffActivity.class));

            }
        });
        //sliderview
        if (InternetCheck.isInternetOn(MainActivity.this) == true) {
            mProg.show();
            final SliderView sliderView = findViewById(R.id.imageSlider);
            Call<BannerResponse> call2 = RetrofitClient.getInstance().getApi().getAllBanner();

            call2.enqueue(new Callback<BannerResponse>() {
                @Override
                public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {

                    mProg.dismiss();
                    if (response.body() != null && response.isSuccessful()) {
                        if (!response.body().isError()) {
                            bannerList = response.body().getBanner();
                            SliderAdapterExample adapter = new SliderAdapterExample(MainActivity.this, bannerList);
                            sliderView.setSliderAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<BannerResponse> call, Throwable t) {
                    mProg.dismiss();
                }
            });
            sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
            sliderView.startAutoCycle();
        } else {
            showsnackbar();
        }

        shownotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    shownotice.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_keyboard_arrow_right_white_24dp));
                    recyclerView.setVisibility(View.GONE);
                } else {
                    shownotice.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_keyboard_arrow_down_white_24dp));
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        // OnClickListeners
        lay_holidays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TimeTableActivity.class));
            }
        });

        lay_cir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CircularsActivity.class));
            }
        });
        layout_circular_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CircularsActivity.class));
            }
        });
        lay_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TimeTableActivity.class));
            }
        });
        layout_timetable_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TimeTableStaffActivity.class));
            }
        });
        layout_attendance_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AttendanceStaff.class));
            }
        });


        lay_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EnquiryActivity.class));
            }
        });
        lay_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationErpActivity.class));
            }
        });
        layout_registration_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationErpActivity.class));
            }
        });
        lay_report_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ReportCardActivity.class));
                // Snackbar.make(view, "Not Available Right now ! Please try again after next update", Snackbar.LENGTH_SHORT).show();
            }
        });
        lay_studymaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StudyMaterialActivity.class));
                //Snackbar.make(view, "Not Available Right now ! Please try again after next update", Snackbar.LENGTH_SHORT).show();
            }
        });
        lay_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,BirthdayActivity.class));
            }
        });
        lay_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,GalleryActivity.class));
               // Snackbar.make(view, "Not Available Right now ! Please try again after next update", Snackbar.LENGTH_SHORT).show();
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopuplogout();
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lay_www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, www.class));
            }
        });
        layour_www_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, www.class));

            }
        });
        lay_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
               intent.putExtra("photo_status",String.valueOf(status));
               startActivity(intent);
            }
        });
        lay_homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Assignment.class));

            }
        });
        lay_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Attendance.class));

            }
        });
        lay_feepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, feepayment.class);
                intent.putExtra("type", "feestructure");
                startActivity(intent);
            }
        });
        lay_feereceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, feepayment.class);
                intent.putExtra("type", "receipt");
                startActivity(intent);
            }
        });
        lay_examination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Examination.class));

            }
        });
        lay_activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Activities.class));

            }
        });
        lay_add_alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, AlertsStaffActivity.class));
            }
        });
        lay_add_activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, ActivitiesStaff.class));

            }
        });

        lay_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MoreActivity.class));
            }
        });
        lay_more_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MorestaffActivity.class));
            }
        });
        lay_assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AssesmentStudentActivity.class));
            }
        });
        lay_assessment_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AssessmentstaffActivity.class));
            }
        });
        lay_alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetCheck.isInternetOn(MainActivity.this) == true) {
                    User user = SharedPrefManager.getInstance(MainActivity.this).getUser();
                    Call<AlertsResponse> call = RetrofitClient.getInstance().getApi().getAlerts(user.getId());
                    call.enqueue(new Callback<AlertsResponse>() {
                        @Override
                        public void onResponse(Call<AlertsResponse> call, Response<AlertsResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (!response.body().isError()) {
                                    startActivity(new Intent(MainActivity.this, AlertsActivity.class));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AlertsResponse> call, Throwable t) {

                        }
                    });
                } else {
                    showsnackbar();
                }
            }
        });

        lay_profile_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivityStaff.class));
            }
        });
        lay_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, calender.class));
            }
        });
        layout_calender_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, calender.class));
            }
        });
        Lay_homework_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AssignmentStaffView.class));
            }
        });
        lay_studymaterial_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StudyStaff1Activity.class));
            }
        });
        lay_liveclass_stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LiveClasssActivity.class));
            }
        });
        lay_liveclass_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LiveClassstaffActivity.class));
            }
        });
        layout_Liveclass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LiveClassStudent1Activity.class));
            }
        });
        layout_LiveclassStaff1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LiveClassStaff1Activity.class));
            }
        });
        layout_icard_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, IcardStaffActivity.class));
            }
        });
        layout_help_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HelpWebActivity.class));
            }
        });
        layout_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HelpWebActivity.class));
            }
        });

        lay_erp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ErpActivity.class));
            }
        });
        lay_erp_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ErpStaffActivity.class));
            }
        });
        lay_gallery_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,GalleryActivity.class));
               // Snackbar.make(view, "Not Available Right now ! Please try again after next update", Snackbar.LENGTH_SHORT).show();
            }
        });
        lay_birthdy_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,BirthdayActivity.class));
            }
        });
        setbottomview();

        User user = SharedPrefManager.getInstance(this).getUser();

        Call<ProfileResponse> call3 = RetrofitClient.getInstance().getApi().getUserProfile(user.getId());
        call3.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (!response.body().isError()) {
                        ProfileResponse profileResponse = response.body();
                      /*  if (mainintent.getStringExtra("usertype") != null && !mainintent.getStringExtra("usertype").equals("staff")) {
                            final String imgeurl = BASE_URL2 + "office_admin/maintenance_image/student_photos/" + profileResponse.getUserprofile().getPhoto();
                            Glide.with(MainActivity.this).load(imgeurl).into(studnetimg);
                        }*/
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

            }
        });
        /*if (mainintent.getStringExtra("usertype") != null && mainintent.getStringExtra("usertype").equals("staff")) {
            Staff staff = SharedPrefManager2.getInstance(this).getStaff();
            toolbar.setTitle("Hi, " + staff.getName());
             String imgeurl2 = BASE_URL2 + "office_admin/maintenance_image/staff/" + staff.getUser_type();
             Glide.with(MainActivity.this).load(imgeurl2).into(studnetimg);
        }*/
        if (InternetCheck.isInternetOn(MainActivity.this) == true) {
            gettoken();
        } else {
            showsnackbar();
        }
    }

    private void checkactiveuser() {
        User user = SharedPrefManager.getInstance(this).getUser();
        Call<ErrorResponse> call = RetrofitClient.getInstance().getApi().checkactiveuser(user.getId());
        call.enqueue(new Callback<ErrorResponse>() {
            @Override
            public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                if (response.isSuccessful() && response != null) {
                    if (response.body().isError()) {
                        Toast.makeText(MainActivity.this, "Your Account has been Suspended ! Please contact Administrator", Toast.LENGTH_SHORT).show();
                        logout();
                    }
                }
            }

            @Override
            public void onFailure(Call<ErrorResponse> call, Throwable t) {

            }
        });
    }

    private void checkactiveteacher() {
        Staff staff = SharedPrefManager2.getInstance(this).getStaff();
        Call<ErrorResponse> call = RetrofitClient.getInstance().getApi().checkactivestaff(staff.getId());
        call.enqueue(new Callback<ErrorResponse>() {
            @Override
            public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                if (response.isSuccessful() && response != null) {
                    if (response.body().isError()) {
                        Toast.makeText(MainActivity.this, "Your Account has been Suspended ! Please contact Administrator", Toast.LENGTH_SHORT).show();
                        logout();
                    }
                }
            }

            @Override
            public void onFailure(Call<ErrorResponse> call, Throwable t) {

            }
        });
    }

    private void logout() {
        Log.v("iliketest", "2");
        SharedPrefManager.getInstance(MainActivity.this).clear();
        SharedPrefManager2.getInstance(MainActivity.this).clear();
        startActivity(new Intent(MainActivity.this, Splash.class));
    }

    private void gettoken() {
        Log.d("Refreshed", "Refreshed token: " + ftoken);
        String id = "";
        String by = "";

        if (mainintent.getStringExtra("usertype") != null && mainintent.getStringExtra("usertype").equals("staff")) {
            Staff staff = SharedPrefManager2.getInstance(this).getStaff();
            String user_ty = staff.getImgpath();
            if (!user_ty.equals("student")) {
                by = "22";
                id = staff.getId();
            }
        } else {
            User user = SharedPrefManager.getInstance(this).getUser();
            by = "11";
            id = user.getId();
        }
        Call<ErrorResponse> call = RetrofitClient.getInstance().getApi().addtokan(ftoken, id, by);
        call.enqueue(new Callback<ErrorResponse>() {
            @Override
            public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (!response.body().isError()) {
                        Log.v("token:", "added Succesfully");
                    }
                }
            }

            @Override
            public void onFailure(Call<ErrorResponse> call, Throwable t) {

            }
        });
    }

    private void showCustomDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.todaynoticedialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final RecyclerView recyclerView2 = dialog.findViewById(R.id.dialogrecycletodaynotice);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        alltodaynoticeadapter adpter = new alltodaynoticeadapter(MainActivity.this, todayNallList);
        recyclerView2.setAdapter(adpter);

        ImageButton imageButton = dialog.findViewById(R.id.bt_close);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        AppCompatButton appCompatButton = dialog.findViewById(R.id.bt_follow);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showCustomDialogStaff() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.todaynoticedialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final RecyclerView recyclerView2 = dialog.findViewById(R.id.dialogrecycletodaynotice);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        alltodaynoticeStaffadapter adpter = new alltodaynoticeStaffadapter(MainActivity.this, todayNoticeStaffList);
        recyclerView2.setAdapter(adpter);

        ImageButton imageButton = dialog.findViewById(R.id.bt_close);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        AppCompatButton appCompatButton = dialog.findViewById(R.id.bt_follow);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    public void ShowPopup(String title, String notice) {
        myDialog.setContentView(R.layout.noticepopup);
        TextView notice_title = myDialog.findViewById(R.id.title_notice);
        TextView notice_desc = myDialog.findViewById(R.id.desc_notice);
        notice_title.setText(title);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            notice_desc.setText(Html.fromHtml(notice, Html.FROM_HTML_MODE_LEGACY));
            notice_desc.setClickable(true);
            notice_desc.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            notice_desc.setText(Html.fromHtml(notice));
            notice_desc.setClickable(true);
            notice_desc.setMovementMethod(LinkMovementMethod.getInstance());
        }

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.closeOptionsMenu();
        myDialog.show();
    }

    public void ShowPopuplogout() {
        myDialog2.setContentView(R.layout.logoutpopup);
        Button yes = myDialog2.findViewById(R.id.yes_logout);
        Button no = myDialog2.findViewById(R.id.no_logout);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(MainActivity.this).clear();
                SharedPrefManager2.getInstance(MainActivity.this).clear();
                startActivity(new Intent(MainActivity.this, Splash.class));
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
            }
        });
        Objects.requireNonNull(myDialog2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.closeOptionsMenu();
        myDialog2.show();
    }

    private class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

        private Context context;
        private List<Banner> bannerList;

        SliderAdapterExample(Context context, List<Banner> bannerList) {
            this.context = context;
            this.bannerList = bannerList;
        }

        public SliderAdapterExample(Context context) {
            this.context = context;
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

            Banner banner = bannerList.get(position);
            Glide.with(viewHolder.itemView)
                    .load(banner.getImg())
                    .into(viewHolder.imageViewBackground);

        }

        @Override
        public int getCount() {

            return bannerList.size();
        }

        class SliderAdapterVH extends  SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;
            TextView textViewDescription;

            SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
                this.itemView = itemView;
            }
        }
    }

    private class alltodaynoticeadapter extends RecyclerView.Adapter<alltodaynoticeadapter.alltodaynoticeholder> {
        private Context mCtx;
        private List<TodayNall> todayNallList;

        public Context getmCtx() {
            return mCtx;
        }

        public List<TodayNall> getTodayNallList() {
            return todayNallList;
        }

        alltodaynoticeadapter(Context mCtx, List<TodayNall> todayNallList) {
            this.mCtx = mCtx;
            this.todayNallList = todayNallList;
        }

        @NonNull
        @Override
        public alltodaynoticeholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_notice_of_today, viewGroup, false);
            return new alltodaynoticeholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull alltodaynoticeholder alltodaynoticeholder, int i) {
            final TodayNall todayNall = todayNallList.get(i);
            alltodaynoticeholder.textView.setText(todayNall.getTitle());
            alltodaynoticeholder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<noticeresponse> call = RetrofitClient
                            .getInstance().getApi().getTodayNotice(todayNall.getId());
                    call.enqueue(new Callback<noticeresponse>() {
                        @Override
                        public void onResponse(Call<noticeresponse> call, Response<noticeresponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (!response.body().isError()) {
                                    noticeresponse noticeresponses = response.body();
                                    String title = noticeresponses.getNotice().getToday_title();
                                    String notice = noticeresponses.getNotice().getToday_notice();
                                    ShowPopup(title, notice);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<noticeresponse> call, Throwable t) {

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return todayNallList.size();
        }


        class alltodaynoticeholder extends RecyclerView.ViewHolder {
            TextView textView;
            LinearLayout linearLayout;

            alltodaynoticeholder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.notice_title);
                linearLayout = itemView.findViewById(R.id.notice_title_layout);
            }
        }
    }
    private class alltodaynoticeStaffadapter extends RecyclerView.Adapter<alltodaynoticeStaffadapter.alltodaynoticeholder> {
        private Context mCtx;
        private List<GetAllTodayNoticeStaff> todayNallList;

        public Context getmCtx() {
            return mCtx;
        }

        public List<GetAllTodayNoticeStaff> getTodayNallList() {
            return todayNallList;
        }

        alltodaynoticeStaffadapter(Context mCtx, List<GetAllTodayNoticeStaff> todayNallList) {
            this.mCtx = mCtx;
            this.todayNallList = todayNallList;
        }

        @NonNull
        @Override
        public alltodaynoticeholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_notice_of_today, viewGroup, false);
            return new alltodaynoticeholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull alltodaynoticeholder alltodaynoticeholder, int i) {
            final GetAllTodayNoticeStaff todayNall = todayNallList.get(i);
            alltodaynoticeholder.textView.setText(todayNall.getTitle());
            alltodaynoticeholder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<noticeresponse> call = RetrofitClient
                            .getInstance().getApi().getTodayNotice(String.valueOf(todayNall.getId()));
                    call.enqueue(new Callback<noticeresponse>() {
                        @Override
                        public void onResponse(Call<noticeresponse> call, Response<noticeresponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (!response.body().isError()) {
                                    noticeresponse noticeresponses = response.body();
                                    String title = noticeresponses.getNotice().getToday_title();
                                    String notice = noticeresponses.getNotice().getToday_notice();
                                    ShowPopup(title, notice);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<noticeresponse> call, Throwable t) {

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return todayNallList.size();
        }


        class alltodaynoticeholder extends RecyclerView.ViewHolder {
            TextView textView;
            LinearLayout linearLayout;

            alltodaynoticeholder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.notice_title);
                linearLayout = itemView.findViewById(R.id.notice_title_layout);
            }
        }
    }
    private void setbottomview() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_nearby:
                        startActivity(new Intent(MainActivity.this, loginByUser.class));
                        return true;
                    case R.id.navigation_home:
                        lay_home.setVisibility(View.VISIBLE);
                        lay_settings.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_settings:
                        lay_home.setVisibility(View.GONE);
                        lay_settings.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.navigation_share1:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage = "\nHi," + getResources().getString(R.string.app_name_main) +
                                    " has now digitalized its many operations and launched its own Mobile Application " +
                                    getResources().getString(R.string.my_website_link)
                                    + "to bring all stakeholders (Parents, Teachers, Students) on single platform.\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            Log.d(TAG, e.toString());
                        }
                        return true;
                }
                return true;
            }
        });
    }

    private void getCheckPhoto() {
        if (InternetCheck.isInternetOn(MainActivity.this) == true) {
            User user = SharedPrefManager.getInstance(MainActivity.this).getUser();
            Log.d(TAG , user.getId());
            Call<IcardCheckResponse> call = RetrofitClient
                    .getInstance().getApi().get_photo_check(user.getId());
           /* Call<IcardCheckResponse> call = RetrofitClient
                    .getInstance().getApi().get_photo_check("935");*/
            call.enqueue(new Callback<IcardCheckResponse>() {
                @Override
                public void onResponse(Call<IcardCheckResponse> call, Response<IcardCheckResponse> response) {
                    if(response.body().error == false){
                        mProg.dismiss();
                        status = response.body().getPhoto_check().getPhoto_editable_stud();
                        if(response.body().getPhoto_check().getPhoto_editable_stud() == 1)
                        {
                            iCardDialog.setContentView(R.layout.dialog_icard);
                            TextView message =  iCardDialog.findViewById(R.id.tvMessage);
                            Button btnUploadNow =  iCardDialog.findViewById(R.id.btnUploadNow);
                            Button btnUploadLater = iCardDialog.findViewById(R.id.btnUploadLater);
                            message.setText("Your Profile Picture is not uploaded yet.Please upload it.");
                            btnUploadNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    iCardDialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, IcardActivity.class);
                                    intent.putExtra("info", user.getId());
                                    intent.putExtra("type","student");
                                    startActivity(intent);
                                }
                            });
                            btnUploadLater.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    iCardDialog.dismiss();
                                }
                            });
                            Objects.requireNonNull( iCardDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            iCardDialog.closeOptionsMenu();
                            iCardDialog.show();

                        }
                    }else {
                        mProg.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<IcardCheckResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            showsnackbar();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SharedPrefManager.getInstance(this).isloggedin()) {
            if (!SharedPrefManager2.getInstance(this).isloggedin()) {
                Intent intent = new Intent(MainActivity.this, Splash.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, This App is no longer usable now.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void getPendingFeeStatus() {
        User user = SharedPrefManager.getInstance(MainActivity.this).getUser();
        Log.d(TAG , user.getId());
        // Call<PendingFeeResponse> call3 = RetrofitClient.getInstance().getApi().getpendingfeeResponse("2");
        Call<PendingFeeResponse> call3 = RetrofitClient.getInstance().getApi().getpendingfeeResponse(user.getId());
        call3.enqueue(new Callback<PendingFeeResponse>() {
            @Override
            public void onResponse(Call<PendingFeeResponse> call, Response<PendingFeeResponse> response) {
                if (response.body() != null) {
                    if (response.body().getError() == false) {
                        if (response.body().getFeeForcePay().equals("1")) {
                            Log.d(TAG, "getFeeForcePay: " + response.body().getFeeForcePay());
                            builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle1);
                            //Setting message manually and performing action on button click
                            builder.setMessage(response.body().getPendingfeeAlert())
                                    .setCancelable(false)
                                    .setPositiveButton("Pay Fee", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Log.d(TAG, "Pay Fee Now");
                                            Intent intent = new Intent(MainActivity.this, feepayment.class);
                                            intent.putExtra("type", "feestructure");
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Pending Fee Alert");
                            alert.show();
                        } else if (response.body().getFeeForcePay().equals("0")) {
                            Log.d(TAG, "getFeeForcePay: " + response.body().getFeeForcePay());
                            builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
                            //Setting message manually and performing action on button click
                            builder.setMessage(response.body().getPendingfeeAlert())
                                    .setCancelable(false)
                                    .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(MainActivity.this, feepayment.class);
                                            intent.putExtra("type", "feestructure");
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Pay Later", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                            dialog.cancel();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Pending Fee Alert");
                            alert.show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PendingFeeResponse> call, Throwable t) {

            }
        });
    }

    private void getAppVersion() {
        if (!minimumversion.equals("") && current_version < Integer.parseInt(minimumversion)) {
            Log.d(TAG, "Current version: " + current_version + " Updated version " + version);
            builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
            //Setting message manually and performing action on button click
            builder.setMessage(getResources().getString(R.string.app_name_main) + " recommends that you update to latest version.")
                    .setCancelable(false)
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(TAG, "Update");
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                            finish();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Update " + getResources().getString(R.string.app_name_main) + "?");
            alert.show();
        } else if (!version.equals("") && current_version < Integer.parseInt(version)) {
            Log.d(TAG, "Current version: " + current_version + " Updated version " + version);
            builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
            //Setting message manually and performing action on button click
            builder.setMessage(getResources().getString(R.string.app_name_main) + " recommends that you update to latest version.")
                    .setCancelable(false)
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(TAG, "Update");
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                            finish();
                        }
                    })
                    .setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Update " + getResources().getString(R.string.app_name_main) + "?");
            alert.show();
        } }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.dismiss();
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
                        if (InternetCheck.isInternetOn(MainActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void get_app_details() {
        Call<MaintenanceResponse> call3 = RetrofitClient.getInstance().getApi().getappcheck();
        call3.enqueue(new Callback<MaintenanceResponse>() {
            @Override
            public void onResponse(Call<MaintenanceResponse> call, Response<MaintenanceResponse> response) {
                if (response.body() != null) {
                    if (response.body().getError() == false) {
                        Log.d(TAG, response.body().getCheck().getVersion());
                        //maintenance = response.body().getCheck().getMaintenace();
                        version = response.body().getCheck().getVersion();
                        // version = "27";
                        //message = response.body().getCheck().getMMessage();
                        //announcement = response.body().getCheck().getAnnouncement();
                        current_version = BuildConfig.VERSION_CODE;
                        minimumversion = response.body().getCheck().getMin_version() +"";
                        //minimumversion = "27";
                        Log.d(TAG, version + " " + current_version + " current "+minimumversion);
                        if (InternetCheck.isInternetOn(MainActivity.this) == true) {
                            getAppVersion();
                        } else {
                            showsnackbar();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MaintenanceResponse> call, Throwable t) {

            }
        });
    }
    private void userlogin() {
        mProg.show();
        Staff staff = SharedPrefManager2.getInstance(this).getStaff();
        if(staff.getPhone_number() != null) {
            Call<LoginResponse> call = RetrofitClient
                    .getInstance().getApi().userlogin(staff.getPhone_number());
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    //Log.d(TAG, "response: " + response.body().getUser());
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        if (!loginResponse.isError()) {
                            SharedPrefManagerGuest.getInstance(MainActivity.this).clear();
                            SharedPrefManager.getInstance(MainActivity.this).clear();
                            SharedPrefManager2.getInstance(MainActivity.this).clear();
                            mProg.dismiss();
                            SharedPrefManager.getInstance(MainActivity.this)
                                    .saveUser(loginResponse.getUser());
                            Intent intent = new Intent(MainActivity.this, loginByUser.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            mProg.dismiss();
                            Toast.makeText(MainActivity.this, "This Mobile number is not registered with No students, Please contact School for more details.", Toast.LENGTH_SHORT).show();
                       /* SharedPrefManagerGuest.getInstance(loginActivity.this)
                                .saveUser(loginResponse.getPhone());
                        startActivity(new Intent(loginActivity.this, GuestMainActivity.class));*/
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    mProg.dismiss();
                    Log.d("error; ", " " + t);
                }
            });
        }
    }

    private void stafflogin() {
       User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
       if(user.getPhone_number() != null) {
           Call<staffloginresponse> call = RetrofitClient.getInstance().getApi().staffloginbyphone(user.getPhone_number());
           call.enqueue(new Callback<staffloginresponse>() {
               @Override
               public void onResponse(Call<staffloginresponse> call, Response<staffloginresponse> response) {
                   if (response.isSuccessful() && response.body() != null) {
                       staffloginresponse staffloginresponses = response.body();
                       if (!staffloginresponses.isError()) {
                           SharedPrefManagerGuest.getInstance(MainActivity.this).clear();
                           SharedPrefManager.getInstance(MainActivity.this).clear();
                           SharedPrefManager2.getInstance(MainActivity.this).clear();
                           mProg.dismiss();
                           SharedPrefManager2.getInstance(MainActivity.this)
                                   .saveStaff(staffloginresponses.getStaff());
                           Intent intent = new Intent(MainActivity.this, MainActivity.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           intent.putExtra("user_type", mainintent.getStringExtra("user_type"));
                           intent.putExtra("usertype", "staff");
                           startActivity(intent);
                           finish();
                       } else {
                           mProg.dismiss();
                           Toast.makeText(MainActivity.this, "This Mobile number is not registered , Please contact School for more details.", Toast.LENGTH_SHORT).show();
                           // startActivity(new Intent(loginActivity.this, GuestMainActivity.class));
                       }
                   }
               }

               @Override
               public void onFailure(Call<staffloginresponse> call, Throwable t) {
                   mProg.dismiss();
               }
           });
       }
    }

    private void userloginCheck() {
        Staff staff = SharedPrefManager2.getInstance(this).getStaff();
        if(staff.getPhone_number() != null) {
            Call<LoginResponse> call = RetrofitClient
                    .getInstance().getApi().userlogin(staff.getPhone_number());
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    //Log.d(TAG, "response: " + response.body().getUser());
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        if (!loginResponse.isError()) {
                            Log.d(TAG, "student yes");
                            btnLoginStudent.setVisibility(View.VISIBLE);
                            btnLoginTeacher.setVisibility(View.GONE);

                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                }
            });
        }
    }

    private void staffloginCheck() {
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        if(user.getPhone_number() != null) {
            Call<staffloginresponse> call = RetrofitClient.getInstance().getApi().staffloginbyphone(user.getPhone_number());
            call.enqueue(new Callback<staffloginresponse>() {
                @Override
                public void onResponse(Call<staffloginresponse> call, Response<staffloginresponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        staffloginresponse staffloginresponses = response.body();
                        if (!staffloginresponses.isError()) {
                            Log.d(TAG, "staff yes");
                            btnLoginTeacher.setVisibility(View.VISIBLE);
                            btnLoginStudent.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<staffloginresponse> call, Throwable t) {

                }
            });
        }
    }
    private void NoticeStudent()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Call<AllTodayNoticeResponse> call = RetrofitClient.getInstance().getApi().getAlltodayNotice(date);
        call.enqueue(new Callback<AllTodayNoticeResponse>() {
            @Override
            public void onResponse(Call<AllTodayNoticeResponse> call, Response<AllTodayNoticeResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (!response.body().isError()) {
                        todayNallList = response.body().getGet_all_today_notice();
                        alltodaynoticeadapter adpter = new alltodaynoticeadapter(MainActivity.this, todayNallList);
                        recyclerView.setAdapter(adpter);
                        if (todayNallList.size() != 0) {
                            showCustomDialog();
                        }
                    } else {
                        showsnackbar();
                    }
                }
            }

            @Override
            public void onFailure(Call<AllTodayNoticeResponse> call, Throwable t) {
                showsnackbar();
            }
        });
    }
    private void NoticeStaff()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Call<NoticeStaffResponse> call = RetrofitClient.getInstance().getApi().getalltodaynotice_staff(date);
        call.enqueue(new Callback<NoticeStaffResponse>() {
            @Override
            public void onResponse(Call<NoticeStaffResponse> call, Response<NoticeStaffResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getError() == false) {
                        todayNoticeStaffList = response.body().getGetAllTodayNoticeStaff();
                        alltodaynoticeStaffadapter adpter = new alltodaynoticeStaffadapter(MainActivity.this, todayNoticeStaffList);
                        recyclerView.setAdapter(adpter);
                        if (todayNoticeStaffList.size() != 0) {
                            showCustomDialogStaff();
                        }
                    } else {
                        showsnackbar();
                    }
                }
            }

            @Override
            public void onFailure(Call<NoticeStaffResponse> call, Throwable t) {
                showsnackbar();
            }
        });
    }
}
