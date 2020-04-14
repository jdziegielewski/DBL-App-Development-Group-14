package com.dblgroup14.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME = 500;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        
        // Start MainActivity after a while
        new Handler().postDelayed(() -> {
            Intent mySuperIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mySuperIntent);
            finish();
        }, SPLASH_TIME);
    }
}
