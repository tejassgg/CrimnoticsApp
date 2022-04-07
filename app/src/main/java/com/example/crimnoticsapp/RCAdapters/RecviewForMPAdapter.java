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
import com.example.crimnoticsapp.HelperClasses.Missing;
import com.example.crimnoticsapp.MissingScreenFrag;
import com.example.crimnoticsapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecviewForMPAdapter extends FirebaseRecyclerAdapter<Missing,RecviewForMPAdapter.myviewholder>
{

    public RecviewForMPAdapter(@NonNull FirebaseRecyclerOptions<Missing> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final Missing model) {

        holder.name1.setText(model.getName());
        holder.age1.setText(model.getAge());
        holder.height1.setText(model.getHeight());
        holder.time1.setText(model.getReportTime());
        holder.lastSeen1.setText(model.getLastSeen());
        holder.gender1.setText(model.getGender());
        holder.desc1.setText(model.getDescription());
        holder.application.setText(model.getApplication());
        holder.appID.setText(model.getPostKey());
        Glide.with(holder.reportImage.getContext()).load(model.getMissingImage()).into(holder.reportImage);

        final String strImage = model.getMissingImage();

        holder.reportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapperUserMP,new MissingScreenFrag(model.getName(),model.getAge(),model.getHeight(),model.getReportTime(),model.getLastSeen(),model.getGender(),model.getDescription(),strImage,model.getApplication(),model.getPostKey())).addToBackStack(null).commit();
            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mp_singlerow,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView reportImage;
        TextView name1,age1,height1,time1,gender1,lastSeen1,desc1,application,appID;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            reportImage = itemView.findViewById(R.id.reportImage);
            name1 = itemView.findViewById(R.id.name1);
            age1 = itemView.findViewById(R.id.age1);
            height1 = itemView.findViewById(R.id.height1);
            time1 = itemView.findViewById(R.id.time1);
            gender1 = itemView.findViewById(R.id.gender1);
            lastSeen1 = itemView.findViewById(R.id.area1);
            desc1 = itemView.findViewById(R.id.desc1);
            application = itemView.findViewById(R.id.application1);
            appID  = itemView.findViewById(R.id.appID1);
        }
    }

}
