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
import com.school.iqdigit.Activity.AddTutorialActivity;
import com.school.iqdigit.Activity.TutorialViewActivity;
import com.school.iqdigit.Model.Achievement_;
import com.school.iqdigit.Model.Tutorials;
import com.school.iqdigit.R;

import java.util.List;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.AchievementHolder>{
    private Context mCtx;
    private List<Tutorials> tutorialsList;
    private String user_type;
    private String chapterid;
    private String chaptername;
    private String subject_id;

    public TutorialAdapter(Context mCtx, List<Tutorials> tutorialsList,String user_type,String chapterid,String chaptername,String subject_id) {
        this.mCtx = mCtx;
        this.tutorialsList = tutorialsList;
        this.user_type = user_type;
        this.chapterid = chapterid;
        this.chaptername = chaptername;
        this.subject_id = subject_id;
    }

    @NonNull
    @Override
    public AchievementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_tutorial,viewGroup,false);
        return new AchievementHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AchievementHolder achievememntHolder, int i) {
        if(user_type.equals("student"))
        {
            achievememntHolder.imgEdit.setVisibility(View.GONE);
        }else if(user_type.equals("staff")){
            achievememntHolder.imgEdit.setVisibility(View.VISIBLE);
        }
        final Tutorials tutorials = tutorialsList.get(i);
        achievememntHolder.title.setText(tutorials.getTitle());
        achievememntHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, TutorialViewActivity.class);
                intent.putExtra("title",tutorials.getTitle());
                intent.putExtra("lesson",tutorials.getLesson());
                intent.putExtra("summary",tutorials.getSummary());
                intent.putExtra("description",tutorials.getTut_desc());
                intent.putExtra("fileurl",tutorials.getFile_path());
                mCtx.startActivity(intent);
            }
        });
        achievememntHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, AddTutorialActivity.class);
                intent.putExtra("title",tutorials.getTitle());
                intent.putExtra("lesson",tutorials.getLesson());
                intent.putExtra("action", "edit");
                intent.putExtra("chapterid",chapterid);
                intent.putExtra("subject_id",subject_id);
                intent.putExtra("chapter_name",chaptername);
                intent.putExtra("running_id",String.valueOf(tutorials.getTut_id()));
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutorialsList.size();
    }

    class AchievementHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView imgEdit;

        public AchievementHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }
}
