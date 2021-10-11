package com.school.iqdigit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.school.iqdigit.Activity.ProfileActivity;
import com.school.iqdigit.Model.Birthday;
import com.school.iqdigit.Model.GetAssessmentListRoot;
import com.school.iqdigit.R;

import java.util.List;

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.Pendingholder> {
    private Context mCtx;
    private List<Birthday> birthdayList;

    public BirthdayAdapter(Context mCtx, List<Birthday> birthdayList) {
        this.mCtx = mCtx;
        this.birthdayList = birthdayList;
    }

    @NonNull
    @Override
    public Pendingholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycle_birthday, viewGroup, false);
        return new Pendingholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Pendingholder holder, int i) {
        Birthday birthday = birthdayList.get(i);
        holder.StudentDetail.setText("[ "+birthday.getClass_()+" ] "+birthday.getFname());

        Glide.with(mCtx)
                .load(birthday.getPhoto())
                .placeholder(ContextCompat.getDrawable(mCtx,  R.drawable.user_icon))
                .into( holder.imageView);

    }

    @Override
    public int getItemCount() {
        return birthdayList.size();
    }

    class Pendingholder extends RecyclerView.ViewHolder {
        TextView StudentDetail;
        ImageView imageView;

        public Pendingholder(@NonNull View itemView) {
            super(itemView);
            StudentDetail = itemView.findViewById(R.id.StudentDetail);
            imageView = itemView.findViewById(R.id.imgStudent);

        }
    }
}