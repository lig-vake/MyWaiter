package com.example.mywaiter;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mywaiter.ui.dashboard.DashboardFragment;
import com.example.mywaiter.ui.home.HomeFragment;
import com.example.mywaiter.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    final Fragment homeFragment = new HomeFragment();
    final Fragment dashboardFragment = new DashboardFragment();
    final Fragment profileFragment = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();
        fm.beginTransaction().add(R.id.main_container, dashboardFragment, "2").hide(dashboardFragment).commit();
        fm.beginTransaction().add(R.id.main_container, profileFragment, "3").hide(profileFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(new Bundle());
    }


    final private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            System.out.println(active);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).commit();
                    fm.beginTransaction().show(homeFragment).commit();
                    active = homeFragment;
                    return true;
                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(active).commit();
                    fm.beginTransaction().show(dashboardFragment).commit();
                    active = dashboardFragment;
                    return true;
                case R.id.navigation_profile:
                    fm.beginTransaction().hide(active).commit();
                    fm.beginTransaction().show(profileFragment).commit();
                    active = profileFragment;
                    return true;
            }
            return false;
        }
    };
}