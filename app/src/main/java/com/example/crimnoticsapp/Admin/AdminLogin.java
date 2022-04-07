package com.example.crimnoticsapp.Admin;

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

import com.example.crimnoticsapp.BasicActivities.ForgotPassword;
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

public class AdminLogin extends AppCompatActivity {

    TextInputLayout txtusername, txtpassword;
    Button login, fPassword, admToadmReg;
    CheckBox remember;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        firebaseAuth = FirebaseAuth.getInstance();

        admToadmReg = findViewById(R.id.adminToadmReg);

        admToadmReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLogin.this, AdminSignUp.class));
                finish();
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

        fPassword = findViewById(R.id.forgot_password);
        txtusername = findViewById(R.id.email);
        txtpassword = findViewById(R.id.password);
        login = findViewById(R.id.go);
        remember = findViewById(R.id.rememberMe);

        fPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLogin.this, ForgotPassword.class));
            }
        });

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if(checkbox.equals("true"))
        {
            startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
            finish();
        }
        else if(checkbox.equals("false"))
        {
            Toast.makeText(this, "Please Sign In..", Toast.LENGTH_SHORT).show();
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(AdminLogin.this, "Checked", Toast.LENGTH_SHORT).show();
                }
                else if(!compoundButton.isChecked())
                {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(AdminLogin.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validateUsername()
    {
        String val = txtusername.getEditText().getText().toString();
        if(val.isEmpty())
        {
            txtusername.setError("Field cannot be empty");
            return false;
        }
        else {
            txtusername.setError(null);
            return true;
        }
    }
    private Boolean validatePassword()
    {
        String val = txtpassword.getEditText().getText().toString();
        if(val.isEmpty())
        {
            txtpassword.setError("Field cannot be empty");
            return false;
        }
        else {
            txtusername.setError(null);
            return true;
        }
    }

    public void loginUser(View view)
    {
        if(!validateUsername() | !validatePassword())
        {

        }
        else
            isUser();
    }

    public void isUser() {

        progressDialog = new ProgressDialog(AdminLogin.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_login);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        final String email = txtusername.getEditText().getText().toString().trim();
        final String password = txtpassword.getEditText().getText().toString().trim();


        firebaseAuth.signInWithEmailAndPassword(email,password)
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
                                        if(usertype.equals("admin"))
                                        {
                                            if(firebaseAuth.getCurrentUser().isEmailVerified())
                                            {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                                                finish();
                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(AdminLogin.this, "Please Verify Your Email Address", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(AdminLogin.this, "No User Allowed in Admin Section", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        FirebaseAuth.getInstance().signOut();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), AdminLogin.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(AdminLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}