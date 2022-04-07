package com.example.crimnoticsapp.Admin.AdminFragments;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimnoticsapp.HelperClasses.Report;
import com.example.crimnoticsapp.R;
import com.example.crimnoticsapp.RCAdapters.AdminRecviewForRCAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminReportedCrimeFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recView;
    AdminRecviewForRCAdapter adapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    double lat1;
    double lon1;
    double lat2;
    double lon2;

    String item;
    Spinner spinner;

    DatabaseReference reference;

    public AdminReportedCrimeFrag() {
    }
    public static AdminReportedCrimeFrag newInstance(String param1, String param2) {
        AdminReportedCrimeFrag fragment = new AdminReportedCrimeFrag();
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
                             final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_admin_reported_crime, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("ReportCrime");

        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Toast.makeText(getActivity(), "Please wait for the List to Load Up", Toast.LENGTH_SHORT).show();


        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("userID").equalTo(userID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    lat1 = dataSnapshot.child(userID).child("latitude").getValue(Double.class);
                    lon1 = dataSnapshot.child(userID).child("longitude").getValue(Double.class);

                    Query checkReport = FirebaseDatabase.getInstance().getReference().child("ReportCrime");
                    checkReport.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           for(DataSnapshot snapshot1: snapshot.getChildren())
                           {

                               lat2 = snapshot1.child("latitude").getValue(Double.class);
                               lon2 = snapshot1.child("longitude").getValue(Double.class);

                               float[] results = new float[1];
                               Location.distanceBetween(lat1, lon1, lat2, lon2, results);

                               float distance = results[0];

                               double kmDist = distance / 1000;

                               reference.child(snapshot1.getKey()).child("dist").setValue(kmDist);

                               if(kmDist<=10)
                               {
                                   //Toast.makeText(getContext(), snapshot1.getKey()+" Found", Toast.LENGTH_SHORT).show();
                                   //Toast.makeText(getContext(), kmDist + " km", Toast.LENGTH_SHORT).show();
                               }

                           }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

            recView = (RecyclerView) view.findViewById(R.id.recview);
            recView.setLayoutManager(new LinearLayoutManager(getContext()));
            FirebaseRecyclerOptions<Report> options =
                    new FirebaseRecyclerOptions.Builder<Report>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("ReportCrime").orderByChild("dist").startAt(0).endAt(10), Report.class)
                            .build();

            adapter = new AdminRecviewForRCAdapter(options);
            recView.setAdapter(adapter);

        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}