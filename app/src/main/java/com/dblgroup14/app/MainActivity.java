package com.dblgroup14.app;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.dblgroup14.support.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Setup navigation tabs
        setupNavigationTabs();
        createActionBarWithGradient();
    }
    
    /**
     * Sets up the four navigation tabs in the main screen
     */
    private void setupNavigationTabs() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.tab_alarms, R.id.tab_challenges, R.id.tab_score,
                R.id.tab_user).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
    
    /**
     * Sets the background of the actionbar to a gradient color
     */
    private void createActionBarWithGradient() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
    }
    
}
