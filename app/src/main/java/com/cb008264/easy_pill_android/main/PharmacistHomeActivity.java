package com.cb008264.easy_pill_android.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cb008264.easy_pill_android.LoginActivity;
import com.cb008264.easy_pill_android.R;
import com.google.android.material.navigation.NavigationView;

public class PharmacistHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    String username;
    String userEmail;
    String userId;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_home);
        Toolbar toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        username = getIntent().getExtras().getString("username");
        userEmail = getIntent().getExtras().getString("email");
        userId = getIntent().getExtras().getString("userId");
        userRole = getIntent().getExtras().getString("role");
        System.out.println(username);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        navUsername.setText(username);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setElevation(0);

        getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new PharmacistMenuFragment()).commit();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new PharmacistMenuFragment());

        }
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home_pharmacist:
                getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new PharmacistMenuFragment()).commit();
                break;
            case R.id.nav_customers_pharmacist:
                getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminCustomersFragment()).commit();
                break;
            case R.id.nav_doctors_pharmacist:
                getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminDoctorsFragment()).commit();
                break;
            case R.id.nav_pharmacists_pharmacist:
                getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminPharmacistsFragment()).commit();
                break;
            case R.id.nav_orders_pharmacist:
                getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminOrderFragment()).commit();
                break;
            case R.id.nav_medicine_pharmacist:
                getSupportFragmentManager().beginTransaction().replace(R.id.pharmacistFragmentContainer, new AdminMedicineFragment()).commit();
                break;
            case R.id.nav_logout_pharmacist: {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.putString("username", "");
                editor.putString("email", "");
                editor.putString("userId", "");
                editor.putString("role", "");
                editor.apply();
                startActivity(intent);
                finish();
                break;
            }


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}