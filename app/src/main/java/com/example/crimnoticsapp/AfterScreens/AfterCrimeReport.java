package com.example.crimnoticsapp.AfterScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crimnoticsapp.DashboardFiles.Dashboard;
import com.example.crimnoticsapp.R;

public class AfterCrimeReport extends AppCompatActivity {

    Button goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_crime_report);

        goBack = findViewById(R.id.goBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AfterCrimeReport.this, Dashboard.class));
                finish();
            }
        });
    }
}