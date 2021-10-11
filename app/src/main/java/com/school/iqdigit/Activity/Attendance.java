package com.school.iqdigit.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.school.iqdigit.Adapter.AttendAdapter;
import com.school.iqdigit.Adapter.LiveStudAttenadapter;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.AttendResponse;
import com.school.iqdigit.Model.LiveAttendanceResponse;
import com.school.iqdigit.Model.LiveAttendanceStud;
import com.school.iqdigit.Modeldata.Attend;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;

public class Attendance extends AppCompatActivity {
    // private RecyclerView recyclerView;
    private AttendAdapter adapter;
    private List<Attend> attendList;
    private String f_date, e_date;
    private Date sdat, edat;
    private DatePickerDialog datePickerDialog;
    private Button f_date_btn, e_date_btn;
    private FloatingActionButton getAttendBtn;
    private Calendar calendar;
    private ImageView backbtn;
    //private LinearLayout noattendance;
    int year;
    int month;
    int day;
    MCalendarView calendarView;
    private ProgressDialog progressDialog;
    private TextView navigation_live_class, navigation_ragular;
    private LinearLayout view_live_class, view_ragular, ll_live, ll_Regular,noattendance,liveattenHead;
    private RecyclerView rv_liveattendance;
    private Button date_btn;
    private ImageView next;
    private String getdate;
    private String TAG = "Attendance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        navigation_live_class = findViewById(R.id.navigation_live_class);
        navigation_ragular = findViewById(R.id.navigation_ragular);
        view_live_class = findViewById(R.id.view_live_class);
        view_ragular = findViewById(R.id.view_ragular);
        ll_live = findViewById(R.id.ll_live);
        ll_Regular = findViewById(R.id.llRagular);
        rv_liveattendance = findViewById(R.id.rv_liveattendance);
        rv_liveattendance.setLayoutManager(new LinearLayoutManager(this));
        liveattenHead = findViewById(R.id.liveattenHead);
        date_btn = findViewById(R.id.date_btn);
        noattendance = findViewById(R.id.noattendance);
        next = findViewById(R.id.next);
        calendarView = findViewById(R.id.mcalendarview);
        getAttendBtn = findViewById(R.id.getAttendbtn);
        backbtn = findViewById(R.id.backbtna);
        f_date_btn = findViewById(R.id.f_date_btn);
        e_date_btn = findViewById(R.id.l_date_btn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Attendance");
        progressDialog.setMessage("Wait a moment while \nattendance is Uploading....");
        progressDialog.create();
        getcurrentattent();
        getcurrentattentLive();
        navigation_ragular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noattendance.setVisibility(View.GONE);
                ll_Regular.setVisibility(View.VISIBLE);
                ll_live.setVisibility(View.GONE);
                view_ragular.setVisibility(View.VISIBLE);
                view_live_class.setVisibility(View.GONE);
            }
        });
        navigation_live_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcurrentattentLive();
                ll_live.setVisibility(View.VISIBLE);
                ll_Regular.setVisibility(View.GONE);
                view_live_class.setVisibility(View.VISIBLE);
                view_ragular.setVisibility(View.GONE);
            }
        });

        getAttendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetCheck.isInternetOn(Attendance.this) == true) {
                    getAttend();
                } else {
                    showsnackbar();
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        e_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                sdat = calendar.getTime();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Attendance.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String yy = String.valueOf(year);
                                String dd = String.valueOf(dayOfMonth);
                                month = month + 1;
                                String mm = String.valueOf(month);
                                if (month < 10) {
                                    mm = "0" + mm;
                                }
                                if (dayOfMonth < 10) {
                                    dd = "0" + dd;
                                }

                                e_date = yy + "-" + mm + "-" + dd;
                                e_date_btn.setText(e_date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        f_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                edat = calendar.getTime();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Attendance.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String yy = String.valueOf(year);
                                String dd = String.valueOf(dayOfMonth);
                                month = month + 1;
                                String mm = String.valueOf(month);
                                if (month < 10) {
                                    mm = "0" + mm;
                                }
                                if (dayOfMonth < 10) {
                                    dd = "0" + dd;
                                }
                                f_date = yy + "-" + mm + "-" + dd;
                                f_date_btn.setText(f_date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        if (InternetCheck.isInternetOn(Attendance.this) == true) {
            getAttend();
        } else {
            showsnackbar();
        }

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Attendance.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String yy = String.valueOf(year);  //2018+"-"
                                String dd = String.valueOf(dayOfMonth);
                                month = month + 1;
                                String mm = String.valueOf(month);
                                if (month < 10) {
                                    mm = "0" + mm;
                                }
                                if (dayOfMonth < 10) {
                                    dd = "0" + dd;
                                }
                                getdate = yy + "-" + mm + "-" + dd;
                                date_btn.setText(getdate);
                                next.setVisibility(View.VISIBLE);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLiveAttendance();
            }
        });
    }

    private void showLiveAttendance() {
        if (InternetCheck.isInternetOn(Attendance.this) == true) {
            progressDialog.show();
            calendarView.setMarkedStyle(MarkStyle.BACKGROUND);
            liveattenHead.setVisibility(View.GONE);
//        calendarView.setDateCell()
            User user = SharedPrefManager.getInstance(this).getUser();
            Call<LiveAttendanceResponse> call = RetrofitClient
                   .getInstance().getApi().getLiveAttendanceResponse(user.getId(),date_btn.getText().toString());
          /*  Call<LiveAttendanceResponse> call = RetrofitClient
                .getInstance().getApi().getLiveAttendanceResponse("65","2020-06-08");*/
            call.enqueue(new Callback<LiveAttendanceResponse>() {
                @Override
                public void onResponse(Call<LiveAttendanceResponse> call, Response<LiveAttendanceResponse> response) {
                    Log.d(TAG ,response.body().getError()+" response "+response.body().getLiveAttendanceStud().size());
                    progressDialog.dismiss();
                    List<LiveAttendanceStud> studentAttenList = new ArrayList<>();
                    if(response.body().getError() == false){
                        studentAttenList = response.body().getLiveAttendanceStud();
                        Log.d(TAG ," response "+response.body().getLiveAttendanceStud().size());
                        if(studentAttenList.size() > 0)
                        {
                            liveattenHead.setVisibility(View.VISIBLE);
                            noattendance.setVisibility(View.GONE);
                            LiveStudAttenadapter liveStudAttenadapter = new LiveStudAttenadapter(Attendance.this,studentAttenList);
                            rv_liveattendance.setAdapter(liveStudAttenadapter);
                        }else if(studentAttenList.size() == 0){
                            liveattenHead.setVisibility(View.GONE);
                            noattendance.setVisibility(View.VISIBLE);
                        }
                    }else {
                        liveattenHead.setVisibility(View.GONE);
                        noattendance.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onFailure(Call<LiveAttendanceResponse> call, Throwable t) {
                    liveattenHead.setVisibility(View.GONE);
                    noattendance.setVisibility(View.VISIBLE);
                }
            });
        }else {
            showsnackbar();
        }
    }

    private void getcurrentattent() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String yy = String.valueOf(year);
        String dd = String.valueOf(day);
        month = month + 1;
        String mm = String.valueOf(month);
        if (month < 10) {
            mm = "0" + mm;
        }
        if (day < 10) {
            dd = "0" + dd;
        }
        String s_date = yy + "-" + mm + "-" + dd;
        String l_date = yy + "-" + mm + "-" + "01";
        f_date_btn.setText(l_date);
        f_date = l_date;
        e_date = s_date;
        e_date_btn.setText(s_date);
    }
    private void getcurrentattentLive() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String yy = String.valueOf(year);
        String dd = String.valueOf(day);
        month = month + 1;
        String mm = String.valueOf(month);
        if (month < 10) {
            mm = "0" + mm;
        }
        if (day < 10) {
            dd = "0" + dd;
        }
        String getdate = yy + "-" + mm + "-" + dd;
        date_btn.setText(getdate);
        showLiveAttendance();
    }
    public void getAttend() {
        calendarView.setMarkedStyle(MarkStyle.BACKGROUND);
//        calendarView.setDateCell()
        User user = SharedPrefManager.getInstance(this).getUser();
        Call<AttendResponse> call = RetrofitClient
                .getInstance().getApi().getAttendance(user.getId(), f_date, e_date);
        call.enqueue(new Callback<AttendResponse>() {
            @Override
            public void onResponse(Call<AttendResponse> call, Response<AttendResponse> response) {
                progressDialog.dismiss();
                final String[][] adate = new String[1][1];
                final String[] alldates = new String[1];
                attendList = response.body().getUser_attendence();

                List<Attend> attendList;
                AttendResponse attendResponse = response.body();
                attendList = attendResponse.getUser_attendence();

                //Creating an String array for the ListView
                String[] type = new String[attendList.size()];
                adate[0] = new String[attendList.size()];


                // List<Date> dates = getListOfDaysBetweenTwoDates(sdat, edat);
                Calendar cal2 = Calendar.getInstance();
                int maxDay = cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
//                int maxDay = 0;
//                try {
//                    maxDay = (int) betweenDates(sdat, edat);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                calendarView.setOnDateClickListener(new OnDateClickListener() {
                    @Override
                    public void onDateClick(View view, DateData date) {

                    }
                });
                calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
                    @Override
                    public void onMonthChange(int year, int month) {

                    }
                });
                for (int i = 0; i < maxDay; i++) {
                    int month = 0, dd = 0, yer = 0;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date d = sdf.parse(attendList.get(1).getAttendence_date());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        month = checkDigit(cal.get(Calendar.MONTH) + 1);
                        dd = i;
                        yer = checkDigit(cal.get(Calendar.YEAR));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    calendarView.unMarkDate(yer, month, dd);


                }


                for (int i = 0; i < attendList.size(); i++) {
                    int month = 0, dd = 0, yer = 0;
                    type[i] = attendList.get(i).getAttendence();
                    adate[0][i] = attendList.get(i).getAttendence_date();

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date d = sdf.parse(adate[0][i]);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        month = checkDigit(cal.get(Calendar.MONTH) + 1);
                        dd = checkDigit(cal.get(Calendar.DATE));
                        yer = checkDigit(cal.get(Calendar.YEAR));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (type[i].equals("P")) {
                        calendarView.markDate(
                                new DateData(yer, month, dd).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.green_500))));
                    } else if (type[i].equals("LP")) {
                        calendarView.markDate(
                                new DateData(yer, month, dd).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.colorPrimary))));
                    } else if (type[i].equals("A")) {
                        calendarView.markDate(
                                new DateData(yer, month, dd).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.red_500))));
                    } else if (type[i].equals("L")) {
                        calendarView.markDate(
                                new DateData(yer, month, dd).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.Grey_dark))));
                    } else if (type[i].equals("H")) {
                        calendarView.markDate(
                                new DateData(yer, month, dd).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.green_200))));
                    }

                }

                String date = f_date;
                String parts[] = date.split("-");

                int dayo = Integer.parseInt(parts[2]);
                int montho = Integer.parseInt(parts[1]);
                int yearo = Integer.parseInt(parts[0]);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, yearo);
                calendar.set(Calendar.MONTH, montho);
                calendar.set(Calendar.DAY_OF_MONTH, dayo);

                DateData dateData = new DateData(yearo, montho, dayo);
                calendarView.travelTo(dateData);

            }

            @Override
            public void onFailure(Call<AttendResponse> call, Throwable t) {
                if (InternetCheck.isInternetOn(Attendance.this) != true) {
                    showsnackbar();
                }
            }
        });
    }

    public int checkDigit(int number) {
        return Integer.parseInt(number <= 9 ? "0" + number : String.valueOf(number));
    }

    //    private List<Date> getListOfDaysBetweenTwoDates(Date startDate, Date endDate) {
//        List<Date> result = new ArrayList<Date>();
//        Calendar start = Calendar.getInstance();
//        start.setTime(startDate);
//        Calendar end = Calendar.getInstance();
//        end.setTime(endDate);
//        end.add(Calendar.DAY_OF_YEAR, 1); //Add 1 day to endDate to make sure endDate is included into the final list
//        while (start.before(end)) {
//            result.add(start.getTime());
//            start.add(Calendar.DAY_OF_YEAR, 1);
//        }
//        return result;
//    }
//
//    @SuppressLint("NewApi")
//    public static long betweenDates(Date firstDate, Date secondDate) throws IOException
//    {
//        return ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
//    }
    private void showsnackbar() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), R.string.internetproblem, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (InternetCheck.isInternetOn(Attendance.this) == true) {
                            startActivity(getIntent());
                            finish();
                        }
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
