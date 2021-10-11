package com.school.iqdigit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.school.iqdigit.R;

public class MaintenanceActivity extends AppCompatActivity {
private TextView tvMainText;
private String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");
        tvMainText = findViewById(R.id.tvMaintext);
        tvMainText.setText(message);
    }
}
