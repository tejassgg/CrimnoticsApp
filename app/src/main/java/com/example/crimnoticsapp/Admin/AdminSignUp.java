package com.example.crimnoticsapp.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.crimnoticsapp.AfterScreens.RegConfirm;
import com.example.crimnoticsapp.HelperClasses.AdminRegis;
import com.example.crimnoticsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSignUp extends AppCompatActivity {

    TextInputLayout regName, regPhoneNO, regEmail, regArea, regCISFNo, regPassword;
    Button regToLogin, regBtn;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    ProgressDialog progressDialog;

    FirebaseUser currentUser;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        regName = (TextInputLayout) findViewById(R.id.reg_name);
        regPhoneNO = (TextInputLayout) findViewById(R.id.reg_phoneNo);
        regEmail = (TextInputLayout) findViewById(R.id.reg_email);
        regArea = (TextInputLayout) findViewById(R.id.reg_Area);
        regCISFNo = (TextInputLayout) findViewById(R.id.reg_CISFNo);
        regPassword = (TextInputLayout) findViewById(R.id.reg_password);


        regBtn = findViewById(R.id.reg_btn);
        regToLogin = findViewById(R.id.reg_login_btn);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.reg_name,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_name);

        awesomeValidation.addValidation(this,R.id.reg_phoneNo,
                "[5-9]{1}[0-9]{9}",R.string.ivalid_contact);

        awesomeValidation.addValidation(this,R.id.reg_email,
                Patterns.EMAIL_ADDRESS,R.string.ivalid_email);

        awesomeValidation.addValidation(this,R.id.reg_Area,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_Parea);

        awesomeValidation.addValidation(this,R.id.reg_CISFNo,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_CISFNo);
        awesomeValidation.addValidation(this,R.id.reg_password,
                ".{6,}",R.string.ivalid_password);

        firebaseAuth = FirebaseAuth.getInstance();

        regToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminSignUp.this, AdminLogin.class);
                startActivity(intent);
            }
        });
    }
    public void registerAdmin(View view)
    {
        if (awesomeValidation.validate())
        {
            isUser();
        }
        else
        {
        }
    }

    public void isUser()
    {
        progressDialog = new ProgressDialog(AdminSignUp.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        final String name = regName.getEditText().getText().toString();
        final String contact = regPhoneNO.getEditText().getText().toString();
        final String email = regEmail.getEditText().getText().toString();
        final String area = regArea.getEditText().getText().toString();
        final String cisfNo = regCISFNo.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profile);

                            AdminRegis information = new AdminRegis(name,contact,email,area,cisfNo,0,0,"admin",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                   .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(getApplicationContext(), RegConfirm.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else
                                                {
                                                    Toast.makeText(AdminSignUp.this, "Email Verification Couldn't be sent", Toast.LENGTH_SHORT);
                                                }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(AdminSignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(AdminSignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}