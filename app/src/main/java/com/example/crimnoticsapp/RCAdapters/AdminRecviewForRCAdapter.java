package com.example.crimnoticsapp.RCAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crimnoticsapp.Admin.AdminFragments.AdminReportScreenFrag;
import com.example.crimnoticsapp.HelperClasses.Report;
import com.example.crimnoticsapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdminRecviewForRCAdapter extends FirebaseRecyclerAdapter<Report,AdminRecviewForRCAdapter.myviewholder> {

    public AdminRecviewForRCAdapter(@NonNull FirebaseRecyclerOptions<Report> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final Report model) {

        holder.type.setText(model.getType());
        holder.address.setText(model.getAddress());
        holder.date.setText(model.getReportDate());
        holder.time.setText(model.getReportTime());
        holder.desc.setText(model.getDescription());
        holder.application.setText(model.getApplication());
        holder.appID.setText(model.getPostKey());

        Glide.with(holder.img.getContext()).load(model.getCrimeImage()).into(holder.img);
        final String strImage = model.getCrimeImage();

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapperAdminRC,new AdminReportScreenFrag(strImage,model.getType(),model.getAddress(),model.getReportDate(),model.getReportTime(),model.getDescription(),model.getApplication(),model.getPostKey())).addToBackStack(null).commit();
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_singlerow,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView type,address,date,time,desc,application,appID;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.reportImage);
            type = itemView.findViewById(R.id.typeIssue1);
            address = itemView.findViewById(R.id.add1);
            date = itemView.findViewById(R.id.date1);
            time = itemView.findViewById(R.id.time1);
            desc = itemView.findViewById(R.id.desc1);
            application = itemView.findViewById(R.id.application1);
            appID  = itemView.findViewById(R.id.appID1);

        }
    }

}
