package com.example.crimnoticsapp.Admin.AdminFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.crimnoticsapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AdminReportScreenFrag extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String image,type, address, date, time, desc,application,appID;
    Button updateButton;
    String item;
    Spinner spinner;
    DatabaseReference reference;

    public AdminReportScreenFrag() {
    }

    public AdminReportScreenFrag(String image, String type, String address, String date, String time, String desc, String application,String appID) {
        this.image = image;
        this.type = type;
        this.address = address;
        this.date = date;
        this.time = time;
        this.desc = desc;
        this.application = application;
        this.appID = appID;
    }

    // TODO: Rename and change types and number of parameters
    public static AdminReportScreenFrag newInstance(String param1, String param2) {
        AdminReportScreenFrag fragment = new AdminReportScreenFrag();
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

        View view = inflater.inflate(R.layout.fragment_admin_report_screen, container, false);

        String[] appStatus = {"Not Acknowledged","Acknowledged","Under Progress","Completed"};
        spinner = view.findViewById(R.id.applicationStatus);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,appStatus);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        reference = FirebaseDatabase.getInstance().getReference("ReportCrime");

        updateButton = view.findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });


        ImageView reportImage = view.findViewById(R.id.reportImage);
        TextView typeCrime = view.findViewById(R.id.typeIssue1);
        TextView add1 = view.findViewById(R.id.add1);
        TextView date1 = view.findViewById(R.id.date1);
        TextView time1 = view.findViewById(R.id.time1);
        TextView desc1 = view.findViewById(R.id.desc1);
        TextView appID1 = view.findViewById(R.id.appID1);

        typeCrime.setText(type);
        add1.setText(address);
        date1.setText(date);
        time1.setText(time);
        desc1.setText(desc);
        appID1.setText(appID);
        Glide.with(getContext()).load(image).into(reportImage);

        return  view;
    }



    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = spinner.getSelectedItem().toString();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void updateData() {

        if(!item.equals("Not Acknowledged"))
        {
            reference.child(appID).child("application").setValue(item);
            Toast.makeText(getContext(), " Data Updated", Toast.LENGTH_SHORT).show();
        }

    }

    public void onBackPressed()
    {
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapperAdminRC,new AdminReportedCrimeFrag()).addToBackStack(null).commit();
    }
}