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
import com.school.iqdigit.Activity.AddBookletActivity;
import com.school.iqdigit.Activity.AddPdfActivity;
import com.school.iqdigit.Activity.BookletViewActivity;
import com.school.iqdigit.Model.Achievement_;
import com.school.iqdigit.Model.Booklets;
import com.school.iqdigit.R;

import java.util.List;

public class BookletsAdapter extends RecyclerView.Adapter<BookletsAdapter.AchievementHolder>{
    private Context mCtx;
    private List<Booklets> bookletList;
    private String user_type;
    private String chapterid;
    private String chaptername;
    private String subject_id;

    public BookletsAdapter(Context mCtx, List<Booklets> bookletList,String user_type,String chapterid,String chaptername,String subject_id) {
        this.mCtx = mCtx;
        this.bookletList = bookletList;
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
        final Booklets booklets = bookletList.get(i);
        achievememntHolder.title.setText(booklets.getBook_name());
        achievememntHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, BookletViewActivity.class);
                intent.putExtra("name",booklets.getBook_name());
                intent.putExtra("description",booklets.getBook_desc());
                intent.putExtra("fileurl",booklets.getBook_file());
                mCtx.startActivity(intent);
            }
        });
        achievememntHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, AddPdfActivity.class);
                intent.putExtra("title",booklets.getBook_name());
                intent.putExtra("lesson",booklets.getBook_desc());
                intent.putExtra("action", "edit");
                intent.putExtra("chapterid",chapterid);
                intent.putExtra("subject_id",subject_id);
                intent.putExtra("chapter_name",chaptername);
                intent.putExtra("running_id",String.valueOf(booklets.getBooklet_id()));
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookletList.size();
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
