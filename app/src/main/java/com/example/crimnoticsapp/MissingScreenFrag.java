package com.example.crimnoticsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class MissingScreenFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String name,age,height,date,lastSeen,gender,desc,image,application,appID;
    Button button;


    public MissingScreenFrag() {
    }

    public MissingScreenFrag(String name, String age, String height, String date, String lastSeen, String gender, String desc,String image,String application,String appID) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.date = date;
        this.lastSeen = lastSeen;
        this.gender = gender;
        this.desc = desc;
        this.image = image;
        this.application = application;
        this.appID = appID;
    }

    public static MissingScreenFrag newInstance(String param1, String param2) {
        MissingScreenFrag fragment = new MissingScreenFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_missing_screen, container, false);

        button = (Button)view.findViewById(R.id.backButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapperUserMP,new MissedPersonFrag()).addToBackStack(null).commit();
            }
        });

        ImageView reportImage = view.findViewById(R.id.reportImage);
        TextView name1 = view.findViewById(R.id.name1);
        TextView age1 = view.findViewById(R.id.age1);
        TextView height1 = view.findViewById(R.id.height1);
        TextView time1 = view.findViewById(R.id.time1);
        TextView lastSeen1 = view.findViewById(R.id.area1);
        TextView gender1 = view.findViewById(R.id.gender1);
        TextView desc1 = view.findViewById(R.id.desc);
        TextView application1 = view.findViewById(R.id.application1);
        TextView appID1= view.findViewById(R.id.appID1);


        name1.setText(name);
        age1.setText(age);
        height1.setText(height);
        time1.setText(date);
        lastSeen1.setText(lastSeen);
        gender1.setText(gender);
        desc1.setText(desc);
        application1.setText(application);
        appID1.setText(appID);
        Glide.with(getContext()).load(image).into(reportImage);

        return view;
    }

    public void onBackPressed()
    {
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapperUserMP,new MissedPersonFrag()).addToBackStack(null).commit();
    }

}