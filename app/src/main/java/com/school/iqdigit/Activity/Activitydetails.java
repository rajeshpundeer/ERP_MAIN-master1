package com.school.iqdigit.Activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.school.iqdigit.R;
import com.school.iqdigit.utility.InternetCheck;

import static com.school.iqdigit.Api.RetrofitClient.BASE_URL2;

public class Activitydetails extends AppCompatActivity {
    Intent mainintent;
    TextView title, desc, credon;
    ImageView actid_image,bk_btn,actid_imagefull;
    ImageButton accdownload;
    private boolean checkinternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitydetails);
        mainintent = getIntent();
        title = findViewById(R.id.actid_title);
        desc = findViewById(R.id.actid_description);
        credon = findViewById(R.id.actid_createdon);
        actid_image = findViewById(R.id.actid_image);
        actid_imagefull = findViewById(R.id.actid_imagefull);
        accdownload = findViewById(R.id.ac_download);
        bk_btn = findViewById(R.id.bac_btn);
        title.setText(mainintent.getStringExtra("title"));
        desc.setText(mainintent.getStringExtra("description"));
        credon.setText(mainintent.getStringExtra("createdon"));
        Glide.with(Activitydetails.this).load(  mainintent.getStringExtra("img")).into(actid_image);
        Glide.with(Activitydetails.this).load( mainintent.getStringExtra("img")).into(actid_imagefull);
        actid_image.setAdjustViewBounds(true);
        checkinternet = InternetCheck.isInternetOn(Activitydetails.this);
        actid_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Activitydetails.this, imageActivity.class);
                intent2.putExtra("imgurl",  mainintent.getStringExtra("img"));
                startActivity(intent2);
            }
        });

        accdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String downloadlink =  mainintent.getStringExtra("img") ;
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse( mainintent.getStringExtra("img"));
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long refrence = downloadManager.enqueue(request);
            }
        });

        bk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actid_imagefull.getVisibility() == View.VISIBLE){
                    actid_image.setVisibility(View.VISIBLE);
                    actid_imagefull.setVisibility(View.GONE);
                }else{
                    finish();
                }
            }
        });
    }
}
