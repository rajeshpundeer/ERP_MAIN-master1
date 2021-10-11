package com.school.iqdigit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.iqdigit.Activity.Assessment1Activity;
import com.school.iqdigit.Activity.imageActivity;
import com.school.iqdigit.Activity.imageActivity1;
import com.school.iqdigit.Model.Assessment_list;
import com.school.iqdigit.Model.GetAssessmentListRoot;
import com.school.iqdigit.R;
import com.school.iqdigit.interfaces.RemoveSubmittedAssessment;

import java.util.List;

public class PendingStaffAdapter extends RecyclerView.Adapter<PendingStaffAdapter.Pendingholder> {
    private Context mCtx;
    private List<GetAssessmentListRoot> pendingList;

    public PendingStaffAdapter(Context mCtx, List<GetAssessmentListRoot> pendingList) {
        this.mCtx = mCtx;
        this.pendingList = pendingList;
    }

    @NonNull
    @Override
    public Pendingholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_assessment_pending, viewGroup, false);
        return new Pendingholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pendingholder holder, int i) {
        GetAssessmentListRoot pendingAssessment = pendingList.get(i);
        holder.StudentDetail.setText("[ "+pendingAssessment.getAdmissionId()+" ] "+pendingAssessment.getStudentName());

    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    class Pendingholder extends RecyclerView.ViewHolder {
        TextView StudentDetail;

        public Pendingholder(@NonNull View itemView) {
            super(itemView);
            StudentDetail = itemView.findViewById(R.id.StudentDetail);

        }
    }
}