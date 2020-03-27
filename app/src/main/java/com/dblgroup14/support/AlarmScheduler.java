package com.dblgroup14.support;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.dblgroup14.app.AlarmActivity;
import com.dblgroup14.app.SleapApplication;
import com.dblgroup14.support.entities.Alarm;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class AlarmScheduler {
    public static void scheduleNext(Alarm alarm) {
        // Create calendar
        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        
        Calendar calendar = (Calendar) now.clone();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.hours);
        calendar.set(Calendar.MINUTE, alarm.minutes);
        
        // Get current day as calendar constant and as alarm index
        int todayCalendar = now.get(Calendar.DAY_OF_WEEK);
        int todayAlarm = todayCalendar - 2;
        if (todayAlarm < 1) {
            todayAlarm = 6;
        }
        
        // Check if alarm has specific day(s) on which it should go off
        boolean hasSpecificDays = false;
        for (boolean b : alarm.days) {
            if (b) {
                hasSpecificDays = true;
                break;
            }
        }
        
        // Find next day that alarm is to be set
        for (int i = 0; i < 8; i++) {                               // process the current day and look 7 days ahead
            int curDayAlarm = (todayAlarm + i) % 7;
            if (alarm.days[curDayAlarm] || !hasSpecificDays) {      // only process day if the alarm is set to go off
                // Get calendar constant for current day
                int curDayCalendar = todayCalendar + i;
                if (curDayCalendar > 7) {
                    curDayCalendar -= 7;
                }
                
                // Set calendar day
                calendar.set(Calendar.DAY_OF_WEEK, curDayCalendar);
                
                // Check if date / time has already passed
                if (calendar.compareTo(now) <= 0) {
                    if (i == 0) {
                        if (hasSpecificDays) {
                            continue;
                        } else {
                            calendar.add(Calendar.DAY_OF_WEEK, 1);      // go to tomorrow
                        }
                    } else {
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);         // go to next week
                    }
                }
                
                // Set alarm for the found date / time
                setAlarm(calendar, alarm);
                
                break;
            }
        }
    }
    
    private static void setAlarm(Calendar calendar, Alarm alarm) {
        Context context = SleapApplication.getContext();
        
        // Create intent
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("alarm_id", alarm.id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        // Schedule alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        
        // Show user a schedule message
        DateFormat format = new SimpleDateFormat("EEEE dd MMM HH:mm");
        Toast.makeText(context, "Alarm will ring next at\n" + format.format(calendar.getTime()), Toast.LENGTH_LONG).show();
    }
    
    public static void unschedule(Alarm alarm) {
        // TODO: Unschedule alarm
    }
}
