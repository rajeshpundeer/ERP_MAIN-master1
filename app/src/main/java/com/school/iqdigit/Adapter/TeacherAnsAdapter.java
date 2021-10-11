package com.school.iqdigit.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.CheckAnsStaffResponse;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.McqListResponse;
import com.school.iqdigit.Model.McqStaffAn;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;
import com.school.iqdigit.utils.SharedHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherAnsAdapter extends RecyclerView.Adapter<TeacherAnsAdapter.UsersViewHolder> {

    private Context mCtx;
    private List<String> McqLayoutList;
    private String TAG = "ShowLotteryActivity";
    private String hw_id;
    private Dialog dialog;
    private String ans = "";
    private List<McqStaffAn> McqStaffAnlist;
    private int submittedquestions = 0;
    private int[] submitedanswer;

    public TeacherAnsAdapter(Context mCtx, List<String> McqLayoutList, String hw_id, List<McqStaffAn> McqStaffAnlist) {
        this.mCtx = mCtx;
        this.McqLayoutList = McqLayoutList;
        this.McqStaffAnlist = McqStaffAnlist;
        this.hw_id = hw_id;

        submitedanswer = new int[McqLayoutList.size()];
    }


    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.grid_mcq_view, viewGroup, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int i) {

        for (int j = 0; j < McqStaffAnlist.size(); j++) {
            Log.d(TAG, McqStaffAnlist.get(j).getMcqSno() + " srno " + McqLayoutList.get(i));
            if (String.valueOf(McqStaffAnlist.get(j).getMcqSno()).equals(McqLayoutList.get(i))) {
                submitedanswer[j] = McqStaffAnlist.get(j).getAns();
                Log.v("Answer : ", j + " " + submitedanswer[j] + " " + McqStaffAnlist.get(j).getAns());
                submittedquestions++;
                usersViewHolder.tv_mcqtest.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.ic_btn_view_five));
            } else {
                usersViewHolder.tv_mcqtest.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.ic_btn_view_three));
            }
        }
        Log.d(TAG, submittedquestions + " submitedanswer " + McqStaffAnlist.size());
        if (submittedquestions == McqLayoutList.size()) {
            SharedHelper.putKey(mCtx, "isAllSubmitted", "Yes");
        } else {
            SharedHelper.putKey(mCtx, "isAllSubmitted", "No");
        }
        usersViewHolder.tv_mcqtest.setText("Q" + McqLayoutList.get(i));
        usersViewHolder.tv_mcqtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(i, view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return McqLayoutList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_mcqtest;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_mcqtest = itemView.findViewById(R.id.tv_mcqtest);
        }
    }

    private void showCustomDialog(int i, View view1) {
        Log.d(TAG, hw_id + " hw_id");
        ans = "1";
        dialog = new Dialog(mCtx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_ans_pop);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);
        RadioButton radio_one = dialog.findViewById(R.id.radio_one);
        RadioButton radio_two = dialog.findViewById(R.id.radio_two);
        RadioButton radio_three = dialog.findViewById(R.id.radio_three);
        RadioButton radio_four = dialog.findViewById(R.id.radio_four);

        FloatingActionButton bt_close = dialog.findViewById(R.id.bt_close);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setText("Choose Ans for Q" + McqLayoutList.get(i));

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (submitedanswer[i] == 1) {
            radio_one.setChecked(true);
        } else if (submitedanswer[i] == 2) {
            radio_two.setChecked(true);
        } else if (submitedanswer[i] == 3) {
            radio_three.setChecked(true);
        } else if (submitedanswer[i] == 4) {
            radio_four.setChecked(true);
        }
        AppCompatButton appCompatButton = dialog.findViewById(R.id.bt_follow);

        appCompatButton.setOnClickListener(view -> {
            if (InternetCheck.isInternetOn(mCtx) == true) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);

                if (radioButton.getText().equals("A")) {
                    ans = "1";
                } else if (radioButton.getText().equals("B")) {
                    ans = "2";
                } else if (radioButton.getText().equals("C")) {
                    ans = "3";
                } else if (radioButton.getText().equals("D")) {
                    ans = "4";
                }
                Log.d(TAG, ans + " ans teacher");
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().setStaffMcqAns(hw_id, McqLayoutList.get(i), ans);
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (response.body() != null) {
                            if (response.body().isErr() == false) {
                                if (submitedanswer[i] != 0)
                                    submittedquestions--;
                                if (ans != "") {
                                    submitedanswer[i] = Integer.parseInt(ans);
                                    dialog.dismiss();
                                    view1.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.ic_btn_view_five));
                                    Toast.makeText(mCtx, "Answer Submitted Successfully", Toast.LENGTH_SHORT).show();
                                    submittedquestions++;
                                    Log.v("SubmittedQues", String.valueOf(submittedquestions));
                                }
                                if (submittedquestions == McqLayoutList.size()) {
                                    SharedHelper.putKey(mCtx, "isAllSubmitted", "Yes");
                                } else {
                                    SharedHelper.putKey(mCtx, "isAllSubmitted", "No");
                                }

//                                submitedanswer[i] = Integer.parseInt(ans);
//                                dialog.dismiss();
//                                view1.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.ic_btn_view_five));
//                                Toast.makeText(mCtx, "Answer Submitted Successfully", Toast.LENGTH_SHORT).show();
//                                submittedquestions++;
//
                                if (submittedquestions == McqLayoutList.size()) {
                                    SharedHelper.putKey(mCtx, "isAllSubmitted", "Yes");
                                } else {
                                    SharedHelper.putKey(mCtx, "isAllSubmitted", "No");
                                }

                            } else {
                                dialog.dismiss();
                                Toast.makeText(mCtx, "Answer Submition Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        dialog.dismiss();
                        if (t instanceof IOException) {
                            Toast.makeText(mCtx, "Internet not connected", Toast.LENGTH_SHORT).show();
                            // logging probably not necessary
                        }
                    }
                });

            } else {
                Toast.makeText(mCtx, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}