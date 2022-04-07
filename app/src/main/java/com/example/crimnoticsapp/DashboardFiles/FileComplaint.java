package com.example.crimnoticsapp.DashboardFiles;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.crimnoticsapp.AfterScreens.AfterCrimeReport;
import com.example.crimnoticsapp.BasicActivities.Login;
import com.example.crimnoticsapp.HelperClasses.File;
import com.example.crimnoticsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileComplaint extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    private  static final String CHANNEL_ID = "channel_id01";
    private  static final int NOTIFICATION_ID = 01 ;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog progressDialog;

    String item;
    Spinner spinner;
    AwesomeValidation awesomeValidation;

    String time;
    TextView textViewDate;

    TextInputLayout area,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_complaint);

        area = (TextInputLayout)findViewById(R.id.area1);
        desc = (TextInputLayout)findViewById(R.id.desc1);


        String[] typeOfIssue = {"Choose Type of Issue","Municipal Corporation Issue","Water Issue","MSEDCL Issue","Fire Near Me","Domestic Violence","Others"};
        spinner = findViewById(R.id.typeOfIssue);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,typeOfIssue);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);



        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        textViewDate = findViewById(R.id.text_view_date);
        textViewDate.setText(currentDate);

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat1.format(calendar.getTime());


        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.alert_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            Button button = dialog.findViewById(R.id.btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });

            dialog.show();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        navigationView.setCheckedItem(R.id.nav_fileComplaint);


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.area1,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_area);
        awesomeValidation.addValidation(this,R.id.desc1,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_desc);

    }
    public void updateNavHeader()
    {
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = headerView.findViewById(R.id.emailID);
        TextView navName = headerView.findViewById(R.id.displayName);
        navName.setText(currentUser.getDisplayName());
        navEmail.setText(currentUser.getEmail());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                Intent intent0 = new Intent(FileComplaint.this, Dashboard.class);
                startActivity(intent0);
                break;

            case R.id.nav_missingPerson:
                Intent intent = new Intent(FileComplaint.this, MissingPerson.class);
                startActivity(intent);
                break;

            case R.id.nav_sos:
                Intent intent1 = new Intent(FileComplaint.this, SOS.class);
                startActivity(intent1);
                break;

            case R.id.nav_listOfCrime:
                Intent intent2 = new Intent(FileComplaint.this, ListOfCrime.class);
                startActivity(intent2);
                break;

            case R.id.nav_report:
                Intent intent3 = new Intent(FileComplaint.this, ReportCrime.class);
                startActivity(intent3);
                break;

            case R.id.nav_fileComplaint:
                break;

            case R.id.nav_logout:
                getSharedPreferences("checkbox",MODE_PRIVATE).edit().clear().commit();
                String checkbox = "false";
                if(checkbox.equals("false"))
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                    Toast.makeText(this, "Please Sign In..", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_contact:
                Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fileComplaint(View View)
    {
            if(awesomeValidation.validate())
            {
                File file =new File(item, area.getEditText().getText().toString(), desc.getEditText().getText().toString(), currentUser.getUid(), time, textViewDate.getText().toString());
                fileReport(file);
            }
    }

    private void fileReport(File file) {

        progressDialog = new ProgressDialog(FileComplaint.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_submit);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        if(item == "Choose Type of Crime")
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Please Select the Type Of Crime", Toast.LENGTH_SHORT).show();
        }
        else {

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("FileComplaint").push();

            String key = myRef.getKey();
            file.setPostKey(key);
            myRef.setValue(file).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Toast.makeText(FileComplaint.this, "Report Submitted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FileComplaint.this, AfterCrimeReport.class));

                        createNotificationChannel();

                        Intent mainIntent = new Intent(FileComplaint.this,ListOfCrime.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent mainPIntent = PendingIntent.getActivity(FileComplaint.this,0,mainIntent,PendingIntent.FLAG_ONE_SHOT);


                        NotificationCompat.Builder builder  = new NotificationCompat.Builder(FileComplaint.this,CHANNEL_ID);
                        builder.setSmallIcon(R.drawable.detectivelogo1);
                        builder.setContentTitle("File Complaint");
                        builder.setContentText("New Complaint Filed by You");
                        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                        builder.setAutoCancel(true);

                        builder.setContentIntent(mainPIntent);

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.file1);
                        builder.setLargeIcon(bitmap);
                        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(FileComplaint.this);
                        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(FileComplaint.this, "Report Not Submitted", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "My Notification";
            String description = "My Notification Description";

            int importance  = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}