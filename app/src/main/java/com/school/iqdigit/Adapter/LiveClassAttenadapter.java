package com.school.iqdigit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.iqdigit.Model.LiveAttendanceClas;
import com.school.iqdigit.R;

import java.util.ArrayList;
import java.util.List;

//Timetable RecyclerView Adapter
    public class LiveClassAttenadapter extends RecyclerView.Adapter<LiveClassAttenadapter.LiveAttendanceholder> {
        private Context mCtx;
        private List<LiveAttendanceClas> LiveAttendanceList = new ArrayList<>();

        public LiveClassAttenadapter(Context mCtx, List<LiveAttendanceClas> LiveAttendanceList) {
            this.mCtx = mCtx;
            this.LiveAttendanceList = LiveAttendanceList;
        }

        @NonNull
        @Override
        public LiveAttendanceholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_class_live, viewGroup, false);
            return new LiveAttendanceholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LiveAttendanceholder timetableholder, int i) {
            LiveAttendanceClas liveAttendanceClas = LiveAttendanceList.get(i);

            timetableholder.tvStudentName.setText(liveAttendanceClas.getStudname());
            timetableholder.tvClassName.setText(String.valueOf(liveAttendanceClas.getClasses()));
        }

        @Override
        public int getItemCount() {
            return LiveAttendanceList.size();
        }

        class LiveAttendanceholder extends RecyclerView.ViewHolder {
            TextView tvStudentName, tvClassName;

            public LiveAttendanceholder(@NonNull View itemView) {
                super(itemView);
                tvStudentName = itemView.findViewById(R.id.tvStudentName);
                tvClassName = itemView.findViewById(R.id.tvClass);
            }
        }
    }
