package com.school.iqdigit.Activity;
/*created by Kiran nanda @Date 01/28/2020*/
/*Activity will show Time Table*/

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.LiveTimetable;
import com.school.iqdigit.Model.LiveTimetableResponse;
import com.school.iqdigit.Model.ProfileResponse;
import com.school.iqdigit.Model.Timetable;
import com.school.iqdigit.Model.Timetableresponse;
import com.school.iqdigit.Modeldata.SpinnerItems;
import com.school.iqdigit.Modeldata.SpinnerItemsLive;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "TimeTableActivity";
    private String dayOfTheWeek = "",dayOfTheWeekLive = "";
    private List<Timetable> timetableList = new ArrayList<>();
    private List<LiveTimetable>  LiveTimetableList = new ArrayList<>();
    private ArrayList<LiveTimetable> LiveTimetableList1 = new ArrayList<>();

    private ArrayList<String> day = new ArrayList<>();
    private ArrayList<String> daylive = new ArrayList<>();
    private ArrayList<SpinnerItems> spinnerList = new ArrayList<>();
    private ArrayList<SpinnerItemsLive> spinnerLiveList = new ArrayList<>();
    private ArrayList<Timetable> timetableArrayList = new ArrayList<>();
    private ProgressDialog mProg;
    private RecyclerView rvtimetable,rvtimetablelive;
    private ImageView backbtn1;
    private Spinner spnTimeTable,spnTimeTableLive;
    private SpinAdapter adapter;
    private SpinLiveAdapter spinLiveAdapter;
    private String selectedday = "",selecteddayLive="";
    private int dayIndex,dayLiveIndex;
    private LinearLayout llNotAvailable,timetableheadlayout;
    private String classid;
    private TextView navigation_live_class, navigation_ragular;
    private LinearLayout view_live_class, view_ragular, ll_live, ll_Regular, timetableheadlayoutlive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        mProg = new ProgressDialog(this);
        rvtimetablelive = findViewById(R.id.rvtimetablelive);
        navigation_live_class = findViewById(R.id.navigation_live_class);
        navigation_live_class.setOnClickListener(this);
        navigation_ragular = findViewById(R.id.navigation_ragular);
        navigation_ragular.setOnClickListener(this);
        view_live_class = findViewById(R.id.view_live_class);
        view_ragular = findViewById(R.id.view_ragular);
        ll_live = findViewById(R.id.ll_live);
        ll_Regular = findViewById(R.id.ll_Regular);
        rvtimetable = (RecyclerView) findViewById(R.id.rvtimetable);
        rvtimetable.setLayoutManager(new LinearLayoutManager(this));
        rvtimetablelive.setLayoutManager(new LinearLayoutManager(this));
        spnTimeTable = (Spinner) findViewById(R.id.spnTimeTable);
        spnTimeTableLive = (Spinner) findViewById(R.id.spnTimeTableLive);
        timetableheadlayout = findViewById(R.id.timetableheadlayout);
        timetableheadlayoutlive = findViewById(R.id.timetableheadlayoutlive);
        backbtn1 = findViewById(R.id.backbtn1);
        llNotAvailable = (LinearLayout) findViewById(R.id.noTimeTableavailable);
        backbtn1.setOnClickListener(this);

        if (InternetCheck.isInternetOn(TimeTableActivity.this) == true) {
            getclassid();
            getSpinnerListLive();
            getSpinnerListRagular();
        } else {
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void getSpinnerListRagular() {
        if(spinnerList.size()>0)
        {
            spinnerList.clear();
        }
        if (day.size()>0)
        {
            day.clear();
        }
        llNotAvailable.setVisibility(View.GONE);
        timetableheadlayout.setVisibility(View.VISIBLE);
        rvtimetable.setVisibility(View.VISIBLE);
        getCurrentDay();
        spinnerList.add(new SpinnerItems("Monday", "m"));
        spinnerList.add(new SpinnerItems("Tuesday", "t"));
        spinnerList.add(new SpinnerItems("Wednesday", "w"));
        spinnerList.add(new SpinnerItems("Thursday", "th"));
        spinnerList.add(new SpinnerItems("Friday", "f"));
        spinnerList.add(new SpinnerItems("Saturday", "s"));
        day.add("Monday");
        day.add("Tuesday");
        day.add("Wednesday");
        day.add("Thursday");
        day.add("Friday");
        day.add("Saturday");
        Log.v(TAG, "spinnerList:" + spinnerList.size());
        adapter = new SpinAdapter(TimeTableActivity.this,
                android.R.layout.simple_list_item_1,
                spinnerList);
        spnTimeTable.setAdapter(adapter); // Set the custom adapter to the spinner
        int index = day.indexOf(dayOfTheWeek);
        if (index < 5) {
            dayIndex = (index + 1);
        } else {
            dayIndex = 0;
        }
        Log.d(TAG, "dayTomarrow:" + dayIndex);
        spnTimeTable.setSelection(dayIndex);

        selectedday = spinnerList.get(dayIndex).getReturnDay();
        if (InternetCheck.isInternetOn(TimeTableActivity.this) == true) {
            getTimeTable();
        } else {
            showsnackbar();
        }

        // You can create an anonymous listener to handle the event when is selected an spinner item
        spnTimeTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                final SpinnerItems spinnerItems = (SpinnerItems) spnTimeTable.getItemAtPosition(position);
                Log.d("SpinnerState", "onItemSelected: spinnerItems: " + spinnerItems.getReturnDay());
                selectedday = spinnerItems.getReturnDay();
                if (InternetCheck.isInternetOn(TimeTableActivity.this) == true) {
                    getTimeTable();
                } else {
                    showsnackbar();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void getTimeTable() {
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);
        mProg.show();
        User user = SharedPrefManager.getInstance(this).getUser();
        Log.d(TAG, "selectedday " + selectedday);
        Log.d(TAG, "user " + user.getId());
        Call<Timetableresponse> call = RetrofitClient.getInstance().getApi().getTimeTable(classid, selectedday);
        call.enqueue(new Callback<Timetableresponse>() {
            @Override
            public void onResponse(Call<Timetableresponse> call, Response<Timetableresponse> response) {
                timetableList.clear();
                timetableArrayList.clear();
                llNotAvailable.setVisibility(View.GONE);
                timetableheadlayout.setVisibility(View.VISIBLE);
                rvtimetable.setVisibility(View.VISIBLE);
                Log.d(TAG, response + " response");
                if (response.code() != 404) {
                    timetableList = response.body().getTimetable();
                    if (!(timetableList.size() == 0)) {
                        Log.d(TAG, "timetableList " + timetableList.size());
                        for (int i = 0; i < timetableList.size(); i++) {
                            timetableArrayList.add(new Timetable(timetableList.get(i).getSr(), timetableList.get(i).getSubject(), timetableList.get(i).getPtime()));
                        }
                        Log.d(TAG, "timetableArrayList " + timetableArrayList.size());
                        Timetablesadapter adapter = new Timetablesadapter(TimeTableActivity.this, timetableArrayList);
                        rvtimetable.setAdapter(adapter);
                    } else {
                        llNotAvailable.setVisibility(View.VISIBLE);
                        timetableheadlayout.setVisibility(View.GONE);
                        rvtimetable.setVisibility(View.GONE);
                }
                } else {
                    llNotAvailable.setVisibility(View.VISIBLE);
                    timetableheadlayout.setVisibility(View.GONE);
                    rvtimetable.setVisibility(View.GONE);
                }
                mProg.dismiss();
            }

            @Override
            public void onFailure(Call<Timetableresponse> call, Throwable t) {
                mProg.dismiss();
                Log.d(TAG, t.getMessage());
            }
        });

    }

    //live class timetable
    private void getSpinnerListLive() {
        if(spinnerLiveList.size()>0)
        {
            spinnerLiveList.clear();
        }
        if (daylive.size()>0)
        {
            daylive.clear();
        }
        llNotAvailable.setVisibility(View.GONE);
        timetableheadlayoutlive.setVisibility(View.VISIBLE);
        rvtimetable.setVisibility(View.VISIBLE);
        getCurrentDay();

        spinnerLiveList.add(new SpinnerItemsLive("Sunday","su"));
        spinnerLiveList.add(new SpinnerItemsLive("Monday", "m"));
        spinnerLiveList.add(new SpinnerItemsLive("Tuesday", "t"));
        spinnerLiveList.add(new SpinnerItemsLive("Wednesday", "w"));
        spinnerLiveList.add(new SpinnerItemsLive("Thursday", "th"));
        spinnerLiveList.add(new SpinnerItemsLive("Friday", "f"));
        spinnerLiveList.add(new SpinnerItemsLive("Saturday", "s"));

        daylive.add("Sunday");
        daylive.add("Monday");
        daylive.add("Tuesday");
        daylive.add("Wednesday");
        daylive.add("Thursday");
        daylive.add("Friday");
        daylive.add("Saturday");
        Log.v(TAG, "spinnerListLive:" + spinnerLiveList.size());
        spinLiveAdapter = new SpinLiveAdapter(TimeTableActivity.this,
                android.R.layout.simple_list_item_1,
                spinnerLiveList);
        spnTimeTableLive.setAdapter(spinLiveAdapter); // Set the custom adapter to the spinner
        int index = daylive.indexOf(dayOfTheWeek);
        if (index < 6) {
            dayLiveIndex = (index + 1);
        } else {
            dayLiveIndex = 0;
        }
        Log.d(TAG, "dayTomarrow:" + dayLiveIndex);
        spnTimeTableLive.setSelection(dayLiveIndex);

        selecteddayLive = spinnerLiveList.get(dayLiveIndex).getReturnDay();
        if (InternetCheck.isInternetOn(TimeTableActivity.this) == true) {
            getTimeTableLive();
        } else {
            showsnackbar();
        }

        // You can create an anonymous listener to handle the event when is selected an spinner item
        spnTimeTableLive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                final SpinnerItemsLive spinnerItems = (SpinnerItemsLive) spnTimeTableLive.getItemAtPosition(position);
                Log.d("SpinnerStateLive", "onItemSelected: SpinnerItemsLive: " + spinnerItems.getReturnDay());
                selecteddayLive = spinnerItems.getReturnDay();
                if (InternetCheck.isInternetOn(TimeTableActivity.this) == true) {
                    getTimeTableLive();
                } else {
                    showsnackbar();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void getTimeTableLive() {
        mProg.setMessage("Loading.....");
        mProg.setTitle(R.string.app_name_main);
        mProg.show();
        User user = SharedPrefManager.getInstance(this).getUser();
        Log.d(TAG, "selecteddayLive " + selecteddayLive);
        Log.d(TAG, "user " + user.getId());
        //Call<LiveTimetableResponse> call = RetrofitClient.getInstance().getApi().getLiveTimetableResponse("1", "m");
        Call<LiveTimetableResponse> call = RetrofitClient.getInstance().getApi().getLiveTimetableResponse(classid, selecteddayLive);
        call.enqueue(new Callback<LiveTimetableResponse>() {
            @Override
            public void onResponse(Call<LiveTimetableResponse> call, Response<LiveTimetableResponse> response) {
                if(LiveTimetableList.size() >0)
                LiveTimetableList.clear();
                if( LiveTimetableList1.size()>0)
                    LiveTimetableList1.clear();
                llNotAvailable.setVisibility(View.GONE);
                timetableheadlayoutlive.setVisibility(View.VISIBLE);
                rvtimetablelive.setVisibility(View.VISIBLE);
                Log.d(TAG, response + " response");
                if (response.body().getError() == false) {
                    LiveTimetableList = response.body().getLiveTimetable();
                    if (!(LiveTimetableList.size() == 0)) {
                        Log.d(TAG, "LiveTimetableList " + LiveTimetableList.size());
                        for (int i = 0; i < LiveTimetableList.size(); i++) {
                            LiveTimetableList1.add(new LiveTimetable(LiveTimetableList.get(i).getSubjectName(),LiveTimetableList.get(i).getStartTime()
                            ,LiveTimetableList.get(i).getEndTime(),LiveTimetableList.get(i).getRemarks(),LiveTimetableList.get(i).getTeacher()));
                        }
                        Log.d(TAG, "LiveTimetableList1 " + LiveTimetableList1.size());
                        TimetablesLiveadapter adapter = new TimetablesLiveadapter(TimeTableActivity.this, LiveTimetableList1);
                        rvtimetablelive.setAdapter(adapter);
                    } else {
                        llNotAvailable.setVisibility(View.VISIBLE);
                        timetableheadlayoutlive.setVisibility(View.GONE);
                        rvtimetablelive.setVisibility(View.GONE);
                    }
                } else {
                    llNotAvailable.setVisibility(View.VISIBLE);
                    timetableheadlayoutlive.setVisibility(View.GONE);
                    rvtimetablelive.setVisibility(View.GONE);
                }
                mProg.dismiss();
            }

            @Override
            public void onFailure(Call<LiveTimetableResponse> call, Throwable t) {
                mProg.dismiss();
                Log.d(TAG, t.getMessage());
            }
        });

    }
    //get current day from current date
    private void getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);
        Log.d(TAG, dayOfTheWeek + " dayOfTheWeek");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn1:
                onBackPressed();
                break;
            case R.id.navigation_live_class:
                getSpinnerListLive();
                ll_live.setVisibility(View.VISIBLE);
                ll_Regular.setVisibility(View.GONE);
                view_live_class.setVisibility(View.VISIBLE);
                view_ragular.setVisibility(View.GONE);
                break;
            case R.id.navigation_ragular:
                getSpinnerListRagular();
                ll_Regular.setVisibility(View.VISIBLE);
                ll_live.setVisibility(View.GONE);
                view_live_class.setVisibility(View.GONE);
                view_ragular.setVisibility(View.VISIBLE);
                break;
        }
    }

    //Timetable RecyclerView Adapter
    private class Timetablesadapter extends RecyclerView.Adapter<Timetablesadapter.Timetableholder> {
        private Context mCtx;
        private ArrayList<Timetable> timetableArrayList = new ArrayList<>();

        public Timetablesadapter(Context mCtx, ArrayList<Timetable> timetableArrayList) {
            this.mCtx = mCtx;
            this.timetableArrayList = timetableArrayList;
        }

        @NonNull
        @Override
        public Timetableholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_timetable, viewGroup, false);
            return new Timetableholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Timetableholder timetableholder, int i) {
            Timetable timetable = timetableArrayList.get(i);

            if (timetable.getSr().equals("")) {
                timetableholder.tvPr.setBackgroundColor(Color.parseColor("#37474F"));
                timetableholder.tvSubject.setBackgroundColor(Color.parseColor("#37474F"));
                timetableholder.tvTime.setBackgroundColor(Color.parseColor("#37474F"));
                timetableholder.tvPr.setTextColor(Color.parseColor("#FFFFFF"));
                timetableholder.tvSubject.setTextColor(Color.parseColor("#FFFFFF"));
                timetableholder.tvTime.setTextColor(Color.parseColor("#FFFFFF"));
                Log.v(TAG, timetable.getSr() + " break");
            }

            timetableholder.tvPr.setText(timetable.getSr());
            timetableholder.tvSubject.setText(timetable.getSubject());
            timetableholder.tvTime.setText(timetable.getPtime());
        }

        @Override
        public int getItemCount() {
            return timetableArrayList.size();
        }

        class Timetableholder extends RecyclerView.ViewHolder {
            TextView tvPr, tvTime, tvSubject;
            LinearLayout llTimeTable;

            public Timetableholder(@NonNull View itemView) {
                super(itemView);
                tvPr = itemView.findViewById(R.id.tvPer);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvSubject = itemView.findViewById(R.id.tvSubject);
                llTimeTable = itemView.findViewById(R.id.llTimeTable);
            }
        }
    }

    //Timetable RecyclerView Adapter
    private class TimetablesLiveadapter extends RecyclerView.Adapter<TimetablesLiveadapter.Timetableholder> {
        private Context mCtx;
        private ArrayList<LiveTimetable> timetableArrayList = new ArrayList<>();

        public TimetablesLiveadapter(Context mCtx, ArrayList<LiveTimetable> timetableArrayList) {
            this.mCtx = mCtx;
            this.timetableArrayList = timetableArrayList;
        }

        @NonNull
        @Override
        public Timetableholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_timetable_live, viewGroup, false);
            return new Timetableholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Timetableholder timetableholder, int i) {
            LiveTimetable timetable = timetableArrayList.get(i);

            timetableholder.tvSubject.setText(timetable.getSubjectName());
            timetableholder.tvTeacher.setText(timetable.getTeacher());
            timetableholder.tvTime.setText(timetable.getStartTime()+"-"+timetable.getEndTime());
        }

        @Override
        public int getItemCount() {
            return timetableArrayList.size();
        }

        class Timetableholder extends RecyclerView.ViewHolder {
            TextView tvSubject, tvTime, tvTeacher;
            LinearLayout llTimeTable;

            public Timetableholder(@NonNull View itemView) {
                super(itemView);
                tvSubject = itemView.findViewById(R.id.tvSubject);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvTeacher = itemView.findViewById(R.id.tvTeacher);
                llTimeTable = itemView.findViewById(R.id.llTimeTable);
            }
        }
    }

    public class SpinAdapter extends ArrayAdapter<SpinnerItems> {

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private ArrayList<SpinnerItems> spinnerList = new ArrayList<>();

        public SpinAdapter(Context context, int textViewResourceId,
                           ArrayList<SpinnerItems> spinnerList) {
            super(context, textViewResourceId, spinnerList);
            this.context = context;
            this.spinnerList = spinnerList;
        }


        @Override
        public int getCount() {
            return spinnerList.size();
        }

        @Override
        public SpinnerItems getItem(int position) {
            return spinnerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            SpinnerItems spinnerItems = spinnerList.get(position);
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setText(spinnerItems.getDay());
            Log.d(TAG, "return day: " + spinnerItems.getReturnDay());
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            SpinnerItems spinnerItems = spinnerList.get(position);
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setText(spinnerItems.getDay());
            Log.d(TAG, "return day: " + spinnerItems.getReturnDay());
            return label;
        }
    }

    public class SpinLiveAdapter extends ArrayAdapter<SpinnerItemsLive> {

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private ArrayList<SpinnerItemsLive> spinnerList = new ArrayList<>();

        public SpinLiveAdapter(Context context, int textViewResourceId,
                           ArrayList<SpinnerItemsLive> spinnerList) {
            super(context, textViewResourceId, spinnerList);
            this.context = context;
            this.spinnerList = spinnerList;
        }


        @Override
        public int getCount() {
            return spinnerList.size();
        }

        @Override
        public SpinnerItemsLive getItem(int position) {
            return spinnerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            SpinnerItemsLive spinnerItems = spinnerList.get(position);
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setText(spinnerItems.getDay());
            Log.d(TAG, "return day: " + spinnerItems.getReturnDay());
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            SpinnerItemsLive spinnerItems = spinnerList.get(position);
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setText(spinnerItems.getDay());
            Log.d(TAG, "return day: " + spinnerItems.getReturnDay());
            return label;
        }
    }
    private void showsnackbar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(TimeTableActivity.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
    private void getclassid()
    {
        User user = SharedPrefManager.getInstance(this).getUser();
        Call<ProfileResponse> call2 = RetrofitClient
                .getInstance().getApi().getUserProfile(user.getId());
        call2.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse profileResponse = response.body();
                Log.d(TAG, profileResponse.getUserprofile().getClassid() + " classid");
                classid = profileResponse.getUserprofile().getClassid();
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                if (mProg.isShowing()) {
                    mProg.dismiss();
                }
                //Toast.makeText(Examination.this, "Error :"+ t , Toast.LENGTH_SHORT).show();
            }
        });

    }
}
