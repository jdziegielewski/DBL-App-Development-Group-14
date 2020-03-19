package com.dblgroup14.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.dblgroup14.app.challenges.challenge1;
import com.dblgroup14.support.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    GradientDrawable gradientdrawable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Instantiate database
        AppDatabase.createDatabase(getApplicationContext());
        
        // Setup navigation tabs
        setupNavigationTabs();
        createActionBarWithGradient();
    }
    
    private void setupNavigationTabs() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.tab_alarms, R.id.tab_challenges, R.id.tab_score,
                R.id.tab_user).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
    
    private void createActionBarWithGradient() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
    }

}
