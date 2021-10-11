package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.school.iqdigit.R;

public class FeeStructureViewActivity extends AppCompatActivity {
private TextView tvFee;
private ImageView imgFee;
private ImageView backbtnfee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_structure_view);
        imgFee = findViewById(R.id.imgFee);
        backbtnfee = findViewById(R.id.backbtnfee);
        Bundle bundle = getIntent().getExtras();
        String img = bundle.getString("img");
        String name = bundle.getString("name");
        tvFee = findViewById(R.id.tvfee);
        tvFee.setText(name);
        Glide.with(getApplicationContext()).load(img).into(imgFee);
        backbtnfee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
