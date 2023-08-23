package com.school.iqdigit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.iqdigit.Model.StudgroupsItem;
import com.school.iqdigit.R;
import com.school.iqdigit.interfaces.SingleItemClick;

import java.util.List;

public class SingleSelectAdapter extends RecyclerView.Adapter<SingleSelectAdapter.SingleSelectViewHolder> {


    private List<StudgroupsItem> studgroups;
    private Context context;
    private SingleItemClick singleItemClick;
    private int lastCheckedPosition = -1;

    public SingleSelectAdapter(List<StudgroupsItem> studgroups, Context context, SingleItemClick singleItemClick) {
        this.studgroups = studgroups;
        this.context = context;
        this.singleItemClick = singleItemClick;
    }

    @NonNull
    @Override
    public SingleSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.class_recyclerview,null,false);
       return new SingleSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleSelectViewHolder holder, int position) {

       holder.textView.setText(studgroups.get(position).getGroupName());

        holder.imageView.setVisibility(position == lastCheckedPosition ? View.VISIBLE : View.GONE);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               lastCheckedPosition =position;
               singleItemClick.click(studgroups.get(position));
               notifyDataSetChanged();
           }
       });

    }

    @Override
    public int getItemCount() {
        return studgroups.size();
    }

    class SingleSelectViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;
        public SingleSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
