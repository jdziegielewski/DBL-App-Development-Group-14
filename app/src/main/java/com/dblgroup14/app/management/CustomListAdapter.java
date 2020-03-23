package com.dblgroup14.app.management;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dblgroup14.app.EditActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.challenges.challenge1;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.entities.Alarm;
import java.util.Calendar;

//https://appsandbiscuits.com/listview-tutorial-android-12-ccef4ead27cc
public class CustomListAdapter extends ArrayAdapter<Alarm> {
    private final Activity activity;    // reference to the activity
    
    public CustomListAdapter(Activity activity) {
        super(activity.getApplicationContext(), R.layout.listview_row);
        this.activity = activity;
    }
    
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null, true);
        
        //this code gets references to objects in the listview_row.xml file
        TextView alarmTimeTextView = rowView.findViewById(R.id.alarmTimeTextView);
        TextView nameTextView = rowView.findViewById(R.id.nameTextView);
        
        //this code sets the values of the objects to values from the arrays
        Alarm alarm = getItem(position);
        String hours = String.format("%02d", alarm.hours);
        String min = String.format("%02d", alarm.minutes);
        
        alarmTimeTextView.setText(String.format("%s:%s", hours, min));
        nameTextView.setText(alarm.name);
        
        ImageView deleteView = rowView.findViewById(R.id.deleteAlarmView);
        deleteView.setOnClickListener(view1 -> {
            AsyncTask.execute(() -> {
                AppDatabase.db().alarmDao().delete(alarm);
            });
        });
        
        ConstraintLayout dropDownView = rowView.findViewById(R.id.dropDownView);
        ConstraintLayout mainBoxAlarm = rowView.findViewById(R.id.mainBoxAlarm);
        dropDownView.setVisibility(View.GONE);
        mainBoxAlarm.setOnClickListener(view12 -> {
            if (dropDownView.getVisibility() == rowView.VISIBLE) {
                dropDownView.setVisibility(View.GONE);
            } else if (dropDownView.getVisibility() == rowView.GONE) {
                dropDownView.setVisibility(View.VISIBLE);
            }
        });
        
        ImageView editAlarmButton = rowView.findViewById(R.id.editAlarmButton);
        editAlarmButton.setOnClickListener(view13 -> {
            Intent intentEditAlarm = new Intent(activity, EditActivity.class);
            intentEditAlarm.putExtra("object", "alarm");
            intentEditAlarm.putExtra("id", alarm.id);
            activity.startActivity(intentEditAlarm);
        });
        
        ImageView alarmOnOffView = rowView.findViewById(R.id.alarmOnOffView);
        if (alarm.enabled) {
            alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_on);
        } else {
            alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_off);
        }
        
        AlarmManager alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), challenge1.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), alarm.id, intent, 0);
        
        alarmOnOffView.setOnClickListener(view14 -> {
            if (alarmOnOffView.getBackground().getConstantState() == activity.getResources().getDrawable(R.drawable.ic_alarm_on).getConstantState()) {
                alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_off);
                alarm.setEnabled(false);
                if (alarmMgr != null) {
                    alarmMgr.cancel(pendingIntent);
                }
            } else if (alarmOnOffView.getBackground().getConstantState() ==
                    activity.getResources().getDrawable(R.drawable.ic_alarm_off).getConstantState()) {
                alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_on);
                alarm.setEnabled(true);
                
                Calendar cal = Calendar.getInstance();
                Calendar currentTime = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, alarm.hours);
                cal.set(Calendar.MINUTE, alarm.minutes);
                cal.set(Calendar.SECOND, 0);
                
                if (cal.compareTo(currentTime) > 0) {
                    assert alarmMgr != null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                    }
                }
            }
            AsyncTask.execute(() -> {
                AppDatabase.db().alarmDao().store(alarm);
            });
        });
        
        return rowView;
    }
    //ToDo: update alarmManager
}