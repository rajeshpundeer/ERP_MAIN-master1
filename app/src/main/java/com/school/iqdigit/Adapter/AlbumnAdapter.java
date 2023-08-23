package com.school.iqdigit.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.school.iqdigit.Model.Photoalbum;
import com.school.iqdigit.R;
import com.school.iqdigit.interfaces.GetClikedIalbumPath;

import java.util.List;

public class AlbumnAdapter extends RecyclerView.Adapter<AlbumnAdapter.VerticalsViewHolder>{
    private TextView gallerytitle;
    private List<Photoalbum> verticalList;
    private Context mContext;
    private GetClikedIalbumPath getClikedIalbumPath;

    public AlbumnAdapter(TextView gallerytitle, List<Photoalbum> verticalList, Context mContext,GetClikedIalbumPath getClikedIalbumPath) {
        this.gallerytitle = gallerytitle;
        this.verticalList = verticalList;
        this.mContext = mContext;
        this.getClikedIalbumPath = getClikedIalbumPath;
    }

    @NonNull
    @Override
    public VerticalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_albumn,parent,false);
        return new VerticalsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VerticalsViewHolder holder, int position) {
        Photoalbum vertical = verticalList.get(position);

        if(vertical.getName() != null && !vertical.getName().equals("") ) holder.albumnName.setText(vertical.getName());
        else holder.albumnName.setText("N/A");

        if(vertical.getImg() != null && !vertical.getImg().equals("") ) Glide.with(mContext).load(vertical.getImg()).into(holder.albumnImage);
        else Glide.with(mContext).load(ContextCompat.getDrawable(mContext, R.drawable.ic_home_recycle_verticle_gradient)).into(holder.albumnImage);

        holder.veritalLayout.setOnClickListener(v -> {
            gallerytitle.setText(vertical.getName());
            getClikedIalbumPath.getId(String.valueOf(vertical.getId()));
            notifyDataSetChanged();
        });
    }



    @Override
    public int getItemCount() {
        return verticalList.size();
    }

    public static class VerticalsViewHolder extends RecyclerView.ViewHolder{
        private TextView albumnName;
        private ImageView albumnImage;
        private androidx.cardview.widget.CardView veritalLayout;
        public VerticalsViewHolder(@NonNull View itemView) {
            super(itemView);
            albumnName = itemView.findViewById(R.id.albumnName);
            albumnImage = itemView.findViewById(R.id.albumnImage);
            veritalLayout = itemView.findViewById(R.id.veritalLayout);
        }
    }
}
