package com.example.crimnoticsapp.AfterScreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crimnoticsapp.BasicActivities.Login;
import com.example.crimnoticsapp.R;

public class RegConfirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_confirm);
    }
    public  void gotoLogin(View view)
    {
        Intent intent = new Intent(RegConfirm.this, Login.class);
        startActivity(intent);
        finish();
    }
}