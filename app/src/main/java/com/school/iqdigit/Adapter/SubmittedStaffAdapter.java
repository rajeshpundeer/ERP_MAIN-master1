package com.school.iqdigit.Adapter;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.iqdigit.Activity.PdfWebViewActivity;
import com.school.iqdigit.Activity.ViewAnswerKey;
import com.school.iqdigit.Activity.imageActivity1;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.GetAssessmentListRoot;
import com.school.iqdigit.R;
import com.school.iqdigit.interfaces.RemoveSubmittedAssessment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmittedStaffAdapter extends RecyclerView.Adapter<SubmittedStaffAdapter.Pendingholder> {
    private Context mCtx;
    private List<GetAssessmentListRoot> pendingList;
    private Dialog dialog;
    ArrayList<String> totalGrades = new ArrayList<>();
    RemoveSubmittedAssessment removeSubmittedAssessment;
    private String TAG = "PdfWebViewActivity";

    public SubmittedStaffAdapter(Context mCtx, List<GetAssessmentListRoot> pendingList, RemoveSubmittedAssessment removeSubmittedAssessment) {
        this.mCtx = mCtx;
        this.pendingList = pendingList;
        this.removeSubmittedAssessment = removeSubmittedAssessment;
    }

    @NonNull
    @Override
    public Pendingholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_assessment_submitted, viewGroup, false);
        return new Pendingholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pendingholder holder, int i) {
        GetAssessmentListRoot pendingAssessment;
        pendingAssessment = pendingList.get(i);
        if (pendingAssessment.getAssessment().equals("2")) {
            holder.btnDownloadAss.setVisibility(View.GONE);
            holder.btnSubmit.setVisibility(View.GONE);
            holder.btnViewReject.setVisibility(View.GONE);
            holder.btnViewAssessment.setVisibility(View.GONE);
            holder.btnAnskey.setVisibility(View.VISIBLE);
            holder.btnViewReject1.setVisibility(View.VISIBLE);
        } else {
            holder.btnDownloadAss.setVisibility(View.VISIBLE);
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.btnViewReject.setVisibility(View.VISIBLE);
            holder.btnViewAssessment.setVisibility(View.VISIBLE);
            holder.btnAnskey.setVisibility(View.GONE);
            holder.btnViewReject1.setVisibility(View.GONE);
        }
        Log.d(TAG, pendingAssessment.getPath() + " path " + " SubmittedStaffAdapter");
        holder.StudentDetail.setText("[ " + pendingAssessment.getAdmissionId() + " ] " + pendingAssessment.getStudentName());
        holder.StudentRemarks.setText(pendingAssessment.getDetails());
        holder.btnViewAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, PdfWebViewActivity.class);
                Log.d(TAG, pendingAssessment.getPath() + " path " + " SubmittedStaffAdapter");
                intent.putExtra("pdf", pendingAssessment.getPath());
                intent.putExtra("title", "Assessment");
                mCtx.startActivity(intent);
            }
        });

        holder.btnAnskey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, ViewAnswerKey.class);
                intent.putExtra("student_id", String.valueOf(pendingAssessment.getStudId()));
                intent.putExtra("hw_id", String.valueOf(pendingAssessment.getHwId()));
                intent.putExtra("user_type", "staff");
                intent.putExtra("status", "submitted_staff");
                mCtx.startActivity(intent);
            }
        });
        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(i);
            }
        });

        holder.btnDownloadAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, pendingAssessment.getPath() + " path1 " + " SubmittedStaffAdapter");
                download(pendingAssessment.getPath());
            }
        });
        holder.btnViewReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().rejectassessment(String.valueOf(pendingAssessment.getId()));
                // Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().checkassessment(finalScore, edRemarks.getText().toString(), "105");

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body() != null) {
                            if (response.body().isErr() == false) {
                                removeSubmittedAssessment.deleteCheckedAsessment(i);
                                Toast.makeText(mCtx, response.message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mCtx, "Assessment Rejection Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.btnViewReject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().rejectassessment(String.valueOf(pendingAssessment.getId()));
                // Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().checkassessment(finalScore, edRemarks.getText().toString(), "105");

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body() != null) {
                            if (response.body().isErr() == false) {
                                removeSubmittedAssessment.deleteCheckedAsessment(i);
                                Toast.makeText(mCtx, response.message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mCtx, "Assessment Rejection Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        TextView StudentDetail, StudentRemarks;
        Button btnViewAssessment, btnSubmit, btnViewReject, btnAnskey,btnViewReject1;
        ImageView btnDownloadAss;

        public Pendingholder(@NonNull View itemView) {
            super(itemView);
            btnViewAssessment = itemView.findViewById(R.id.btnViewAssessment);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);
            btnDownloadAss = itemView.findViewById(R.id.btnDownloadAss);
            StudentDetail = itemView.findViewById(R.id.StudentDetail);
            StudentRemarks = itemView.findViewById(R.id.StudentRemarks);
            btnViewReject = itemView.findViewById(R.id.btnViewReject);
            btnAnskey = itemView.findViewById(R.id.btnAnskey);
            btnViewReject1 = itemView.findViewById(R.id.btnViewReject1);
        }
    }

    private void showCustomDialog(int i) {
        GetAssessmentListRoot pendingAssessment = pendingList.get(i);
        dialog = new Dialog(mCtx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.evaluation_pop);
        dialog.setCancelable(true);
        final String[] score = {"0"};

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        LinearLayout llRating = dialog.findViewById(R.id.llRating);
        LinearLayout llGrade = dialog.findViewById(R.id.llGrade);
        LinearLayout llPercentage = dialog.findViewById(R.id.llPercentage);
        LinearLayout llMarks = dialog.findViewById(R.id.llMarks);
        RatingBar ratingbar1 = dialog.findViewById(R.id.rating);
        Spinner spGrade = dialog.findViewById(R.id.spGrade);
        EditText edPercentage = dialog.findViewById(R.id.edPercentage);
        EditText edMarks = dialog.findViewById(R.id.edMarks);
        TextView tvScore = dialog.findViewById(R.id.tvScore);
        EditText edRemarks = dialog.findViewById(R.id.edRemarks);
        ArrayAdapter<String> adapter2;
        adapter2 = new ArrayAdapter<String>(mCtx, android.R.layout.simple_list_item_1, totalGrades);
        totalGrades.add("A1");
        totalGrades.add("A2");
        totalGrades.add("B1");
        totalGrades.add("B2");
        totalGrades.add("C");
        totalGrades.add("D");

        //setting adapter to spinner
        spGrade.setAdapter(adapter2);
        if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Marks")) {
            llMarks.setVisibility(View.VISIBLE);
            tvScore.setText(pendingAssessment.getMax());

        } else if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Rating")) {
            llRating.setVisibility(View.VISIBLE);
            ratingbar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    score[0] = String.valueOf(Math.round(ratingBar.getRating()));
                    System.out.println("Rate for Module is1" + score[0]);
                }
            });
        } else if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Percentage")) {
            llPercentage.setVisibility(View.VISIBLE);

        } else if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Grade")) {
            llGrade.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(mCtx, "You have selected N/A for this Assignment./n So, You can not Assess this.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        FloatingActionButton imageButton = dialog.findViewById(R.id.bt_close);
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
                if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Marks")) {
                    score[0] = edMarks.getText().toString();

                } else if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Percentage")) {
                    score[0] = edPercentage.getText().toString();

                } else if (pendingAssessment.getAssessmentUnit().equalsIgnoreCase("Grade")) {
                    score[0] = spGrade.getSelectedItem().toString();
                }
                String finalScore = score[0];
                if (!finalScore.equals("")) {
                    System.out.println("Rate for Module is2" + finalScore);
                  //  if (!edRemarks.getText().toString().equals("")) {
                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().checkassessment(finalScore, edRemarks.getText().toString(), String.valueOf(pendingAssessment.getId()));
                        // Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().checkassessment(finalScore, edRemarks.getText().toString(), "105");

                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().isErr() == false) {
                                        dialog.dismiss();
                                        removeSubmittedAssessment.deleteCheckedAsessment(i);
                                        Toast.makeText(mCtx, "Assessment Evaluated Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(mCtx, "Assessment Evaluation Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                dialog.dismiss();
                                Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    //} else {
                       // Toast.makeText(mCtx, "Please Enter Remarks", Toast.LENGTH_SHORT).show();
                    //}
                } else {
                    Toast.makeText(mCtx, "Please Enter Score , Percentage or Grade.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void download(String path) {
        DownloadManager downloadmanager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
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
        Toast.makeText(mCtx, "File is downloaded successfully and saved in downloads", Toast.LENGTH_LONG).show();
    }
}