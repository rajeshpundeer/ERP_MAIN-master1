package com.school.iqdigit.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.school.iqdigit.Activity.imageActivity;
import com.school.iqdigit.Model.Achievement_;
import com.school.iqdigit.Modeldata.AssSub;
import com.school.iqdigit.R;

import java.util.List;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementHolder>{
    private Context mCtx;
    private List<Achievement_> achievementsList;

    public AchievementAdapter(Context mCtx, List<Achievement_> achievementsList) {
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
        final Achievement_ achievement = achievementsList.get(i);
        achievememntHolder.title.setText(achievement.getTitle());
        achievememntHolder.description.setText(achievement.getDescription());
        final String imgeurl = achievement.getImg();
        Glide.with(mCtx).load(imgeurl).into( achievememntHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return achievementsList.size();
    }

    class AchievementHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title,description;

        public AchievementHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvtitle);
            description = itemView.findViewById(R.id.tvdesription);
            imageView = itemView.findViewById(R.id.download_image);
        }
    }
}
