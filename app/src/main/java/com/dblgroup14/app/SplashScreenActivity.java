package com.dblgroup14.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreenActivity extends AppCompatActivity {
    
    int SPLASH_TIME = 500;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    
        getSupportActionBar().hide();
    
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                Intent mySuperIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mySuperIntent);
            
                //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
                finish();
            
            }
        }, SPLASH_TIME);
    
    }
}
