package com.example.crimnoticsapp.Admin;

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
import com.example.crimnoticsapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Calendar;

public class AdminDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TabLayout tabLayout;
    TabItem tab1, tab2, tab3;
    ViewPager viewPager;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;


    com.example.crimnoticsapp.Admin.AdminLOCPageAdapter AdminLOCPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (!currentUser.isEmailVerified()) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
            Toast.makeText(this, "Please Verify Your Email First", Toast.LENGTH_SHORT).show();
        }

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tab1 = (TabItem) findViewById(R.id.tab1);
        tab2 = (TabItem) findViewById(R.id.tab2);
        tab3 = (TabItem) findViewById(R.id.tab3);
        viewPager = (ViewPager) findViewById(R.id.vPager);

        AdminLOCPageAdapter = new AdminLOCPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(AdminLOCPageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2) {
                    AdminLOCPageAdapter.notifyDataSetChanged();
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
        navigationView = findViewById(R.id.admin_nav_view);
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

        navigationView.setCheckedItem(R.id.admin_nav_home);

    }


    public void updateNavHeader()
    {
        navigationView = findViewById(R.id.admin_nav_view);
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

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.admin_nav_home:
                break;

            case R.id.admin_nav_logout:

                getSharedPreferences("checkbox", MODE_PRIVATE).edit().clear().commit();
                String checkbox = "false";
                if (checkbox.equals("false")) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), AdminLogin.class));
                    finish();
                    Toast.makeText(this, "Please Sign In..", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.admin_nav_not_acknowledged:
                startActivity(new Intent(getApplicationContext(), AdminNotAcknowledged.class));
                break;

            case R.id.admin_nav_acknowledged:
                startActivity(new Intent(getApplicationContext(),AdminAcknowledged.class));
                break;

            case R.id.admin_nav_underProgress:
                startActivity(new Intent(getApplicationContext(),AdminUnderProgress.class));
                break;

            case R.id.admin_nav_completed:
                startActivity(new Intent(getApplicationContext(),AdminCompleted.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}