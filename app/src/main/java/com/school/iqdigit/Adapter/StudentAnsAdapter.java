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
import com.school.iqdigit.Activity.ViewAnswerKey;
import com.school.iqdigit.Api.RetrofitClient;
import com.school.iqdigit.Model.DefaultResponse;
import com.school.iqdigit.Model.McqLayout;
import com.school.iqdigit.Model.McqStudAn;
import com.school.iqdigit.Modeldata.User;
import com.school.iqdigit.R;
import com.school.iqdigit.Storage.SharedPrefManager;
import com.school.iqdigit.utility.InternetCheck;
import com.school.iqdigit.utils.SharedHelper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StudentAnsAdapter extends RecyclerView.Adapter<StudentAnsAdapter.UsersViewHolder> {

    private Context mCtx;
    private List<McqLayout> McqLayoutList;
    private String TAG = "ShowLotteryActivity";
    private Dialog dialog;
    private String hw_id;
    private String ans = "";
    private List<McqStudAn> McqStudAnlist;
    private int submittedquestions = 0;
    private int[] submitedanswer;

    public StudentAnsAdapter(Context mCtx, List<McqLayout> McqLayoutList, String hw_id, List<McqStudAn> McqStudAnlist) {
        this.mCtx = mCtx;
        this.McqLayoutList = McqLayoutList;
        this.hw_id = hw_id;
        this.McqStudAnlist = McqStudAnlist;
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
        for (int j = 0; j < McqStudAnlist.size(); j++) {
            Log.d(TAG, McqStudAnlist.get(j).getMcqSno() + " srno1 " + McqLayoutList.get(i).getMcqSno());
            if (String.valueOf(McqStudAnlist.get(j).getMcqSno()).equals(String.valueOf(McqLayoutList.get(i).getMcqSno()))) {
                submitedanswer[j] = McqStudAnlist.get(j).getAnsstud();
                submittedquestions++;
                usersViewHolder.tv_mcqtest.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.ic_btn_view_five));
            }
        }
        if (submittedquestions > 0) {
            SharedHelper.putKey(mCtx, "isAnsSubmitted", "Yes");
        } else {
            SharedHelper.putKey(mCtx, "isAnsSubmitted", "No");
        }
        String mcqid = String.valueOf(McqLayoutList.get(i).getMcqId());
        String mcqno = String.valueOf(McqLayoutList.get(i).getMcqSno());
        usersViewHolder.tv_mcqtest.setText("Q" + mcqno);
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
        TextView tv_title = dialog.findViewById(R.id.tv_title);

        FloatingActionButton bt_close = dialog.findViewById(R.id.bt_close);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tv_title.setText("Choose Ans for Q" + McqLayoutList.get(i).getMcqSno());

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

        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if (InternetCheck.isInternetOn(mCtx) == true) {
                    User user = SharedPrefManager.getInstance(mCtx).getUser();
                    Log.d(TAG, ans + " ans");
                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().setStudentMcqAns(String.valueOf(McqLayoutList.get(i).getMcqId()), user.getId(), ans);
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
                                        Log.d(TAG, response.body().getMsg() + "msg1");
                                        submittedquestions++;
                                    }
                                    if (submittedquestions > 0) {
                                        SharedHelper.putKey(mCtx, "isAnsSubmitted", "Yes");
                                    } else {
                                        SharedHelper.putKey(mCtx, "isAnsSubmitted", "No");
                                    }
                                } else {
                                    dialog.dismiss();
                                    Log.d(TAG, response.body().getMsg() + "msg2");
                                    Toast.makeText(mCtx, "Answer Submition Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                            dialog.dismiss();
                            Log.d(TAG, t.getMessage() + "msg3");
                            if (t instanceof IOException) {
                                Toast.makeText(mCtx, "Internet not connected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mCtx, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
