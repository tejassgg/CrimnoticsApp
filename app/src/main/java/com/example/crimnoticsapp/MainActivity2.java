package com.example.crimnoticsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crimnoticsapp.Admin.AdminLogin;
import com.example.crimnoticsapp.BasicActivities.Login;

public class MainActivity2 extends AppCompatActivity {

    Button admin, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        admin  = findViewById(R.id.admin);
        user = findViewById(R.id.user);


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminLogin.class));
                finish();
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, Login.class));
            }
        });

    }
}