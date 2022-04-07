package com.example.crimnoticsapp.DashboardFiles;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.crimnoticsapp.AfterScreens.AfterCrimeReport;
import com.example.crimnoticsapp.BasicActivities.Login;
import com.example.crimnoticsapp.HelperClasses.Report;
import com.example.crimnoticsapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportCrime extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    private  static final String CHANNEL_ID = "channel_id01";
    private  static final int NOTIFICATION_ID = 01 ;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    FirebaseDatabase database;
    DatabaseReference myRef;

    Button btnLocation;
    TextView latti, longi, address1,address2;
    double longitude,latitude;
    FusedLocationProviderClient fusedLocationProviderClient;

    ImageView upImage;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUrl = null;

    TextInputLayout desc;
    Button subCrime;
    AwesomeValidation awesomeValidation;

    CheckBox iAgree;
    public static final int REQUEST_CHECK_SETTING = 1001;

    String item;
    Spinner spinner;

    String time;
    TextView textViewDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_crime);

        iAgree = findViewById(R.id.checkBox);


        String[] typeOfCrime = {"Choose Type of Crime","Accident","Murder","Found a Dead Body","Robbery","Cyber Crime","Domestic Violence","Kidnapping","Hacking","Smuggling","Corruption","Others"};
        spinner = findViewById(R.id.typeOfIssue);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,typeOfCrime);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        upImage = findViewById(R.id.uploadImage);
        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT>=22)
                {
                    checkAndRequestForPermission();
                }
                else
                {
                    openGallery();
                }
            }
        });


        btnLocation = findViewById(R.id.btn_loc);
        latti = findViewById(R.id.lat);
        longi = findViewById(R.id.lon);
        address1 = findViewById(R.id.textAddress);
        address2 = findViewById(R.id.addressHide);
        address2.setVisibility(View.INVISIBLE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ReportCrime.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(ReportCrime.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
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
        textViewDate = findViewById(R.id.text_view_date);
        textViewDate.setText(currentDate);

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat1.format(calendar.getTime());

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

        navigationView.setCheckedItem(R.id.nav_report);



        subCrime = findViewById(R.id.submitCrime);
        desc = (TextInputLayout) findViewById(R.id.description);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.description,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_name);

    }



    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {

        if(ContextCompat.checkSelfPermission(ReportCrime.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(ReportCrime.this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(ReportCrime.this, "Please Accept the Required Permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(ReportCrime.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }
        else
        {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==REQUESCODE && data!=null)
        {
            pickedImgUrl = data.getData();
            upImage.setImageURI(pickedImgUrl);
        }
    }

    private void getLocation() {
        Toast.makeText(this, "Manually Turn On Your Location Settings", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        progressDialog = new ProgressDialog(ReportCrime.this);
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
                        Geocoder geocoder = new Geocoder(ReportCrime.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        latti.setText(Html.fromHtml(
                                "<font><b>Latitude :</b><br></font>"
                                        + addresses.get(0).getLatitude()
                        ));
                        latitude = location.getLatitude();

                        longi.setText(Html.fromHtml(
                                "<font><b>Longitude :</b><br></font>"
                                        + addresses.get(0).getLongitude()
                        ));
                        longitude = location.getLongitude();

                        address1.setText(Html.fromHtml(
                                "<font><b></b></font>"
                                        + addresses.get(0).getAddressLine(0)
                        ));

                        if(latti.getText().toString().isEmpty() || longi.getText().toString().isEmpty() || address1.getText().toString().isEmpty())
                        {
                            Toast.makeText(ReportCrime.this, "Please Fetch the Location First...", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            address2.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
                Intent intent0 = new Intent(ReportCrime.this, Dashboard.class);
                startActivity(intent0);
                break;

            case R.id.nav_missingPerson:
                Intent intent = new Intent(ReportCrime.this, MissingPerson.class);
                startActivity(intent);
                break;

            case R.id.nav_sos:
                Intent intent1 = new Intent(ReportCrime.this, SOS.class);
                startActivity(intent1);
                break;

            case R.id.nav_listOfCrime:
                Intent intent2 = new Intent(ReportCrime.this, ListOfCrime.class);
                startActivity(intent2);
                break;

            case R.id.nav_report:
                break;

            case R.id.nav_fileComplaint:
                Intent intent3 = new Intent( ReportCrime.this, FileComplaint.class);
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


    public void reportFunction(View view)
    {
        if( latti.getText().toString().isEmpty() || longi.getText().toString().isEmpty() || address1.getText().toString().isEmpty())
        {
            Toast.makeText(ReportCrime.this, "Please Fetch the Location First...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(awesomeValidation.validate() && pickedImgUrl !=null && iAgree.isChecked())
            {
                subReport();
            }
            else
            {
                iAgree.setError("Cannot Submit Without Selecting the Checkbox");
                Toast.makeText(ReportCrime.this, "Please File the Report Correctly", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void subReport()
    {
        if(item == "Choose Type of Crime")
        {
            Toast.makeText(this, "Please Select the Type Of Crime", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("crime_photos");
            progressDialog = new ProgressDialog(ReportCrime.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_upload);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            final StorageReference imageFilePath = storageReference.child(pickedImgUrl.getLastPathSegment());
            imageFilePath.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageDownloadLink = uri.toString();
                            Report report = new Report(item, latitude, longitude, address1.getText().toString(), desc.getEditText().getText().toString(), imageDownloadLink, currentUser.getUid(),time,textViewDate.getText().toString(),"Not Acknowledged",0);

                            addReport(report);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ReportCrime.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });
        }
    }

    private void addReport(Report report) {

        progressDialog = new ProgressDialog(ReportCrime.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_submit);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ReportCrime").push();

        String key = myRef.getKey();
        report.setPostKey(key);

        myRef.setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(ReportCrime.this, "Report Submitted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ReportCrime.this, AfterCrimeReport.class));
                }
                else
                {
                    Toast.makeText(ReportCrime.this, "Report Not Submitted", Toast.LENGTH_SHORT).show();
                }
            }
        });


        createNotificationChannel();

        Intent mainIntent = new Intent(this,ListOfCrime.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mainPIntent = PendingIntent.getActivity(this,0,mainIntent,PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder  = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.detectivelogo1);
        builder.setContentTitle("Report Crime");
        builder.setContentText("New Crime Reported by You");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);

        builder.setContentIntent(mainPIntent);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.anonymity);
        builder.setLargeIcon(bitmap);
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}