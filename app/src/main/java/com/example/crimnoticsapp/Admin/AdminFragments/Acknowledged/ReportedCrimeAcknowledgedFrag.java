package com.example.crimnoticsapp.Admin.AdminFragments.Acknowledged;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimnoticsapp.HelperClasses.Report;
import com.example.crimnoticsapp.R;
import com.example.crimnoticsapp.RCAdapters.AdminRecviewForRCAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ReportedCrimeAcknowledgedFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recView;
    AdminRecviewForRCAdapter adapter;

    public ReportedCrimeAcknowledgedFrag() {
    }

    public static ReportedCrimeAcknowledgedFrag newInstance(String param1, String param2) {
        ReportedCrimeAcknowledgedFrag fragment = new ReportedCrimeAcknowledgedFrag();
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

        final View view = inflater.inflate(R.layout.fragment_reported_crime_acknowldged, container, false);

        recView = (RecyclerView) view.findViewById(R.id.recview);

        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Report> options =
                new FirebaseRecyclerOptions.Builder<Report>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("ReportCrime").orderByChild("application").equalTo("Acknowledged"), Report.class)
                        .build();

        adapter = new AdminRecviewForRCAdapter(options);
        recView.setAdapter(adapter);

        return view;
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