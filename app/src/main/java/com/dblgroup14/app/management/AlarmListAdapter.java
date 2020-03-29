package com.dblgroup14.app.management;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dblgroup14.app.EditActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AlarmScheduler;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.entities.Alarm;
import java.util.Locale;

//https://appsandbiscuits.com/listview-tutorial-android-12-ccef4ead27cc
public class AlarmListAdapter extends ArrayAdapter<Alarm> {
    private final Activity activity;    // reference to the activity
    
    /**
     * Initializes a new AlarmListAdapter.
     *
     * @param activity The activity in which this adapter is created
     */
    public AlarmListAdapter(Activity activity) {
        super(activity.getApplicationContext(), R.layout.alarm_row);
        this.activity = activity;
    }
    
    /**
     * Sets the time, deletion, drop down menu, edit button and on/off-button
     *
     * @param position the position of the list
     * @param view     the view
     * @param parent   the view of the parent
     * @return the view rowView
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get alarm object
        Alarm alarm = getItem(position);
        assert alarm != null;
        
        // Inflate new row
        LayoutInflater inflater = activity.getLayoutInflater();
        View newRow = inflater.inflate(R.layout.alarm_row, parent, false);
        
        // Initialize new row
        setTime(newRow, alarm);
        setDelete(newRow, alarm);
        setDropDownMenu(newRow, alarm);
        setEditAlarmButton(newRow, alarm);
        setAlarmState(newRow, alarm);
        
        return newRow;
    }
    
    /**
     * Sets the alarm time and name in the textViews
     *
     * @param newRow The newly inflated row
     * @param alarm  The alarm matching alarm object
     */
    private void setTime(View newRow, Alarm alarm) {
        TextView alarmTimeTextView = newRow.findViewById(R.id.alarmTimeTextView);
        TextView nameTextView = newRow.findViewById(R.id.nameTextView);
        alarmTimeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", alarm.hours, alarm.minutes));
        nameTextView.setText(alarm.name);
    }
    
    /**
     * Sets the onClickLister of the delete button: the alarm is canceled and the alarm deleted
     *
     * @param newRow The newly inflated row
     * @param alarm  The alarm matching alarm object
     */
    private void setDelete(View newRow, final Alarm alarm) {
        ImageView deleteView = newRow.findViewById(R.id.deleteAlarmView);
        deleteView.setOnClickListener(v -> {
            // Unschedule alarm
            AlarmScheduler.unschedule(alarm);
            
            // Delete alarm object
            AsyncTask.execute(() -> AppDatabase.db().alarmDao().delete(alarm));
        });
    }
    
    /**
     * When the edit button is pressed, the edit activity will be started
     *
     * @param newRow The newly inflated row
     * @param alarm  The alarm matching alarm object
     */
    private void setEditAlarmButton(View newRow, Alarm alarm) {
        ImageView editAlarmButton = newRow.findViewById(R.id.editAlarmButton);
        editAlarmButton.setOnClickListener(v -> {
            Intent intentEditAlarm = new Intent(activity, EditActivity.class);
            intentEditAlarm.putExtra(EditActivity.KEY_OBJECT_TYPE, EditActivity.VAL_OBJECT_ALARM);
            intentEditAlarm.putExtra(EditActivity.KEY_OBJECT_ID, alarm.id);
            activity.startActivity(intentEditAlarm);
        });
    }
    
    /**
     * If the alarm box has been clicked, the drop down menu is shown
     *
     * @param newRow The newly inflated row
     * @param alarm  The alarm matching alarm object
     */
    private void setDropDownMenu(View newRow, Alarm alarm) {
        ConstraintLayout dropDownView = newRow.findViewById(R.id.dropDownView);
        ConstraintLayout mainBoxAlarm = newRow.findViewById(R.id.mainBoxAlarm);
        dropDownView.setVisibility(View.GONE);
        mainBoxAlarm.setOnClickListener(view12 -> {
            if (dropDownView.getVisibility() == newRow.VISIBLE) {
                dropDownView.setVisibility(View.GONE);
            } else if (dropDownView.getVisibility() == newRow.GONE) {
                dropDownView.setVisibility(View.VISIBLE);
            }
        });
    }
    
    /**
     * Set the layout to the state of the alarm: enabled or disabled
     *
     * @param newRow The newly inflated row
     * @param alarm  The alarm matching alarm object
     */
    private void setAlarmState(View newRow, final Alarm alarm) {
        ImageView alarmOnOffView = newRow.findViewById(R.id.alarmOnOffView);
        if (alarm.enabled) {
            alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_on);
        } else {
            alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_off);
        }
        
        alarmOnOffView.setOnClickListener(v -> {
            // Change button background resource and (un)schedule alarm
            if (alarm.enabled) {
                alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_off);
                AlarmScheduler.unschedule(alarm);
            } else {
                alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_on);
                AlarmScheduler.scheduleNext(alarm);
            }
            
            // Change enabled state
            alarm.enabled = !alarm.enabled;
            
            // Store object in database
            AsyncTask.execute(() -> AppDatabase.db().alarmDao().store(alarm));
        });
    }
}