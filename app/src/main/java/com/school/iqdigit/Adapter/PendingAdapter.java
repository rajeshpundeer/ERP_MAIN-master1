package com.school.iqdigit.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.instacart.library.truetime.TrueTime;
import com.itextpdf.text.pdf.parser.Line;
import com.school.iqdigit.Activity.Assessment1Activity;
import com.school.iqdigit.Activity.ImageTeacherActivity;
import com.school.iqdigit.Activity.PdfWebViewActivity;
import com.school.iqdigit.Activity.PdfWebViewActivity1;
import com.school.iqdigit.Activity.imageActivity;
import com.school.iqdigit.Activity.imageActivity1;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.Assessment_list;
import com.school.iqdigit.Model.GetCurrentTime;
import com.school.iqdigit.R;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.Pendingholder> {
    private Context mCtx;
    private List<Assessment_list> pendingList;
    private String TAG = "PendingAdapter";
    long different;
    private Date currentdatetime;



    public PendingAdapter(Context mCtx, List<Assessment_list> pendingList, Date currentdatetime) {
        this.mCtx = mCtx;
        this.pendingList = pendingList;
        this.currentdatetime = currentdatetime;
    }

    @NonNull
    @Override
    public Pendingholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_assessment_pending_student, viewGroup, false);
        return new Pendingholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pendingholder holder, int i) {
        Date date1 = null;
        Date date2 = null;
        Assessment_list pendingAssessment = pendingList.get(i);
        holder.title.setText(pendingAssessment.getAs_name());
        holder.date.setText(pendingAssessment.getCreated_at());
        holder.description.setText(pendingAssessment.getEs_detail());
        if (pendingAssessment.getAssessment().equals("2")) {
            holder.q_type_banner.setVisibility(View.VISIBLE);
            holder.q_type_banner.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.ic_mcq_banner));
        } else {
            holder.q_type_banner.setVisibility(View.GONE);
        }

        if (pendingAssessment.getTime_bound() == 1 && (Integer.toString(pendingAssessment.getTime_bound()) != null)) {
            Log.d(TAG, pendingAssessment.getStart_datetime() + " start date " + pendingAssessment.getEnd_datetime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
//                date1 = simpleDateFormat.parse("06/06/2020 21:00:00");
//                date2 = simpleDateFormat.parse("06/06/2020 22:09:00");
                if (pendingAssessment.getStart_datetime() != null && pendingAssessment.getEnd_datetime() != null) {
                    date1 = simpleDateFormat.parse(pendingAssessment.getStart_datetime());
                    date2 = simpleDateFormat.parse(pendingAssessment.getEnd_datetime());
                    holder.tvStartTime.setVisibility(View.VISIBLE);
                    holder.tvEndTime.setVisibility(View.VISIBLE);
                    holder.lltimeshow.setVisibility(View.VISIBLE);
                    holder.tvStartTime.setText(DateFormat.getDateTimeInstance().format(date1) +"");
                    holder.tvEndTime.setText(DateFormat.getDateTimeInstance().format(date2) +"");

                    Log.d(TAG, date1 + " date1 " + date2 + " date2");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Date finalDate = date2;
            Date finalDate1 = date1;


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
                    holder.btnView.setVisibility(View.VISIBLE);
                    holder.btnSubmit.setVisibility(View.VISIBLE);
                    holder.btnSubmit1.setVisibility(View.GONE);
                    holder.tv_timer.setVisibility(View.VISIBLE);
                    holder.tv_timer.setBackgroundColor(Color.parseColor("#F4511E"));
                    holder.tv_timer.setText("Time to over " + elapsedDays1 + "days " + elapsedHours1 + "hr " + min + "min");
                } else {
                    Log.d(TAG, "disabled1 " + difference);
                    holder.btnSubmit.setVisibility(View.GONE);
                    holder.btnSubmit1.setVisibility(View.VISIBLE);
                    holder.btnView.setVisibility(View.VISIBLE);
                    holder.tv_timer.setVisibility(View.GONE);
                    holder.btnSubmit1.setText("Time Over");
                }
            } else {
                if (currentdatetime.getTime() < finalDate1.getTime()) {
                    Log.d(TAG, "disabled2");
                    holder.btnView.setVisibility(View.GONE);
                    holder.btnSubmit.setVisibility(View.GONE);
                    holder.btnSubmit1.setVisibility(View.GONE);
                    holder.tv_timer.setVisibility(View.VISIBLE);
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

                    holder.tv_timer.setBackgroundColor(Color.parseColor("#43A047"));
                    holder.tv_timer.setText("Time to Start " + elapsedDays1 + "days " + elapsedHours1 + "hr " + min + "min");
                } else if (currentdatetime.getTime() > finalDate.getTime()) {
                    Log.d(TAG, "disabled3");
                    holder.btnSubmit.setVisibility(View.GONE);
                    holder.btnSubmit1.setVisibility(View.VISIBLE);
                    holder.btnView.setVisibility(View.VISIBLE);
                    holder.tv_timer.setVisibility(View.GONE);
                    holder.btnSubmit1.setText("Time Over");
                }
            }
        } else {
            holder.tv_timer.setVisibility(View.GONE);
            holder.btnView.setVisibility(View.VISIBLE);
            holder.btnSubmit1.setVisibility(View.GONE);
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.tvEndTime.setVisibility(View.GONE);
            holder.tvStartTime.setVisibility(View.GONE);
            holder.lltimeshow.setVisibility(View.GONE);
        }
        holder.tvSubject.setText(pendingAssessment.getEs_subjectname() + " / " + pendingAssessment.getTeacher_name());
        if (pendingAssessment.getAssessment_unit().equals("Marks")) {
            holder.al_assessment_type.setText(pendingAssessment.getAssessment_unit() + "( " + pendingAssessment.getMax() + ")");
        } else {
            holder.al_assessment_type.setText(pendingAssessment.getAssessment_unit());
        }
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pendingAssessment.getAs_description().endsWith("pdf")) {
                    Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                    intent.putExtra("pdf", pendingAssessment.getAs_description());
                    intent.putExtra("title", "Assessment");
                    mCtx.startActivity(intent);
                } else {
                    Intent intent = new Intent(mCtx, imageActivity1.class);
                    intent.putExtra("imgurl", pendingAssessment.getAs_description());
                    mCtx.startActivity(intent);
                }
            }
        });

        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<GetCurrentTime> call2 = RetrofitClient.getInstance().getApi().getCurrentDateTime();
                call2.enqueue(new Callback<GetCurrentTime>() {
                    @Override
                    public void onResponse(Call<GetCurrentTime> call, Response<GetCurrentTime> response) {
                        Date currentdatetime1 = null;

                        Date date1 = null;
                        if (pendingAssessment.getTime_bound() == 1 && (Integer.toString(pendingAssessment.getTime_bound()) != null)) {
                            Log.d(TAG, pendingAssessment.getStart_datetime() + " start date " + pendingAssessment.getEnd_datetime());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                if (pendingAssessment.getStart_datetime() != null && pendingAssessment.getEnd_datetime() != null) {
                                    date1 = simpleDateFormat.parse(pendingAssessment.getStart_datetime());
                                    Log.d(TAG, date1 + " date1 ");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date finalDate1 = date1;
                            if (response.body() != null) {
                                String currentdate = response.body().getCurrentdatetime();
                                String newcurrentdate = currentdate.replace("-", "/");
                                Log.d(TAG, newcurrentdate + " date");
                                try {
                                    currentdatetime1 = simpleDateFormat.parse(newcurrentdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (currentdatetime1.getTime() >= finalDate1.getTime()) {
                                if (pendingAssessment.getAssessment().equals("1")) {
                                    Intent intent = new Intent(mCtx, Assessment1Activity.class);
                                    intent.putExtra("hw_id", String.valueOf(pendingAssessment.getId()));
                                    mCtx.startActivity(intent);
                                    ((Activity) mCtx).finish();
                                } else if (pendingAssessment.getAssessment().equals("2")) {
                                    if (pendingAssessment.getAs_description().endsWith("pdf")) {
                                        Intent intent = new Intent(mCtx, PdfWebViewActivity1.class);
                                        intent.putExtra("pdf", pendingAssessment.getAs_description());
                                        intent.putExtra("hw_id", String.valueOf(pendingAssessment.getHw_id()));
                                        intent.putExtra("usertype", "student");
                                        intent.putExtra("mcq_count", pendingAssessment.getMcq_count());
                                        intent.putExtra("mcq_marks", pendingAssessment.getMcq_marks());
                                        intent.putExtra("timebound", Integer.toString(pendingAssessment.getTime_bound()));
                                        intent.putExtra("start_date", pendingAssessment.getStart_datetime());
                                        intent.putExtra("end_date", pendingAssessment.getEnd_datetime());
                                        mCtx.startActivity(intent);
                                        ((Activity) mCtx).finish();
                                    } else {
                                        Intent intent = new Intent(mCtx, ImageTeacherActivity.class);
                                        intent.putExtra("imgurl", pendingAssessment.getAs_description());
                                        intent.putExtra("hw_id", String.valueOf(pendingAssessment.getHw_id()));
                                        intent.putExtra("usertype", "student");
                                        intent.putExtra("mcq_count", pendingAssessment.getMcq_count());
                                        intent.putExtra("mcq_marks", pendingAssessment.getMcq_marks());
                                        intent.putExtra("timebound", Integer.toString(pendingAssessment.getTime_bound()));
                                        intent.putExtra("start_date", pendingAssessment.getStart_datetime());
                                        intent.putExtra("end_date", pendingAssessment.getEnd_datetime());
                                        mCtx.startActivity(intent);
                                        ((Activity) mCtx).finish();
                                    }
                                }
                            } else {
                                Toast.makeText(mCtx, "Test will start at" + finalDate1 + ". Please WaiT.", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            if (pendingAssessment.getAssessment().equals("1")) {
                                Intent intent = new Intent(mCtx, Assessment1Activity.class);
                                intent.putExtra("hw_id", String.valueOf(pendingAssessment.getId()));
                                mCtx.startActivity(intent);
                                ((Activity) mCtx).finish();
                            } else if (pendingAssessment.getAssessment().equals("2")) {
                                if (pendingAssessment.getAs_description().endsWith("pdf")) {
                                    Intent intent = new Intent(mCtx, PdfWebViewActivity1.class);
                                    intent.putExtra("pdf", pendingAssessment.getAs_description());
                                    intent.putExtra("hw_id", String.valueOf(pendingAssessment.getHw_id()));
                                    intent.putExtra("usertype", "student");
                                    intent.putExtra("mcq_count", pendingAssessment.getMcq_count());
                                    intent.putExtra("mcq_marks", pendingAssessment.getMcq_marks());
                                    intent.putExtra("timebound", Integer.toString(pendingAssessment.getTime_bound()));
                                    intent.putExtra("start_date", pendingAssessment.getStart_datetime());
                                    intent.putExtra("end_date", pendingAssessment.getEnd_datetime());
                                    mCtx.startActivity(intent);
                                    ((Activity) mCtx).finish();
                                } else {
                                    Intent intent = new Intent(mCtx, ImageTeacherActivity.class);
                                    intent.putExtra("imgurl", pendingAssessment.getAs_description());
                                    intent.putExtra("hw_id", String.valueOf(pendingAssessment.getHw_id()));
                                    intent.putExtra("usertype", "student");
                                    intent.putExtra("mcq_count", pendingAssessment.getMcq_count());
                                    intent.putExtra("mcq_marks", pendingAssessment.getMcq_marks());
                                    intent.putExtra("timebound", Integer.toString(pendingAssessment.getTime_bound()));
                                    intent.putExtra("start_date", pendingAssessment.getStart_datetime());
                                    intent.putExtra("end_date", pendingAssessment.getEnd_datetime());
                                    mCtx.startActivity(intent);
                                    ((Activity) mCtx).finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCurrentTime> call, Throwable t) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    class Pendingholder extends RecyclerView.ViewHolder {
        TextView title, date, description, tvSubject, al_assessment_type, tv_timer, tvEndTime, tvStartTime;
        Button btnView, btnSubmit, btnSubmit1;
        ImageView q_type_banner;
        private LinearLayout lltimeshow;

        public Pendingholder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            title = itemView.findViewById(R.id.al_title);
            date = itemView.findViewById(R.id.al_createdon);
            description = itemView.findViewById(R.id.al_description);
            al_assessment_type = itemView.findViewById(R.id.al_assessment_type);
            btnView = itemView.findViewById(R.id.btnView);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);
            btnSubmit1 = itemView.findViewById(R.id.btnSubmit1);
            tv_timer = itemView.findViewById(R.id.tv_timer);
            q_type_banner = itemView.findViewById(R.id.q_type_banner);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            lltimeshow = itemView.findViewById(R.id.lltimeshow);
        }
    }

}