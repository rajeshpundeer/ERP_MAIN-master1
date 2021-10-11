package com.school.iqdigit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.school.iqdigit.Model.McqStudAnsWithKey;
import com.school.iqdigit.R;

import java.util.ArrayList;
import java.util.List;

public class AnsKeyadapter extends RecyclerView.Adapter<AnsKeyadapter.AnsKeyholder> {
    private Context mCtx;
    private List<McqStudAnsWithKey> McqStudAnsWithKeyList = new ArrayList<>();
    private String status;

    public AnsKeyadapter(Context mCtx, List<McqStudAnsWithKey> McqStudAnsWithKeyList,String status) {
        this.mCtx = mCtx;
        this.McqStudAnsWithKeyList = McqStudAnsWithKeyList;
        this.status = status;
    }

    @NonNull
    @Override
    public AnsKeyholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_viewans, viewGroup, false);
        return new AnsKeyholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnsKeyholder anskeyholder, int i) {
          anskeyholder.tvQuestionno.setText(String.valueOf(McqStudAnsWithKeyList.get(i).getMcqId()));
          if(String.valueOf(McqStudAnsWithKeyList.get(i).getAnsstud()).equals("1")) {
              anskeyholder.tvSubmittedAns.setText("A");
          }else if(String.valueOf(McqStudAnsWithKeyList.get(i).getAnsstud()).equals("2")) {
              anskeyholder.tvSubmittedAns.setText("B");
          }else if(String.valueOf(McqStudAnsWithKeyList.get(i).getAnsstud()).equals("3")) {
              anskeyholder.tvSubmittedAns.setText("C");
          }else if(String.valueOf(McqStudAnsWithKeyList.get(i).getAnsstud()).equals("4")) {
              anskeyholder.tvSubmittedAns.setText("D");
          }
        if(String.valueOf(McqStudAnsWithKeyList.get(i).getCorrectAns()).equals("1")) {
            anskeyholder.tvAns.setText("A");
        }else if(String.valueOf(McqStudAnsWithKeyList.get(i).getCorrectAns()).equals("2")) {
            anskeyholder.tvAns.setText("B");
        }else if(String.valueOf(McqStudAnsWithKeyList.get(i).getCorrectAns()).equals("3")) {
            anskeyholder.tvAns.setText("C");
        }else if(String.valueOf(McqStudAnsWithKeyList.get(i).getCorrectAns()).equals("4")) {
            anskeyholder.tvAns.setText("D");
        }

        if(status.equals("submitted_student")){
            anskeyholder.tvAns.setVisibility(View.GONE);
            anskeyholder.tvQuestionno.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.ic_btn_view_three));
            anskeyholder.tvSubmittedAns.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.ic_btn_view_three));
        }else {
          if(McqStudAnsWithKeyList.get(i).getAnsstud() == McqStudAnsWithKeyList.get(i).getCorrectAns())
          {
              anskeyholder.tvQuestionno.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.ic_btn_view_five));
              anskeyholder.tvAns.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.ic_btn_view_five));
              anskeyholder.tvSubmittedAns.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.ic_btn_view_five));
          }else {
              anskeyholder.tvQuestionno.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.ic_btn_view_six));
              anskeyholder.tvAns.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.ic_btn_view_six));
              anskeyholder.tvSubmittedAns.setBackground(ContextCompat.getDrawable(mCtx,R.drawable.ic_btn_view_six));
          }
        }
    }

    @Override
    public int getItemCount() {
        return McqStudAnsWithKeyList.size();
    }

    class AnsKeyholder extends RecyclerView.ViewHolder {
        TextView tvQuestionno,tvSubmittedAns,tvAns;

        public AnsKeyholder(@NonNull View itemView) {
            super(itemView);
            tvQuestionno = itemView.findViewById(R.id.tvQuestionno);
            tvSubmittedAns = itemView.findViewById(R.id.tvSubmittedAns);
            tvAns = itemView.findViewById(R.id.tvAns);
        }
    }

}