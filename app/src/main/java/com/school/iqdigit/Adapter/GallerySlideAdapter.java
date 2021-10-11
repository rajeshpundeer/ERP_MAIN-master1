package com.school.iqdigit.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.school.iqdigit.Model.GetPhoto;
import com.school.iqdigit.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GallerySlideAdapter extends PagerAdapter {

    private Context context;
    LayoutInflater inflater;
    List<String> getPhotos = new ArrayList<>();

    public GallerySlideAdapter(Context context, List<String> getPhotos) {
        this.context = context;
        this.getPhotos = getPhotos;
    }

    @Override
    public int getCount() {
        return getPhotos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout) o);
    }

    public GallerySlideAdapter() {
        super();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slideshow_layout,container,false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.imageView_id);
        Glide.with(context).load(getPhotos.get(position)).into(photoView);
        container.addView(view);
        return  view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
