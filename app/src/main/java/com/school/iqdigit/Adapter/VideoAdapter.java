package com.school.iqdigit.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.school.iqdigit.Activity.AddVideoActivity;
import com.school.iqdigit.Model.Media;
import com.school.iqdigit.Model.Tutorials;
import com.school.iqdigit.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import pl.aprilapps.easyphotopicker.EasyImage;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;
import static java.security.AccessController.getContext;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.AchievementHolder> {
    private Context mCtx;
    private List<Media> mediaList;
    private String TAG = "VideoAdapter";
    private String user_type;
    private String chapterid;
    private String chaptername;
    private String subject_id;

    public VideoAdapter(Context mCtx, List<Media> mediaList,String user_type,String chapterid,String chaptername,String subject_id) {
        this.mCtx = mCtx;
        this.mediaList = mediaList;
        this.user_type = user_type;
        this.chapterid = chapterid;
        this.chaptername = chaptername;
        this.subject_id = subject_id;
    }

    @NonNull
    @Override
    public AchievementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_video, viewGroup, false);
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
        final Media media = mediaList.get(i);
        achievememntHolder.title.setText(media.getBook_name());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            achievememntHolder.tvDescription.setText(Html.fromHtml(media.getBook_desc(), Html.FROM_HTML_MODE_LEGACY));
            achievememntHolder.tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            achievememntHolder.tvDescription.setText(Html.fromHtml(media.getBook_desc()));
            achievememntHolder.tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        }
        String videourl = media.getMedia_url();
        StringTokenizer tokens = new StringTokenizer(videourl, "=");
       // String baseurl = tokens.nextToken();
       // String id = tokens.nextToken();
        //String thumnail = " http://img.youtube.com/vi/"+id+"/0.jpg";
       // Log.d(TAG ,thumnail);
       // Glide.with(mCtx).load(R.drawable.youtube).placeholder(R.drawable.youtubeicon).into(achievememntHolder.imgThumnail);
        achievememntHolder.imgThumnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videourl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                mCtx.startActivity(intent);
            }
        });

        achievememntHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, AddVideoActivity.class);
                intent.putExtra("title",media.getBook_name());
                intent.putExtra("lesson",media.getBook_desc());
                intent.putExtra("action", "edit");
                intent.putExtra("chapterid",chapterid);
                intent.putExtra("subject_id",subject_id);
                intent.putExtra("chapter_name",chaptername);
                intent.putExtra("running_id",String.valueOf(media.getBooklet_id()));
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    class AchievementHolder extends RecyclerView.ViewHolder {
        private TextView title,tvDescription;
        private ImageView imgThumnail;
        private  ImageView imgEdit;

        public AchievementHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitleVideo);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imgThumnail = itemView.findViewById(R.id.imgThumnail);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }

}
