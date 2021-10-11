package com.school.iqdigit.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.school.iqdigit.R;
import com.school.iqdigit.interfaces.GetClikedImagePath;
import java.util.ArrayList;

public class AssessmentImagesAdapter extends RecyclerView.Adapter<AssessmentImagesAdapter.ViewHolder>  {
    private Activity mBaseActivity;
    private ArrayList<String> images_list = new ArrayList<>();
    private GetClikedImagePath getClikedImagePath;


    public AssessmentImagesAdapter(Activity mBaseActivity, ArrayList<String> images_list, GetClikedImagePath getClikedImagePath) {
        this.mBaseActivity = mBaseActivity;
        this.images_list = images_list;
        this.getClikedImagePath = getClikedImagePath;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_list, img_close;

        public ViewHolder(View v) {
            super(v);
            img_list = v.findViewById(R.id.img_list);
            img_close = v.findViewById(R.id.img_close);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mBaseActivity).inflate(R.layout.images_layout, parent, false);
        ViewHolder vs = new ViewHolder(v);
        v.setTag(vs);
        return vs;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mBaseActivity)
                .load(Uri.parse(images_list.get(position)))
                .into(holder.img_list);
        holder.img_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClikedImagePath.savePath(images_list.get(position));
            }
        });

        holder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClikedImagePath.deleteImage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images_list.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
