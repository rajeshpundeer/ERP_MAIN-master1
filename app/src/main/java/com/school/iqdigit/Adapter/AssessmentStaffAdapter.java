package com.school.iqdigit.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.instacart.library.truetime.TrueTime;
import com.school.iqdigit.Activity.AssesmentStaffListActivity;
import com.school.iqdigit.Activity.ImageTeacherActivity;
import com.school.iqdigit.Activity.PdfWebViewActivity;
import com.school.iqdigit.Activity.PdfWebViewActivity1;
import com.school.iqdigit.Activity.imageActivity1;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.Assessment_list_staff;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.GetCurrentTime;
import com.school.iqdigit.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssessmentStaffAdapter extends RecyclerView.Adapter<AssessmentStaffAdapter.Pendingholder> {
    private Context mCtx;
    private List<Assessment_list_staff> assessmentStaffList;
    private String TAG = "AssessmentStaffAdapter";
    Date date1 = null;
    Date date2 = null;
    long different;
    private Date currentdatetime;
    private ProgressDialog progressDialog;


    public AssessmentStaffAdapter(Context mCtx, List<Assessment_list_staff> assessmentStaffList, Date currentdatetime) {
        this.mCtx = mCtx;
        this.assessmentStaffList = assessmentStaffList;
        this.currentdatetime = currentdatetime;
    }

    @NonNull
    @Override
    public Pendingholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_assessment_staff, viewGroup, false);
        return new Pendingholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pendingholder holder, int i) {
        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setTitle("Exam Marks");
        progressDialog.setMessage("Wait a moment...");
        progressDialog.create();
        Date date1 = null;
        Date date2 = null;
        Assessment_list_staff submittedAssessment = assessmentStaffList.get(i);
        holder.title.setText(submittedAssessment.getAs_name());
        holder.date.setText(submittedAssessment.getCreated_at());
        holder.description.setText(submittedAssessment.getEs_detail());
        if (submittedAssessment.getAssessment().equals("2")) {
            holder.tv_mcq_ans.setVisibility(View.VISIBLE);
            holder.q_type_banner.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.ic_mcq_banner));
            holder.al_assessment_type.setVisibility(View.GONE);
        } else {
            holder.q_type_banner.setVisibility(View.GONE);
            holder.tv_mcq_ans.setVisibility(View.GONE);
            holder.al_assessment_type.setVisibility(View.VISIBLE);
        }

        holder.tvClassName.setText(submittedAssessment.getEs_classname() + " / " + submittedAssessment.getEs_subjectname());
        if (submittedAssessment.getAssessment_unit().equals("Marks")) {
            holder.al_assessment_type.setText(submittedAssessment.getAssessment_unit() + "( " + submittedAssessment.getMax() + ")");
        } else {
            holder.al_assessment_type.setText(submittedAssessment.getAssessment_unit());
        }
        holder.tvSubmittedNum.setText(submittedAssessment.getSubmitted());
        holder.tvPendingNum.setText(submittedAssessment.getPending());
        holder.tvCheckedNum.setText(submittedAssessment.getChecked());

        if (Integer.parseInt(submittedAssessment.getPending()) + Integer.parseInt(submittedAssessment.getSubmitted()) + Integer.parseInt(submittedAssessment.getChecked()) != 0) {
            holder.tv_mcq_ans.setVisibility(View.GONE);
        }
        holder.tvPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, AssesmentStaffListActivity.class);
                intent.putExtra("type", "pending");
                intent.putExtra("hw_id", String.valueOf(submittedAssessment.getHw_id()));
                intent.putExtra("a_class", submittedAssessment.getEs_classname() + " / " + submittedAssessment.getEs_subjectname());
                intent.putExtra("a_title", String.valueOf(submittedAssessment.getAs_name()));
                intent.putExtra("imgurl", String.valueOf(submittedAssessment.getAs_description()));
                intent.putExtra("a_type", submittedAssessment.getAssessment_unit() + "( " + submittedAssessment.getMax() + ")");
                intent.putExtra("assessment_type", submittedAssessment.getAssessment());
                intent.putExtra("pendingcount",submittedAssessment.getPending());
                intent.putExtra("submittedcount",submittedAssessment.getSubmitted());
                mCtx.startActivity(intent);
                ((Activity) mCtx).finish();
            }
        });
        holder.tvSubmitted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, AssesmentStaffListActivity.class);
                intent.putExtra("type", "submitted");
                intent.putExtra("hw_id", String.valueOf(submittedAssessment.getHw_id()));
                intent.putExtra("a_class", submittedAssessment.getEs_classname() + " / " + submittedAssessment.getEs_subjectname());
                intent.putExtra("a_title", String.valueOf(submittedAssessment.getAs_name()));
                intent.putExtra("imgurl", String.valueOf(submittedAssessment.getAs_description()));
                intent.putExtra("a_type", submittedAssessment.getAssessment_unit() + "( " + submittedAssessment.getMax() + ")");
                intent.putExtra("assessment_type", submittedAssessment.getAssessment());
                intent.putExtra("pendingcount",submittedAssessment.getPending());
                intent.putExtra("submittedcount",submittedAssessment.getSubmitted());
                mCtx.startActivity(intent);
                ((Activity) mCtx).finish();
            }
        });

        holder.tvChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, AssesmentStaffListActivity.class);
                intent.putExtra("type", "checked");
                intent.putExtra("hw_id", String.valueOf(submittedAssessment.getHw_id()));
                intent.putExtra("a_class", submittedAssessment.getEs_classname() + " / " + submittedAssessment.getEs_subjectname());
                intent.putExtra("a_title", String.valueOf(submittedAssessment.getAs_name()));
                intent.putExtra("imgurl", String.valueOf(submittedAssessment.getAs_description()));
                intent.putExtra("a_type", submittedAssessment.getAssessment_unit() + "( " + submittedAssessment.getMax() + ")");
                intent.putExtra("assessment_type", submittedAssessment.getAssessment());
                intent.putExtra("pendingcount",submittedAssessment.getPending());
                intent.putExtra("submittedcount",submittedAssessment.getSubmitted());
                mCtx.startActivity(intent);
                ((Activity) mCtx).finish();
            }
        });
        holder.tvViewHomeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submittedAssessment.getAs_description().endsWith("pdf")) {
                    Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                    intent.putExtra("pdf", submittedAssessment.getAs_description());
                    intent.putExtra("title", "Assessment");
                    mCtx.startActivity(intent);
                } else {
                    Intent intent = new Intent(mCtx, imageActivity1.class);
                    intent.putExtra("imgurl", submittedAssessment.getAs_description());
                    mCtx.startActivity(intent);
                }
            }
        });

        holder.tv_mcq_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submittedAssessment.getAs_description().endsWith("pdf")) {
                    Intent intent = new Intent(mCtx, PdfWebViewActivity1.class);
                    intent.putExtra("pdf", submittedAssessment.getAs_description());
                    intent.putExtra("hw_id", String.valueOf(submittedAssessment.getHw_id()));
                    intent.putExtra("usertype", "staff");
                    intent.putExtra("mcq_count", submittedAssessment.getMcq_count());
                    intent.putExtra("mcq_marks", submittedAssessment.getMcq_marks());
                    intent.putExtra("timebound", Integer.toString(submittedAssessment.getTime_bound()));
                    intent.putExtra("start_date", submittedAssessment.getStart_datetime());
                    intent.putExtra("end_date", submittedAssessment.getEnd_datetime());
                    mCtx.startActivity(intent);
                    ((Activity) mCtx).finish();
                } else {
                    Intent intent = new Intent(mCtx, ImageTeacherActivity.class);
                    intent.putExtra("imgurl", submittedAssessment.getAs_description());
                    intent.putExtra("hw_id", String.valueOf(submittedAssessment.getHw_id()));
                    intent.putExtra("usertype", "staff");
                    intent.putExtra("mcq_count", submittedAssessment.getMcq_count());
                    intent.putExtra("mcq_marks", submittedAssessment.getMcq_marks());
                    intent.putExtra("timebound", Integer.toString(submittedAssessment.getTime_bound()));
                    intent.putExtra("start_date", submittedAssessment.getStart_datetime());
                    intent.putExtra("end_date", submittedAssessment.getEnd_datetime());
                    mCtx.startActivity(intent);
                    ((Activity) mCtx).finish();
                }
            }
        });
        if (submittedAssessment.getTime_bound() == 1 && (Integer.toString(submittedAssessment.getTime_bound()) != null)) {
            Log.d(TAG, submittedAssessment.getStart_datetime() + " start date " + submittedAssessment.getEnd_datetime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
//                date1 = simpleDateFormat.parse("06/06/2020 21:00:00");
//                date2 = simpleDateFormat.parse("06/06/2020 22:09:00");
                if (submittedAssessment.getStart_datetime() != null && submittedAssessment.getEnd_datetime() != null) {
                    date1 = simpleDateFormat.parse(submittedAssessment.getStart_datetime());
                    date2 = simpleDateFormat.parse(submittedAssessment.getEnd_datetime());
                    holder.lltimeshow.setVisibility(View.VISIBLE);
                    holder.tvStartTime.setVisibility(View.VISIBLE);
                    holder.tvEndTime.setVisibility(View.VISIBLE);
                    holder.tvStartTime.setText(DateFormat.getDateTimeInstance().format(date1) +"");
                    holder.tvEndTime.setText(DateFormat.getDateTimeInstance().format(date2) +"");
                    Log.d(TAG, date1 + " date1 " + date2 + " date2");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date finalDate = date2;
            Date finalDate1 = date1;

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
                    holder.tv_timer.setVisibility(View.VISIBLE);
                    holder.tv_timer.setBackgroundColor(Color.parseColor("#F4511E"));
                    holder.tv_timer.setText("Time to over " + elapsedDays1 + "days " + elapsedHours1 + "hr " + min + "min");
                } else {
                    Log.d(TAG, "disabled1 " + difference);
                    holder.tv_timer.setText("Time Over");
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

                    holder.tv_timer.setBackgroundColor(Color.parseColor("#43A047"));
                    holder.tv_timer.setText("Time to Start " + elapsedDays1 + "days " + elapsedHours1 + "hr " + min + "min");
                } else if (currentdatetime.getTime() > finalDate.getTime()) {
                    Log.d(TAG, "disabled3");
                    holder.tv_timer.setText("Time Over");
                }
            }

        } else {
            holder.tvEndTime.setVisibility(View.GONE);
            holder.tvStartTime.setVisibility(View.GONE);
            holder.tv_timer.setVisibility(View.GONE);
            holder.lltimeshow.setVisibility(View.GONE);
        }

        holder.tvDeleteHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNotificationPop(String.valueOf(submittedAssessment.getHw_id()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return assessmentStaffList.size();
    }

    class Pendingholder extends RecyclerView.ViewHolder {
        TextView title, date, description, tvClassName, al_assessment_type;
        Button tvPending, tvSubmitted, tvChecked;
        TextView tvPendingNum, tvSubmittedNum, tvCheckedNum, tv_timer, tv_mcq_ans, tvEndTime, tvStartTime;
        ImageView tvViewHomeWork, q_type_banner, tvDeleteHomework;
        private LinearLayout lltimeshow;

        public Pendingholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.al_title);
            date = itemView.findViewById(R.id.al_createdon);
            description = itemView.findViewById(R.id.al_description);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvViewHomeWork = itemView.findViewById(R.id.tvViewHomeWork);
            tvPending = itemView.findViewById(R.id.tvPending);
            tvSubmitted = itemView.findViewById(R.id.tvSubmitted);
            tvChecked = itemView.findViewById(R.id.tvChecked);
            tvPendingNum = itemView.findViewById(R.id.tvPendingNum);
            tvSubmittedNum = itemView.findViewById(R.id.tvSubmittedNum);
            tvCheckedNum = itemView.findViewById(R.id.tvCheckedNum);
            al_assessment_type = itemView.findViewById(R.id.al_assessment_type);
            tv_timer = itemView.findViewById(R.id.tv_timer);
            tv_mcq_ans = itemView.findViewById(R.id.tv_set_ans);
            q_type_banner = itemView.findViewById(R.id.q_type_banner);
            tvDeleteHomework = itemView.findViewById(R.id.tvDeleteHomework);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            lltimeshow = itemView.findViewById(R.id.lltimeshow);
        }
    }

    public void ShowNotificationPop(String hwid){
        Dialog dialog;
        TextView textView;
        FloatingActionButton imgcancel;
        Button btnOk,btnCancel;
        dialog = new Dialog(mCtx);
        dialog.setContentView(R.layout.ic_common_text_popup1);
        textView = dialog.findViewById(R.id.textmaintain);
        imgcancel = dialog.findViewById(R.id.img_cancel_dialog);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnOk = dialog.findViewById(R.id.btnOk);
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().delete_assessment(hwid);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body().isErr() == false) {
                            progressDialog.dismiss();
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            ((Activity) mCtx).finish();
                            mCtx.startActivity( ((Activity) mCtx).getIntent());
                        } else {
                            progressDialog.dismiss();
                            dialog.dismiss();
                            Toast.makeText(mCtx, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mCtx, "Assessment Deletion Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        textView.setText("Assessment will not be recovered back, once deleted!  Are you sure you want to delete the Assessment?");
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.closeOptionsMenu();
        dialog.setCancelable(true);
        dialog.show();
    }
}