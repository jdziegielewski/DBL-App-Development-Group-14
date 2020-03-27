package com.dblgroup14.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.dblgroup14.support.SimpleDatabase;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Increase amount of total challenges
        SharedPreferences db = SimpleDatabase.getSharedPreferences();
        int totalChallenges = db.getInt(SimpleDatabase.TOTAL_CHALLENGES, 0) + 1;
        db.edit().putInt(SimpleDatabase.TOTAL_CHALLENGES, totalChallenges).apply();
        
        // Start alarm activity
        Intent alarmActivityIntent = new Intent();
        alarmActivityIntent.setClassName("com.dblgroup14.app", AlarmActivity.class.getName());
        alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        alarmActivityIntent.putExtras(intent);
        context.startActivity(alarmActivityIntent);
    }
}
