package com.school.iqdigit.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.school.iqdigit.Activity.FeeStructureViewActivity;
import com.school.iqdigit.Model.Achievement_;
import com.school.iqdigit.R;

import java.util.List;


public class FeeStructureAdapter extends RecyclerView.Adapter<FeeStructureAdapter.AchievementHolder>{
        private Context mCtx;
        private List<Achievement_> achievementsList;

        public FeeStructureAdapter(Context mCtx, List<Achievement_> achievementsList) {
            this.mCtx = mCtx;
            this.achievementsList = achievementsList;
        }

        @NonNull
        @Override
        public AchievementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_achievements,viewGroup,false);

            return new AchievementHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final AchievementHolder achievememntHolder, int i) {
            achievememntHolder.imgbackground.setBackgroundColor(Color.parseColor("#90EE90"));
            final Achievement_ achievement = achievementsList.get(i);
            achievememntHolder.title.setText(achievement.getTitle());
            achievememntHolder.description.setText(achievement.getDescription());
            final String imgeurl = achievement.getImg();
            Glide.with(mCtx).load(imgeurl).into( achievememntHolder.imageView);

            achievememntHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, FeeStructureViewActivity.class);
                    intent.putExtra("img", imgeurl);
                    intent.putExtra("name",achievement.getTitle()+"");
                    mCtx.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return achievementsList.size();
        }

        class AchievementHolder extends RecyclerView.ViewHolder{
            ImageView imageView,imgbackground;
            TextView title,description;

            public AchievementHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tvtitle);
                description = itemView.findViewById(R.id.tvdesription);
                imageView = itemView.findViewById(R.id.download_image);
                imgbackground = itemView.findViewById(R.id.imgbackground);
            }
        }
    }


