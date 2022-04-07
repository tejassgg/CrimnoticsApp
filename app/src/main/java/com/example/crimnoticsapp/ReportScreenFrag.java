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

public class ReportScreenFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String image,type, address, date, time, desc,application,appID;
    Button button;

    public ReportScreenFrag() {
    }

    public ReportScreenFrag(String image, String type, String address, String date, String time, String desc,String application,String appID) {
        this.image = image;
        this.type = type;
        this.address = address;
        this.date = date;
        this.time = time;
        this.desc = desc;
        this.application = application;
        this.appID = appID;
    }

    public static ReportScreenFrag newInstance(String param1, String param2) {
        ReportScreenFrag fragment = new ReportScreenFrag();
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
        View view = inflater.inflate(R.layout.fragment_report_screen, container, false);

        button = (Button)view.findViewById(R.id.backButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapperUserRC,new ReportedCrimeFrag()).addToBackStack(null).commit();
            }
        });

        ImageView reportImage = view.findViewById(R.id.reportImage);
        TextView typeCrime = view.findViewById(R.id.typeIssue1);
        TextView add1 = view.findViewById(R.id.add1);
        TextView date1 = view.findViewById(R.id.date1);
        TextView time1 = view.findViewById(R.id.time1);
        TextView desc1 = view.findViewById(R.id.desc1);
        TextView application1 = view.findViewById(R.id.application1);
        TextView appID1= view.findViewById(R.id.appID1);

        typeCrime.setText(type);
        add1.setText(address);
        date1.setText(date);
        time1.setText(time);
        desc1.setText(desc);
        application1.setText(application);
        appID1.setText(appID);
        Glide.with(getContext()).load(image).into(reportImage);

        return  view;

    }

    public void onBackPressed()
    {
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapperUserRC,new ReportedCrimeFrag()).addToBackStack(null).commit();
    }
}