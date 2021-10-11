package com.school.iqdigit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.iqdigit.Model.LiveAttendanceStud;
import com.school.iqdigit.R;

import java.util.ArrayList;
import java.util.List;

//Timetable RecyclerView Adapter
    public class LiveStudAttenadapter extends RecyclerView.Adapter<LiveStudAttenadapter.LiveAttendanceholder> {
        private Context mCtx;
        private List<LiveAttendanceStud> LiveAttendanceList = new ArrayList<>();

        public LiveStudAttenadapter(Context mCtx, List<LiveAttendanceStud> LiveAttendanceList) {
            this.mCtx = mCtx;
            this.LiveAttendanceList = LiveAttendanceList;
        }

        @NonNull
        @Override
        public LiveAttendanceholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_stud_live, viewGroup, false);
            return new LiveAttendanceholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LiveAttendanceholder timetableholder, int i) {
            LiveAttendanceStud liveAttendanceStud = LiveAttendanceList.get(i);

            timetableholder.tvClassName.setText(liveAttendanceStud.getClassTitle());
            timetableholder.tvTeacher.setText(liveAttendanceStud.getStaffname());
            timetableholder.tvTime.setText(liveAttendanceStud.getJoinTime());
        }

        @Override
        public int getItemCount() {
            return LiveAttendanceList.size();
        }

        class LiveAttendanceholder extends RecyclerView.ViewHolder {
            TextView tvClassName, tvTime, tvTeacher;

            public LiveAttendanceholder(@NonNull View itemView) {
                super(itemView);
                tvClassName = itemView.findViewById(R.id.tvClassName);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvTeacher = itemView.findViewById(R.id.tvTeacher);
            }
        }
    }
