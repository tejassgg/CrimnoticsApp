package com.example.crimnoticsapp.AfterScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crimnoticsapp.DashboardFiles.Dashboard;
import com.example.crimnoticsapp.R;

public class AfterMissingPerson extends AppCompatActivity {
    Button goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_missing_person);

        goBack = findViewById(R.id.goBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AfterMissingPerson.this, Dashboard.class));
                finish();
            }
        });
    }
}