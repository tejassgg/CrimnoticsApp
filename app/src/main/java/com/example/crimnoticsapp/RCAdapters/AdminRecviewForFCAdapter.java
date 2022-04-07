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

public class AdminRecviewForFCAdapter extends FirebaseRecyclerAdapter<File, AdminRecviewForFCAdapter.myviewholder> {

    public AdminRecviewForFCAdapter(@NonNull FirebaseRecyclerOptions<File> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull File model) {
        holder.type.setText(model.getType());
        holder.area.setText(model.getArea());
        holder.desc.setText(model.getDescription());
        holder.date.setText(model.getReportDate());
        holder.time.setText(model.getReportTime());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fc_singlerow,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        TextView type,area,date,time,desc;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.typeIssue1);
            area = itemView.findViewById(R.id.area1);
            date = itemView.findViewById(R.id.date1);
            time = itemView.findViewById(R.id.time1);
            desc = itemView.findViewById(R.id.desc1);

        }
    }

}
