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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.example.crimnoticsapp.AfterScreens.AfterMissingPerson;
import com.example.crimnoticsapp.BasicActivities.Login;
import com.example.crimnoticsapp.HelperClasses.Missing;
import com.example.crimnoticsapp.R;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MissingPerson extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private  static final String CHANNEL_ID = "channel_id01";
    private  static final int NOTIFICATION_ID = 01 ;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ProgressDialog progressDialog;

    RadioButton radioMale, radioFemale, radioOthers;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    FirebaseDatabase database;
    DatabaseReference myRef;

    ImageView missPersonImg;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUrl = null;

    TextInputLayout name, age, lastSeen, height,desc;
    Button subMissingReport;
    AwesomeValidation awesomeValidation;

    String time;
    TextView textViewDate;

    public String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_person);

        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        radioOthers = findViewById(R.id.radio_others);

        missPersonImg = findViewById(R.id.uploadImage);
        missPersonImg.setOnClickListener(new View.OnClickListener() {
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
        navigationView.setCheckedItem(R.id.nav_missingPerson);


        subMissingReport = findViewById(R.id.submitCrime);
        name = (TextInputLayout)findViewById(R.id.name1);
        age = (TextInputLayout)findViewById(R.id.age1);
        height = (TextInputLayout)findViewById(R.id.height1);
        lastSeen = (TextInputLayout)findViewById(R.id.area1);
        desc = (TextInputLayout) findViewById(R.id.description);


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.name1,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_name);

        awesomeValidation.addValidation(this,R.id.age1,
                RegexTemplate.NOT_EMPTY,R.string.invalid_age);

        awesomeValidation.addValidation(this,R.id.height1,
                RegexTemplate.NOT_EMPTY,R.string.invalid_height);

        awesomeValidation.addValidation(this,R.id.area1,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_lastSeen);

        awesomeValidation.addValidation(this,R.id.description,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_name);

        awesomeValidation.addValidation(this,R.id.radio_male,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_gender);

        awesomeValidation.addValidation(this,R.id.radio_female,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_gender);

        awesomeValidation.addValidation(this,R.id.radio_others,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_gender);


    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {

        if(ContextCompat.checkSelfPermission(MissingPerson.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MissingPerson.this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(MissingPerson.this, "Please Accept the Required Permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(MissingPerson.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
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
            missPersonImg.setImageURI(pickedImgUrl);
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
                Intent intent0 = new Intent(MissingPerson.this, Dashboard.class);
                startActivity(intent0);
                break;

            case R.id.nav_missingPerson:
                break;

            case R.id.nav_sos:
                Intent intent1 = new Intent(MissingPerson.this, SOS.class);
                startActivity(intent1);
                break;

            case R.id.nav_listOfCrime:
                Intent intent2 = new Intent(MissingPerson.this, ListOfCrime.class);
                startActivity(intent2);
                break;

            case R.id.nav_report:
                Intent intent = new Intent(MissingPerson.this,  ReportCrime.class);
                startActivity(intent);
                break;

            case R.id.nav_fileComplaint:
                Intent intent3 = new Intent( MissingPerson.this, FileComplaint.class);
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
            if(awesomeValidation.validate() && pickedImgUrl !=null)
            {
                subReport();
            }
            else
            {
                Toast.makeText(MissingPerson.this, "Please File the Report Correctly", Toast.LENGTH_SHORT).show();
            }
    }

    private void subReport()
    {
        if (radioMale.isChecked())
            gender = "Male";
        if (radioFemale.isChecked())
            gender = "Female";
        if (radioOthers.isChecked())
            gender = "Others";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("missing_person_photos");
            progressDialog = new ProgressDialog(MissingPerson.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_upload);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            final String height11 = height.getEditText().getText().toString() + " cm";
            final String age11 = age.getEditText().getText().toString() + "Yr";

            final StorageReference imageFilePath = storageReference.child(pickedImgUrl.getLastPathSegment());
            imageFilePath.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageDownloadLink = uri.toString();
                            Missing report = new Missing(name.getEditText().getText().toString(),age11,height11,lastSeen.getEditText().getText().toString(),gender, desc.getEditText().getText().toString(), imageDownloadLink, currentUser.getUid(),time,textViewDate.getText().toString(),"Not Acknowledged");

                            addReport(report);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MissingPerson.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });
    }

    private void addReport(Missing report) {

        progressDialog = new ProgressDialog(MissingPerson.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_submit);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("MissingPerson").push();

        String key = myRef.getKey();
        report.setPostKey(key);

        myRef.setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(MissingPerson.this, "Report Submitted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MissingPerson.this, AfterMissingPerson.class));


                    createNotificationChannel();

                    Intent mainIntent = new Intent(MissingPerson.this,ListOfCrime.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent mainPIntent = PendingIntent.getActivity(MissingPerson.this,0,mainIntent,PendingIntent.FLAG_ONE_SHOT);


                    NotificationCompat.Builder builder  = new NotificationCompat.Builder(MissingPerson.this,CHANNEL_ID);
                    builder.setSmallIcon(R.drawable.detectivelogo1);
                    builder.setContentTitle("Missing Person");
                    builder.setContentText("New Missing Person Reported by You");
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setAutoCancel(true);

                    builder.setContentIntent(mainPIntent);

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.missperson);
                    builder.setLargeIcon(bitmap);
                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MissingPerson.this);
                    notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                }
                else
                {
                    Toast.makeText(MissingPerson.this, "Report Not Submitted", Toast.LENGTH_SHORT).show();
                }
            }
        });

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