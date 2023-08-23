package com.school.iqdigit.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.school.iqdigit.Model.GetPhoto;
import com.school.iqdigit.R;
import com.school.iqdigit.interfaces.GalleryClicked;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>{
    private RelativeLayout progressbar;
    private List<GetPhoto> items;
    private Context mContext;
    GalleryClicked galleryClicked;

    public GalleryAdapter( RelativeLayout progressbar, List<GetPhoto> items, Context mContext, GalleryClicked galleryClicked) {
        this.progressbar = progressbar;
        this.items = items;
        this.mContext = mContext;
        this.galleryClicked = galleryClicked;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_gallery,parent,false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        GetPhoto item = items.get(position);

        if(item.getDescription() != null && !item.getDescription().equals("") ) holder.item_name.setText(item.getDescription());
        else holder.item_name.setText("N/A");

        if(item.getImg() != null && !item.getImg().equals("") ) Glide.with(mContext).load(item.getImg()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressbar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressbar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.item_image);
        else Glide.with(mContext).load(ContextCompat.getDrawable(mContext, R.drawable.ic_blank_image)).into(holder.item_image);

        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryClicked.getId(position,items);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder{
        private TextView item_name;
        private ImageView item_image;
        private ProgressBar progressbar;
        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_title);
            progressbar = itemView.findViewById(R.id.fabProgress);
        }
    }
}
