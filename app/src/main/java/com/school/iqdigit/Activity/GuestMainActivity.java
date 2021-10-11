package com.school.iqdigit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.school.iqdigit.BuildConfig;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.BannerResponse;
import com.school.iqdigit.Model.MaintenanceResponse;
import com.school.iqdigit.Modeldata.Banner;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.Storage.SharedPrefManager2;
import com.school.iqdigit.Storage.SharedPrefManagerGuest;
import com.school.iqdigit.utility.InternetCheck;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestMainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Banner> bannerList;
    private FloatingActionButton layout_enquiry, layout_registration, layout_job_apply, layout_fee_structure, layout_achivement, layout_website;
    private ProgressDialog mProg;
    private String TAG = "GuestMainActivity";
    private String phone_school = "";
    private RelativeLayout rl_calnow;
    private static final int PERMISSION_CALL_PHONE = 1;
    private BottomNavigationView navigationView;
    private LinearLayout lay_home, lay_settings, discard;
    private TextView tvIqdigit;
    private Dialog myDialog2;
    private AppUpdateManager mAppUpdateManager;
    private int current_version;
    AlertDialog.Builder builder;
    public String version = "";
    private Intent mainintent;
    private Toolbar toolbar;
    private TextView tvVersion, tvHelp, tvFeedback;
    private ImageView imgHelp, imgFeedback;
    public String minimumversion = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);
        toolbar = findViewById(R.id.toolbar_dashboard);
        toolbar.inflateMenu(R.menu.menu_bottom_navigation_guest_logout);
        mainintent = getIntent();
        if (InternetCheck.isInternetOn(GuestMainActivity.this) == true) {
            get_app_details();
        } else {
            showsnackbar();
        }
        // version = mainintent.getStringExtra("version");

        phone_school = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getResources().getString(R.string.call_phone), "");
        Log.d(TAG, phone_school + "phonenum");
        tvIqdigit = findViewById(R.id.tvIqdigit);
        tvIqdigit.setOnClickListener(this);
        myDialog2 = new Dialog(this);
        discard = findViewById(R.id.discard);
        discard.setOnClickListener(this);
        layout_enquiry = findViewById(R.id.layout_enquiry);
        layout_registration = findViewById(R.id.layout_registration);
        layout_achivement = findViewById(R.id.layout_achivement);
        layout_fee_structure = findViewById(R.id.layout_fee_structure);
        layout_job_apply = findViewById(R.id.layout_job_apply);
        layout_website = findViewById(R.id.layout_website);
        rl_calnow = findViewById(R.id.rl_callnow);
        lay_home = findViewById(R.id.HomeLayout);
        lay_settings = findViewById(R.id.SettingsLayout);
        lay_home.setOnClickListener(this);
        lay_settings.setOnClickListener(this);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation1);
        rl_calnow.setOnClickListener(this);
        layout_website.setOnClickListener(this);
        layout_enquiry.setOnClickListener(this);
        layout_job_apply.setOnClickListener(this);
        layout_fee_structure.setOnClickListener(this);
        layout_achivement.setOnClickListener(this);
        layout_registration.setOnClickListener(this);
        tvHelp = findViewById(R.id.tvHelp);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvVersion = findViewById(R.id.tvVersion);
        imgFeedback = findViewById(R.id.imgFeedback);
        imgHelp = findViewById(R.id.imgHelp);
        tvVersion.setText(BuildConfig.VERSION_NAME);
        imgHelp.setOnClickListener(this);
        imgFeedback.setOnClickListener(this);
        tvHelp.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.logout) {
                    ShowPopuplogout();
                }
                return false;
            }
        });
        mProg = new ProgressDialog(this);
        mProg.setTitle(R.string.app_name);
        mProg.setMessage("Loading...");
        mProg.show();
        setbottomview();

        if (InternetCheck.isInternetOn(GuestMainActivity.this) == true) {
            final SliderView sliderView = findViewById(R.id.imageSlider1);
            Call<BannerResponse> call2 = RetrofitClient.getInstance().getApi().getAllBanner();

            call2.enqueue(new Callback<BannerResponse>() {
                @Override
                public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {

                    if (response.body() != null && response.isSuccessful()) {
                        if (!response.body().isError()) {
                            mProg.dismiss();
                            bannerList = response.body().getBanner();
                            SliderAdapter adapter = new SliderAdapter(GuestMainActivity.this, bannerList);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_enquiry:
                startActivity(new Intent(GuestMainActivity.this, EnquiryActivity.class));
                break;
            case R.id.layout_registration:
                Intent intent = new Intent(GuestMainActivity.this, RegistrationErpActivity.class);
                intent.putExtra("guest", "guest");
                startActivity(intent);
                break;
            case R.id.layout_achivement:
                startActivity(new Intent(GuestMainActivity.this, AchievementActivity.class));
                break;
            case R.id.layout_job_apply:
                startActivity(new Intent(GuestMainActivity.this, JobApplyActivity.class));
                break;
            case R.id.layout_website:
                startActivity(new Intent(GuestMainActivity.this, www.class));
                break;
            case R.id.layout_fee_structure:
                startActivity(new Intent(GuestMainActivity.this, FeeStructureActivity1.class));
                break;
            case R.id.rl_callnow:
                if (phone_school != null && !phone_school.equalsIgnoreCase("null") && !phone_school.equalsIgnoreCase("") && phone_school.length() > 0) {

                    if (ActivityCompat.checkSelfPermission(GuestMainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // request permission (see result in onRequestPermissionsResult() method)
                        ActivityCompat.requestPermissions(GuestMainActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                PERMISSION_CALL_PHONE);
                    } else {
                        callnow(phone_school);
                    }
                }
                break;
            case R.id.tvIqdigit:
                startActivity(new Intent(GuestMainActivity.this, CompayWebsiteActivity.class));
                break;
            case R.id.discard:
                ShowPopuplogout();
                break;
            case R.id.imgHelp:
                startActivity(new Intent(GuestMainActivity.this, HelpWebActivity.class));
                break;
            case R.id.imgFeedback:
                startActivity(new Intent(GuestMainActivity.this, EnquiryActivity.class));
                break;
            case R.id.tvHelp:
                startActivity(new Intent(GuestMainActivity.this, HelpWebActivity.class));
                break;
            case R.id.tvFeedback:
                startActivity(new Intent(GuestMainActivity.this, EnquiryActivity.class));
                break;
        }
    }

    private class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVh> {
        private Context context;
        private List<Banner> bannerList;

        SliderAdapter(Context context, List<Banner> bannerList) {
            this.context = context;
            this.bannerList = bannerList;
        }

        public SliderAdapter(Context context) {
            this.context = context;
        }

        @Override
        public SliderAdapterVh onCreateViewHolder(ViewGroup parent) {
            @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVh(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVh viewHolder, int position) {

            Banner banner = bannerList.get(position);
            Glide.with(viewHolder.itemView)
                    .load(banner.getImg())
                    .into(viewHolder.imageViewBackground);

        }

        @Override
        public int getCount() {

            return bannerList.size();
        }

        class SliderAdapterVh extends  SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;
            TextView textViewDescription;

            SliderAdapterVh(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
                this.itemView = itemView;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CALL_PHONE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    callnow(phone_school);
                } else {
                    // permission denied
                }
                return;
            }
        }
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
                        if (InternetCheck.isInternetOn(GuestMainActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    private void callnow(String phoneNo) {
        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse("tel:" + phone_school));
        startActivity(intentCall);
    }

    private void setbottomview() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
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
                    case R.id.navigation_home1:
                        lay_home.setVisibility(View.VISIBLE);
                        lay_settings.setVisibility(View.GONE);
                        return true;
                    case R.id.navigation_settings1:
                        lay_home.setVisibility(View.GONE);
                        lay_settings.setVisibility(View.VISIBLE);
                        return true;
                }
                return true;
            }
        });
    }

    public void ShowPopuplogout() {
        myDialog2.setContentView(R.layout.logoutpopup);
        Button yes = myDialog2.findViewById(R.id.yes_logout);
        Button no = myDialog2.findViewById(R.id.no_logout);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManagerGuest.getInstance(GuestMainActivity.this).clear();
                startActivity(new Intent(GuestMainActivity.this, Splash.class));
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

    private void getAppVersion() {
        if (!minimumversion.equals("") && current_version < Integer.parseInt(minimumversion)) {
            Log.d(TAG, "Current version: " + current_version + " Updated version " + version);
            builder = new AlertDialog.Builder(GuestMainActivity.this, R.style.AlertDialogStyle);
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
            builder = new AlertDialog.Builder(GuestMainActivity.this, R.style.AlertDialogStyle);
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
                        //message = response.body().getCheck().getMMessage();
                        //announcement = response.body().getCheck().getAnnouncement();
                        current_version = BuildConfig.VERSION_CODE;
                        minimumversion = response.body().getCheck().getMin_version() +"";
                        Log.d(TAG, version + " " + current_version + " current");
                        if (InternetCheck.isInternetOn(GuestMainActivity.this) == true) {
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
}
