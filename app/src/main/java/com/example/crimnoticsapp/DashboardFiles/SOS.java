package com.example.crimnoticsapp.DashboardFiles;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.crimnoticsapp.BasicActivities.Login;
import com.example.crimnoticsapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SOS extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private  static final String CHANNEL_ID = "channel_id01";
    private  static final int NOTIFICATION_ID = 01 ;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    TextView latti, longi;

    Button getLoc;
    double longitude = 0;
    double latitude = 0;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextInputLayout phoneNo1,phoneNo2,phoneNo3,phoneNo4;
    TextView msgText;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_s);

        latti = findViewById(R.id.lat1);
        longi = findViewById(R.id.lon1);
        latti.setVisibility(View.INVISIBLE);
        longi.setVisibility(View.INVISIBLE);


        msgText= findViewById(R.id.txt_msg);
        msgText.setVisibility(View.INVISIBLE);

        phoneNo1 = (TextInputLayout)findViewById(R.id.phoneNo1);
        phoneNo2 = (TextInputLayout)findViewById(R.id.phoneNo2);
        phoneNo3 = (TextInputLayout)findViewById(R.id.phoneNo3);
        phoneNo4 = (TextInputLayout)findViewById(R.id.phoneNo4);


        getLoc = findViewById(R.id.btn_location);
        final int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(SOS.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SOS.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(SOS.this, "Click Again.!!", Toast.LENGTH_SHORT).show();
                    if(permissionCheck==PackageManager.PERMISSION_GRANTED ) {
                        getLocation();
                    }
                    else
                        ActivityCompat.requestPermissions(SOS.this, new String[]{Manifest.permission.SEND_SMS}, 0);
                }
                else {
                    ActivityCompat.requestPermissions(SOS.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44 );
                }
            }
        });

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


        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.text_view_date);
        textViewDate.setText(currentDate);

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

        navigationView.setCheckedItem(R.id.nav_sos);

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }

        String p1 = phoneNo1.getEditText().getText().toString().trim();
        String p2 = phoneNo2.getEditText().getText().toString().trim();
        String p3 = phoneNo3.getEditText().getText().toString().trim();
        String p4 = phoneNo4.getEditText().getText().toString().trim();

        if( !phoneNo1.getEditText().getText().toString().equals("") && !phoneNo2.getEditText().getText().toString().equals("") && !phoneNo3.getEditText().getText().toString().equals("") && !phoneNo4.getEditText().getText().toString().equals("") ) {
            if (!p1.equals(p2) || (!p1.equals(p3)) ||!p1.equals(p3)) {
                progressDialog = new ProgressDialog(SOS.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_location);
                progressDialog.setCancelable(true);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(SOS.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                latti.setText(Html.fromHtml(
                                        "<font><b></b><br></font>"
                                                + addresses.get(0).getLatitude()
                                ));
                                latitude = location.getLatitude();

                                longi.setText(Html.fromHtml(
                                        "<font><b></b><br></font>"
                                                + addresses.get(0).getLongitude()
                                ));
                                longitude = location.getLongitude();

                                if (longitude == 0 || latitude == 0)
                                    Toast.makeText(SOS.this, "Please Switch on your Location service...", Toast.LENGTH_SHORT).show();
                                else {
                                    progressDialog.dismiss();
                                }

                                String phone_no1 = phoneNo1.getEditText().getText().toString().trim();
                                String phone_no2 = phoneNo2.getEditText().getText().toString().trim();
                                String phone_no3 = phoneNo3.getEditText().getText().toString().trim();
                                String phone_no4 = phoneNo4.getEditText().getText().toString().trim();


                                msgText.setText(Html.fromHtml("<font>Help Me! My Location is: https://maps.google.com/maps?f=q&t=m&q=</font>"
                                        + addresses.get(0).getLatitude() + "," + addresses.get(0).getLongitude()
                                ));

                                String msg = msgText.getText().toString().trim();
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(phone_no1, null, msg, null, null);

                                SmsManager smsManager1 = SmsManager.getDefault();
                                smsManager1.sendTextMessage(phone_no2, null, msg, null, null);

                                SmsManager smsManager2 = SmsManager.getDefault();
                                smsManager2.sendTextMessage(phone_no3, null, msg, null, null);

                                SmsManager smsManager3 = SmsManager.getDefault();
                                smsManager3.sendTextMessage(phone_no4, null, msg, null, null);



                                Toast.makeText(SOS.this, "Alert Message Sent To Your Selected Contact", Toast.LENGTH_SHORT).show();

                                createNotificationChannel();

                                NotificationCompat.Builder builder  = new NotificationCompat.Builder(SOS.this,CHANNEL_ID);
                                builder.setSmallIcon(R.drawable.detectivelogo1);
                                builder.setContentTitle("SOS");
                                builder.setContentText("Emergency Message Has Been Sent To Your Contacts");
                                builder.setPriority(NotificationCompat.PRIORITY_HIGH);

                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sos1);
                                builder.setLargeIcon(bitmap);
                                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));

                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(SOS.this);
                                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());


                                startActivity(new Intent(SOS.this, SOS.class));


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                phoneNo1.setError("Number Cannot be Same");
                phoneNo2.setError("Number Cannot be Same");
                phoneNo3.setError("Number Cannot be Same");
                phoneNo4.setError("Number Cannot be Same");
            }
        }
        else
        {
            phoneNo1.setError("Please Enter the Contact Number 1");
            phoneNo2.setError("Please Enter the Contact Number 2");
            phoneNo3.setError("Please Enter the Contact Number 3");
            phoneNo4.setError("Please Enter the Contact Number 4");
            Toast.makeText(SOS.this, "Please Switch on your Location service... ", Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 0:
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    getLocation();
                }
                else
                {
                    Toast.makeText(this, "You Don't Have Permissions to send SMS", Toast.LENGTH_SHORT).show();
                }
        }

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
                Intent intent0 = new Intent(SOS.this, Dashboard.class);
                startActivity(intent0);
                break;

            case R.id.nav_missingPerson:
                Intent intent = new Intent(SOS.this, MissingPerson.class);
                startActivity(intent);
                break;

            case R.id.nav_sos:
                break;

            case R.id.nav_listOfCrime:
                Intent intent2 = new Intent(SOS.this, ListOfCrime.class);
                startActivity(intent2);
                break;

            case R.id.nav_report:
                Intent intent1 = new Intent(SOS.this,  ReportCrime.class);
                startActivity(intent1);
                break;

            case R.id.nav_fileComplaint:
                Intent intent3 = new Intent( SOS.this, FileComplaint.class);
                startActivity(intent3);
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