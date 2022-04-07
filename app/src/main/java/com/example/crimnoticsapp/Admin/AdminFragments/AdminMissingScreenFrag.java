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


public class AdminMissingScreenFrag extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String name,age,height,date,lastSeen,gender,desc,image,application,appID;
    Button updateButton;
    String item;
    Spinner spinner;
    DatabaseReference reference;

    public AdminMissingScreenFrag() {
    }

    public AdminMissingScreenFrag(String name, String age, String height, String date, String lastSeen, String gender, String desc, String image, String application,String appID) {
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

    public static AdminMissingScreenFrag newInstance(String param1, String param2) {
        AdminMissingScreenFrag fragment = new AdminMissingScreenFrag();
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

        View view = inflater.inflate(R.layout.fragment_admin_missing_screen, container, false);

        String[] appStatus = {"Not Acknowledged","Acknowledged","Under Progress","Person Found"};
        spinner = view.findViewById(R.id.applicationStatus);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,appStatus);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        reference = FirebaseDatabase.getInstance().getReference("MissingPerson");
        updateButton = (Button)view.findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
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
        TextView appID1 = view.findViewById(R.id.appID1);


        name1.setText(name);
        age1.setText(age);
        height1.setText(height);
        time1.setText(date);
        lastSeen1.setText(lastSeen);
        gender1.setText(gender);
        desc1.setText(desc);
        appID1.setText(appID);
        Glide.with(getContext()).load(image).into(reportImage);

        return view;

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
            Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
        }

    }

    public void onBackPressed()
    {
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapperAdminMP,new AdminMissedPersonFrag()).addToBackStack(null).commit();
    }
}