package com.example.crimnoticsapp.BasicActivities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.crimnoticsapp.Admin.AdminLogin;
import com.example.crimnoticsapp.DashboardFiles.Dashboard;
import com.example.crimnoticsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    TextInputLayout txtemail, txtpassword;
    Button callSignUp, login, fPassword,adminLoginBtn;
    CheckBox remember;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        adminLoginBtn = findViewById(R.id.adminLogin);

        adminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,AdminLogin.class));
            }
        });

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if(checkbox.equals("true"))
        {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }
        else if(checkbox.equals("false"))
        {
            Toast.makeText(this, "Please Sign In..", Toast.LENGTH_SHORT).show();
        }

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

        fPassword = findViewById(R.id.forgot_password);
        txtemail = findViewById(R.id.email);
        txtpassword = findViewById(R.id.password);
        login = findViewById(R.id.go);
        remember = findViewById(R.id.rememberMe);

        fPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,ForgotPassword.class));
            }
        });

        callSignUp = findViewById(R.id.new_user);
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.email,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_email);
        awesomeValidation.addValidation(this,R.id.password,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_password);

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(Login.this, "Checked", Toast.LENGTH_SHORT).show();
                }
                else if(!compoundButton.isChecked())
                {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(Login.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    public void loginUser(View view)
    {
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_login);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        if(awesomeValidation.validate())
        {
            isUser();
        }
        else
            progressDialog.dismiss();
    }

    public void isUser()
    {
         String email = txtemail.getEditText().getText().toString().trim();
         String password = txtpassword.getEditText().getText().toString().trim();


        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("userID").equalTo(userID);
                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        String usertype = dataSnapshot.child(userID).child("userType").getValue(String.class);
                                        if(usertype.equals("user"))
                                        {
                                            if(firebaseAuth.getCurrentUser().isEmailVerified())
                                            {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                                finish();
                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(Login.this, "Please Verify Your Email Address", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "No Admin Allowed in User Section", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        FirebaseAuth.getInstance().signOut();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}