package com.dblgroup14;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.dblgroup14.app.R;
import com.dblgroup14.app.AlarmScheduler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
/*MainActivity of the app that is present when the app is started for the first time and set ups other tabs in the app to be explored by the user*/
public class MainActivity extends AppCompatActivity {
    // Notification channel
    private static final String CHANNEL_1_ID = "channel1";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Request permission to draw over other apps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Please allow the Sleap App to display over other applications")
                        .setTitle("Display over other applications")
                        .setPositiveButton("NEXT", (o, e) -> {
                            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                            finish();
                        })
                        .create();
                dialog.show();
            }
        }
        
        // Setup navigation tabs
        setupNavigationTabs();
        createActionBarWithGradient();
        
        // Setup notification channel
        CreateNotificationChannel();
       
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
    
    /**
     * Sets up the notification Channel
     */
    private void CreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            
        }
    }
    
    /**
     * create setup notification class
     */
    public static void CreateNotification() {
        Context context = SleapApplication.getContext();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String time = AlarmScheduler.time;
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        
        // Setup notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_alarm_on)
                .setContentTitle("Alarm set")
                .setContentText(time)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setSound(null)
                .setContentIntent(contentIntent)
                .build();
        notificationManager.notify(1, notification);
    }
}
