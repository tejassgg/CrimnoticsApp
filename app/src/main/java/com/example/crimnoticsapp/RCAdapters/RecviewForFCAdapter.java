package com.example.crimnoticsapp.RCAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimnoticsapp.HelperClasses.File;
import com.example.crimnoticsapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class RecviewForFCAdapter extends FirebaseRecyclerAdapter<File, RecviewForFCAdapter.myviewholder > {

    public RecviewForFCAdapter(@NonNull FirebaseRecyclerOptions<File> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final File model) {

        holder.type1.setText(model.getType());
        holder.area1.setText(model.getArea());
        holder.desc1.setText(model.getDescription());
        holder.date1.setText(model.getReportDate());
        holder.time1.setText(model.getReportTime());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fc_singlerow,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {

        TextView type1,area1,desc1,date1,time1;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            type1 = itemView.findViewById(R.id.typeIssue1);
            area1 = itemView.findViewById(R.id.area1);
            desc1 = itemView.findViewById(R.id.desc1);
            date1 = itemView.findViewById(R.id.date1);
            time1 = itemView.findViewById(R.id.time1);
        }
    }

}
