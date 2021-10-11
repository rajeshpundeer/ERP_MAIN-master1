package com.school.iqdigit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.iqdigit.Model.AnsPreviewStaff;
import com.school.iqdigit.Model.McqStudAnsWithKey;
import com.school.iqdigit.R;

import java.util.ArrayList;
import java.util.List;

public class AnsStaffadapter extends RecyclerView.Adapter<AnsStaffadapter.AnsKeyholder> {
    private Context mCtx;
    private List<AnsPreviewStaff> AnsPreviewStaffList = new ArrayList<>();
    private String status;

    public AnsStaffadapter(Context mCtx, List<AnsPreviewStaff> AnsPreviewStaffList) {
        this.mCtx = mCtx;
        this.AnsPreviewStaffList = AnsPreviewStaffList;
    }

    @NonNull
    @Override
    public AnsKeyholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.rv_ans_view, viewGroup, false);
        return new AnsKeyholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnsKeyholder anskeyholder, int i) {
          anskeyholder.tvQuestionno.setText(String.valueOf(AnsPreviewStaffList.get(i).getMcqId()));
          if(String.valueOf(AnsPreviewStaffList.get(i).getAnsstaff()).equals("1")) {
              anskeyholder.tvSubmittedAns.setText("A");
          }else if(String.valueOf(AnsPreviewStaffList.get(i).getAnsstaff()).equals("2")) {
              anskeyholder.tvSubmittedAns.setText("B");
          }else if(String.valueOf(AnsPreviewStaffList.get(i).getAnsstaff()).equals("3")) {
              anskeyholder.tvSubmittedAns.setText("C");
          }else if(String.valueOf(AnsPreviewStaffList.get(i).getAnsstaff()).equals("4")) {
              anskeyholder.tvSubmittedAns.setText("D");
          }
    }

    @Override
    public int getItemCount() {
        return AnsPreviewStaffList.size();
    }

    class AnsKeyholder extends RecyclerView.ViewHolder {
        TextView tvQuestionno,tvSubmittedAns;

        public AnsKeyholder(@NonNull View itemView) {
            super(itemView);
            tvQuestionno = itemView.findViewById(R.id.tvQuestionno);
            tvSubmittedAns = itemView.findViewById(R.id.tvSubmittedAns);
        }
    }

}