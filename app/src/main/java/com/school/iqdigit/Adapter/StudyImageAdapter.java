package com.school.iqdigit.Adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.school.iqdigit.Activity.AddBookletActivity;
import com.school.iqdigit.Activity.imageActivity;
import com.school.iqdigit.Activity.imageActivity1;
import com.school.iqdigit.Model.Image;
import com.school.iqdigit.Model.Media;
import com.school.iqdigit.Modeldata.AssSub;
import com.school.iqdigit.R;

import java.util.List;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class StudyImageAdapter extends RecyclerView.Adapter<StudyImageAdapter.AssSubHolder> {
    private Context mCtx;
    private List<Image> assSubList;
    DownloadManager downloadManager;
    private String user_type;
    private String chapterid;
    private String chaptername;
    private String subject_id;

    public StudyImageAdapter(Context mCtx,List<Image> assSubList,String user_type,String chapterid,String chaptername,String subject_id) {
        this.mCtx = mCtx;
        this.assSubList = assSubList;
        this.user_type = user_type;
        this.chapterid = chapterid;
        this.chaptername = chaptername;
        this.subject_id = subject_id;
    }

    @NonNull
    @Override
    public AssSubHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_study_images, viewGroup, false);
        return new AssSubHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AssSubHolder assSubHolder, int i) {
        if(user_type.equals("student"))
        {
            assSubHolder.imgEdit.setVisibility(View.GONE);
        }else if(user_type.equals("staff")){
            assSubHolder.imgEdit.setVisibility(View.VISIBLE);
        }
        final Image assSub = assSubList.get(i);
        assSubHolder.title.setText(assSub.getBook_name());

        final String imgeurl = assSub.getMedia_url();
        Glide.with(mCtx).load(imgeurl).into(assSubHolder.imageView);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            assSubHolder.description.setText(Html.fromHtml(assSub.getBook_desc(),Html.FROM_HTML_MODE_LEGACY));
            assSubHolder.description.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            assSubHolder.description.setText(Html.fromHtml(assSub.getBook_desc()));
            assSubHolder.description.setMovementMethod(LinkMovementMethod.getInstance());
        }
        assSubHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, imageActivity1.class);
                intent.putExtra("imgurl", imgeurl);
                mCtx.startActivity(intent);
            }
        });
        assSubHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadManager = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(assSub.getMedia_url());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long refrence = downloadManager.enqueue(request);
                Toast.makeText(mCtx,"File is downloaded successfully and saved is downloads",Toast.LENGTH_LONG).show();
            }
        });
        assSubHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, AddBookletActivity.class);
                intent.putExtra("title",assSub.getBook_name());
                intent.putExtra("lesson",assSub.getBook_desc());
                intent.putExtra("action", "edit");
                intent.putExtra("chapterid",chapterid);
                intent.putExtra("subject_id",subject_id);
                intent.putExtra("chapter_name",chaptername);
                intent.putExtra("running_id",String.valueOf(assSub.getBooklet_id()));
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assSubList.size();
    }

    class AssSubHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, description;
        ImageButton download;
        ImageView imgEdit;

        public AssSubHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ass_title);
            description = itemView.findViewById(R.id.ass_deription);
            download = itemView.findViewById(R.id.ass_download);
            imageView = itemView.findViewById(R.id.download_image);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }
}
