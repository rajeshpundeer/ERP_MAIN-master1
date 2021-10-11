package com.school.iqdigit.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.school.iqdigit.Modeldata.Attend;
import com.school.iqdigit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendAdapter extends RecyclerView.Adapter<AttendAdapter.AttendHolder>{
    private Context mmCtx;
    private List<Attend> attendList;

    public AttendAdapter(Context mmCtx, List<Attend> attendList) {
        this.mmCtx = mmCtx;
        this.attendList = attendList;
    }

    @NonNull
    @Override
    public AttendHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mmCtx).inflate(R.layout.recycle_attend_of_users,viewGroup,false);
        return new AttendHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AttendHolder attendHolder, int i) {
        Attend attend = attendList.get(i);
       // attendHolder.date.setText(attend.getAttendence_date());



        String dateTime = attend.getAttendence_date();
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = null;
        try {
            date = dateParser.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy");
        attendHolder.date.setText(dateFormatter.format(date));

        String Attend_status = attend.getAttendence();
        if(Attend_status.equals("P")){
            attendHolder.status.setTextColor(Color.GREEN);
            attendHolder.status.setText("Present");
        }else if(Attend_status.equals("LP")){
            attendHolder.status.setTextColor(R.color.colorPrimary);
            attendHolder.status.setText("Late Present");
        }else if(Attend_status.equals("A")){
            attendHolder.status.setTextColor(Color.RED);
            attendHolder.status.setText("Absent");
        }else if(Attend_status.equals("L")){
            attendHolder.status.setTextColor(R.color.Grey_dark);
            attendHolder.status.setText("Leave");
        }else if(Attend_status.equals("H")){
            attendHolder.status.setTextColor(R.color.colorPrimaryDark);
            attendHolder.status.setText("Half day");
        }

    }

    @Override
    public int getItemCount() {
        return attendList.size();
    }

    class AttendHolder extends RecyclerView.ViewHolder{
        TextView date,status;
        public AttendHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.attend_type);
            date = itemView.findViewById(R.id.attend_date);
        }
    }
}
