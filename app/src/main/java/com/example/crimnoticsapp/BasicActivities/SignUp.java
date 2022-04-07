package com.example.crimnoticsapp.BasicActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.crimnoticsapp.AfterScreens.RegConfirm;
import com.example.crimnoticsapp.HelperClasses.UserHelperClass;
import com.example.crimnoticsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout regName, regPhoneNO, regEmail, regCard, regUsername, regPassword;
    RadioButton radioMale, radioFemale, radioOthers;
    Button regToLogin, regBtn;
    public String gender = "";

    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    AwesomeValidation awesomeValidation;
    ProgressDialog progressDialog;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        regName = (TextInputLayout) findViewById(R.id.reg_name);
        regPhoneNO = (TextInputLayout) findViewById(R.id.reg_phoneNo);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        radioOthers = findViewById(R.id.radio_others);
        regEmail = (TextInputLayout) findViewById(R.id.reg_email);
        regCard = (TextInputLayout) findViewById(R.id.reg_Area);
        regUsername = (TextInputLayout) findViewById(R.id.reg_CISFNo);
        regPassword = (TextInputLayout) findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLogin = findViewById(R.id.reg_login_btn);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this,R.id.reg_name,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_name);

        awesomeValidation.addValidation(this,R.id.reg_phoneNo,
                "[5-9]{1}[0-9]{9}",R.string.ivalid_contact);

        awesomeValidation.addValidation(this,R.id.radio_male,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_gender);

        awesomeValidation.addValidation(this,R.id.radio_female,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_gender);

        awesomeValidation.addValidation(this,R.id.radio_others,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_gender);

        awesomeValidation.addValidation(this,R.id.reg_email,
                Patterns.EMAIL_ADDRESS,R.string.ivalid_email);

        awesomeValidation.addValidation(this,R.id.reg_Area,
                ".{12}",R.string.ivalid_aadhar);

        awesomeValidation.addValidation(this,R.id.reg_CISFNo,
                RegexTemplate.NOT_EMPTY,R.string.ivalid_username);
        awesomeValidation.addValidation(this,R.id.reg_password,
                ".{6,}",R.string.ivalid_password);

        firebaseAuth = FirebaseAuth.getInstance();

        regToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }
    public void registerUser(View view)
    {
        if (awesomeValidation.validate() && (radioFemale.isChecked() || radioMale.isChecked() || radioOthers.isChecked()))
        {

            final String name = regName.getEditText().getText().toString();
            final String contact = regPhoneNO.getEditText().getText().toString();
            final String email = regEmail.getEditText().getText().toString();
            final String aadhar = regCard.getEditText().getText().toString();
            final String username = regUsername.getEditText().getText().toString();
            String password = regPassword.getEditText().getText().toString();

            if (radioMale.isChecked())
                gender = "Male";
            if (radioFemale.isChecked())
                gender = "Female";
            if (radioOthers.isChecked())
                gender = "Others";
            isUser();
        }
        else
        {
            Toast.makeText(this, "Please Select a Gender...", Toast.LENGTH_SHORT).show();
        }
    }

    public void isUser()
    {
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        final String name = regName.getEditText().getText().toString();
        final String contact = regPhoneNO.getEditText().getText().toString();
        final String email = regEmail.getEditText().getText().toString();
        final String aadhar = regCard.getEditText().getText().toString();
        final String username = regUsername.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                            UserHelperClass information = new UserHelperClass(name, contact, gender, email, aadhar, username,"user",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {

                                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();

                                        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profile);

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
                                                    Toast.makeText(SignUp.this, "Email Verification Couldn't be sent", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                }
                            });
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
