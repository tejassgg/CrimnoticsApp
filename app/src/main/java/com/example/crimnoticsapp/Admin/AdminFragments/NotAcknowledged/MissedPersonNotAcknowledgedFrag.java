package com.example.crimnoticsapp.Admin.AdminFragments.NotAcknowledged;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimnoticsapp.HelperClasses.Missing;
import com.example.crimnoticsapp.R;
import com.example.crimnoticsapp.RCAdapters.AdminRecviewForMPAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MissedPersonNotAcknowledgedFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recView;
    AdminRecviewForMPAdapter adapter;

    public MissedPersonNotAcknowledgedFrag() {
    }

    public static MissedPersonNotAcknowledgedFrag newInstance(String param1, String param2) {
        MissedPersonNotAcknowledgedFrag fragment = new MissedPersonNotAcknowledgedFrag();
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

        final View view = inflater.inflate(R.layout.fragment_missed_person_not_acknowledged, container, false);


        recView = (RecyclerView) view.findViewById(R.id.recview);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Missing> options =
                new FirebaseRecyclerOptions.Builder<Missing>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("MissingPerson").orderByChild("application").equalTo("Not Acknowledged"), Missing.class)
                        .build();

        adapter = new AdminRecviewForMPAdapter(options);
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