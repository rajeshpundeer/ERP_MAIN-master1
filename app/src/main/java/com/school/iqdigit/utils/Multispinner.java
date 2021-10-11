package com.school.iqdigit.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.school.iqdigit.Modeldata.Classes;
import com.school.iqdigit.R;

import java.util.List;

public class Multispinner extends RecyclerView.Adapter<Multispinner.Multispinnerholder>{
    private Context mmCtx;
    private List<Classes> classes;
    private  String[] cl_id ;

    public Multispinner(Context mmCtx, List<Classes> classes) {
        this.mmCtx = mmCtx;
        this.classes = classes;
    }

    @NonNull
    @Override
    public Multispinnerholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mmCtx).inflate(R.layout.recyele_multispinner,viewGroup,false);

        return new Multispinnerholder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final Multispinnerholder holder, int i) {
        Classes classel = classes.get(i);
        cl_id = new String[classes.size()];

        holder.msclass.setText(classel.getClass_name());
        holder.mscheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.mscheck.isChecked()){
                    holder.mscheck.setChecked(false);
                }else{
                    holder.mscheck.setChecked(true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    class Multispinnerholder extends RecyclerView.ViewHolder{
        TextView msclass;
        CheckBox mscheck;
        public Multispinnerholder(@NonNull View itemView) {
            super(itemView);
            msclass = itemView.findViewById(R.id.mscheck);
            mscheck = itemView.findViewById(R.id.mscheck);
        }
    }
}
