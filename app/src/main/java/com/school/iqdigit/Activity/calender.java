package com.school.iqdigit.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.CalenderResponse;
import com.school.iqdigit.Modeldata.Calender;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;

public class calender extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Calender> calenders;
    private JellyToggleButton jtb;
    private String f_date , e_date ;
    private Date sdat, edat;
    private DatePickerDialog datePickerDialog;
    private Button f_date_btn, e_date_btn;
    private FloatingActionButton getAttendBtn;
    private Calendar calendar;
    private LinearLayout view_calender_layout, view_detailview;
    private ImageView backbtn;
    int year;
    int month;
    int day;
    private boolean checkinternet;
    MCalendarView calendarView ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        calendarView = findViewById(R.id.mcalendarview2);
        checkinternet = InternetCheck.isInternetOn(calender.this);
        getAttendBtn = findViewById(R.id.getAttendbtn);
        backbtn = findViewById(R.id.backbtna);
        f_date_btn = findViewById(R.id.f_date_btn);
        e_date_btn = findViewById(R.id.l_date_btn);
        view_calender_layout = findViewById(R.id.view_calender_layout);
        view_detailview = findViewById(R.id.recycle_calender_layout);
        recyclerView = findViewById(R.id.calenderrecyclerview);
        jtb = findViewById(R.id.switch_cal_lay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAttendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCalender();
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
                datePickerDialog = new DatePickerDialog(calender.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String yy = String.valueOf(year);
                                String dd = String.valueOf(dayOfMonth);
                                month = month + 1;
                                String mm = String.valueOf(month);
                                if(month < 10 ){
                                    mm = "0" + mm ;
                                }
                                if(dayOfMonth < 10){
                                    dd = "0" + dd;
                                }

                                e_date = yy + "-" + mm + "-" + dd;
                                e_date_btn.setText(e_date);
                            }
                        },year,month,day);
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
                datePickerDialog = new DatePickerDialog(calender.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String yy = String.valueOf(year);
                                String dd = String.valueOf(dayOfMonth);
                                month = month+1;
                                String mm = String.valueOf(month);
                                if(month < 10 ){
                                    mm = "0" + mm ;
                                }
                                if(dayOfMonth < 10){
                                    dd = "0" + dd;
                                }
                                f_date = yy + "-" + mm + "-" + dd;
                                f_date_btn.setText(f_date);
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });

        jtb.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener() {
            @Override
            public void onStateChange(float process, State state, JellyToggleButton jtb) {
                if(state.equals(State.LEFT)){
                    view_calender_layout.setVisibility(View.VISIBLE);
                    view_detailview.setVisibility(View.GONE);
                }else{
                    view_calender_layout.setVisibility(View.GONE);
                    view_detailview.setVisibility(View.VISIBLE);

                }

            }
        });

        getcurrentcalender();
        getCalender();
    }

    private void getcurrentcalender() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String yy = String.valueOf(year);
        String dd = String.valueOf(day);
        month = month+1;
        String mm = String.valueOf(month);
        if(month < 10 ){
            mm = "0" + mm ;
        }
        if(day < 10){
            dd = "0" + dd;
        }
        String s_date = yy + "-" + mm + "-" + dd;
        String l_date = yy + "-" + mm + "-" + "01";
        f_date_btn.setText(l_date);
        f_date = l_date;
        e_date = s_date;
        e_date_btn.setText(s_date);
    }

    private void getCalender() {
        calendarView.setMarkedStyle(MarkStyle.BACKGROUND);
//        calendarView.setDateCell()
        User user = SharedPrefManager.getInstance(this).getUser();
        Call<CalenderResponse> call = RetrofitClient
                .getInstance().getApi().getcalender(f_date,e_date);
        call.enqueue(new Callback<CalenderResponse>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onResponse(Call<CalenderResponse> call, Response<CalenderResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    final String[][] adate = new String[1][1];
                    final String[] alldates = new String[1];
                    calenders = response.body().getCalender();
                    List<Calender> calenders1;

                    calenderadapter adaper = new calenderadapter(calender.this, calenders);
                    recyclerView.setAdapter(adaper);

                    CalenderResponse CalenderResponse = response.body();
                    calenders1 = CalenderResponse.getCalender();

                    //Creating an String array for the ListView
                    String[] type = new String[calenders1.size()];
                    adate[0] = new String[calenders1.size()];


                    // List<Date> dates = getListOfDaysBetweenTwoDates(sdat, edat);
                    Calendar cal2 = Calendar.getInstance();
                    int maxDay = cal2.getActualMaximum(Calendar.DAY_OF_MONTH);

                    calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
                        @Override
                        public void onMonthChange(int year, int month) {

                        }
                    });
                    for (int i = 0; i < maxDay; i++) {
                        int month = 0, dd = 0, yer = 0;
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            Date d = sdf.parse(calenders1.get(1).getDate());
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


                    for (int i = 0; i < calenders1.size(); i++) {
                        int month = 0, dd = 0, yer = 0;
                        type[i] = calenders1.get(i).getIsholiday();
                        adate[0][i] = calenders1.get(i).getDate();

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

                        if (type[i].equals("0")) {
                            calendarView.markDate(
                                    new DateData(yer, month, dd).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.green_500))));
                        } else {
                            calendarView.markDate(
                                    new DateData(yer, month, dd).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.red_500))));
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
            }

            @Override
            public void onFailure(Call<CalenderResponse> call, Throwable t) {
                Toast.makeText(calender.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int checkDigit (int number) {
        return Integer.parseInt(number <= 9 ? "0" + number : String.valueOf(number));
    }



    private class calenderadapter extends RecyclerView.Adapter<calenderadapter.calenderholder>{
        private Context mCtx;
        private List<Calender> calenderList;

        public calenderadapter(Context mCtx, List<Calender> calenderList) {
            this.mCtx = mCtx;
            this.calenderList = calenderList;
        }

        @NonNull
        @Override
        public calenderholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_calender_list,viewGroup,false);
            return new calenderholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull calenderholder holder, int i) {
            Calender calender = calenderList.get(i);


            String date = calender.getDate();
            String parts[] = date.split("-");

            int dayor = Integer.parseInt(parts[2]);
            int monthor = Integer.parseInt(parts[1]);
            int yearor = Integer.parseInt(parts[0]);

            holder.cal_dayandmonth.setText(dayor+"-"+monthor);
            holder.cal_year.setText(yearor+"");
            holder.cal_title.setText(calender.getTitle());
            holder.cal_desc.setText(calender.getDesc());
            Integer isholi = Integer.valueOf(calender.getIsholiday());
            if(isholi == 0) {
                holder.cal_isholiday.setText("Activity");
            }else{
                holder.cal_isholiday.setText("Holiday");
            }

        }

        @Override
        public int getItemCount() {
            return calenderList.size();
        }

        class calenderholder extends RecyclerView.ViewHolder{
            TextView cal_dayandmonth, cal_year, cal_isholiday, cal_title, cal_desc;
            public calenderholder(@NonNull View itemView) {
                super(itemView);
                cal_dayandmonth = itemView.findViewById(R.id.cal_dateandmonth);
                cal_year = itemView.findViewById(R.id.cal_year);
                cal_isholiday = itemView.findViewById(R.id.cal_isholiday);
                cal_title = itemView.findViewById(R.id.cal_title);
                cal_desc = itemView.findViewById(R.id.cal_desc);

            }
        }
    }
}
