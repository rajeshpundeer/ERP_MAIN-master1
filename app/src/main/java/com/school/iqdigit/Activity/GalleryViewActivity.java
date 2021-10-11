package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.school.iqdigit.Adapter.GallerySlideAdapter;
import com.school.iqdigit.Model.GetPhoto;
import com.school.iqdigit.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private GallerySlideAdapter adapter;
    private List<String> getPhotos = new ArrayList<>();
    private String position;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);
        back = findViewById(R.id.backbtn);
        Bundle bundle = getIntent().getExtras();
        getPhotos = bundle.getStringArrayList("list");
        position = bundle.getString("position");

        viewPager = (ViewPager) findViewById(R.id.viewPager_id);
        adapter = new GallerySlideAdapter(this,getPhotos);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.parseInt(position));

        int i = viewPager.getCurrentItem();
        if(i==getPhotos.size()){
            i=0;
            viewPager.setCurrentItem(i, true);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}