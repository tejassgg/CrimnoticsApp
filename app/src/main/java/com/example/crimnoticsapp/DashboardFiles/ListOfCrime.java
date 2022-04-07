package com.example.crimnoticsapp.DashboardFiles;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.crimnoticsapp.BasicActivities.Login;
import com.example.crimnoticsapp.LOCPageAdapter;
import com.example.crimnoticsapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Calendar;

public class ListOfCrime extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    TabLayout tabLayout;
    TabItem tab1,tab2,tab3;
    ViewPager viewPager;
    com.example.crimnoticsapp.LOCPageAdapter LOCPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_crime);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tab1 = (TabItem)findViewById(R.id.tab1);
        tab2 = (TabItem)findViewById(R.id.tab2);
        tab3 = (TabItem)findViewById(R.id.tab3);
        viewPager = (ViewPager)findViewById(R.id.vPager);

        LOCPageAdapter = new LOCPageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(LOCPageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1 ||tab.getPosition()==2) {
                    LOCPageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

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

        navigationView.setCheckedItem(R.id.nav_listOfCrime);


    }


    public void updateNavHeader() {
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
                Intent intent0 = new Intent(ListOfCrime.this, Dashboard.class);
                startActivity(intent0);
                break;

            case R.id.nav_missingPerson:
                Intent intent = new Intent(ListOfCrime.this, MissingPerson.class);
                startActivity(intent);
                break;

            case R.id.nav_sos:
                Intent intent1 = new Intent(ListOfCrime.this, SOS.class);
                startActivity(intent1);
                break;

            case R.id.nav_listOfCrime:
                break;

            case R.id.nav_report:
                Intent intent2 = new Intent(ListOfCrime.this,  ReportCrime.class);
                startActivity(intent2);
                break;

            case R.id.nav_fileComplaint:
                Intent intent3 = new Intent(ListOfCrime.this, FileComplaint.class);
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

}