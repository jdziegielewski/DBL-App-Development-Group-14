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
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dblgroup14.app.EditActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.challenges.RebusChallengeFragment;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.entities.Alarm;
import java.util.Calendar;
import java.util.Locale;

//https://appsandbiscuits.com/listview-tutorial-android-12-ccef4ead27cc
public class CustomListAdapter extends ArrayAdapter<Alarm> {
    private final Activity activity;    // reference to the activity
    private AlarmManager alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    private Intent intent = new Intent(getContext(), RebusChallengeFragment.class);
    private PendingIntent pendingIntent;
    private Alarm alarm;
    private View rowView;
    
    /**
     * Sets the activity
     *
     * @param activity the activity
     */
    CustomListAdapter(Activity activity) {
        super(activity.getApplicationContext(), R.layout.listview_row);
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
        LayoutInflater inflater = activity.getLayoutInflater();
        rowView = inflater.inflate(R.layout.listview_row, null, true);
        alarm = getItem(position);
        assert alarm != null;
        pendingIntent = PendingIntent.getActivity(getContext(), alarm.id, intent, 0);
        
        setTime();
        setDelete();
        setDropDownMenu();
        setEditAlarmButton();
        setAlarmState(alarm);
        
        return rowView;
    }
    
    /**
     * Sets the alarm time and name in the textViews
     */
    private void setTime() {
        TextView alarmTimeTextView = rowView.findViewById(R.id.alarmTimeTextView);
        TextView nameTextView = rowView.findViewById(R.id.nameTextView);
        alarmTimeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", alarm.hours, alarm.minutes));
        nameTextView.setText(alarm.name);
    }
    
    /**
     * Sets the onClickLister of the delete button: the alarm is canceled and the alarm deleted
     */
    private void setDelete() {
        ImageView deleteView = rowView.findViewById(R.id.deleteAlarmView);
        deleteView.setOnClickListener(view1 -> {
            alarm.setEnabled(false);
            if (alarmMgr != null) {
                alarmMgr.cancel(pendingIntent);
            }
            AsyncTask.execute(() -> {
                AppDatabase.db().alarmDao().delete(alarm);
            });
        });
    }
    
    /**
     * When the edit button is pressed, the edit activity will be started
     */
    private void setEditAlarmButton() {
        ImageView editAlarmButton = rowView.findViewById(R.id.editAlarmButton);
        editAlarmButton.setOnClickListener(view13 -> {
            Intent intentEditAlarm = new Intent(activity, EditActivity.class);
            intentEditAlarm.putExtra("object", "alarm");
            intentEditAlarm.putExtra("id", alarm.id);
            activity.startActivity(intentEditAlarm);
        });
    }
    
    /**
     * If the alarm box has been clicked, the drop down menu is shown
     */
    private void setDropDownMenu() {
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
    }
    
    /**
     * Set the layout to the state of the alarm: enabled or disabled
     */
    private void setAlarmState(Alarm alarm) {
        ImageView alarmOnOffView = rowView.findViewById(R.id.alarmOnOffView);
        if (alarm.enabled) {
            alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_on);
        } else {
            alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_off);
        }
        
        changeAlarmStateOnClick(alarmOnOffView, alarm);
    }
    
    /**
     * Sets the alarm
     *
     * @param cal the time and date of the alarm
     */
    private void setAlarm(Calendar cal) {
        Calendar currentTime = Calendar.getInstance();
        if (cal.compareTo(currentTime) > 0) {
            assert alarmMgr != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            } else {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
        }
    }
    
    /**
     * Creates a calender at the time and date the alarm should go off
     *
     * @param alarm the alarm object
     * @return the calender with the set time and date
     */
    private Calendar createCal(Alarm alarm) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, alarm.hours);
        cal.set(Calendar.MINUTE, alarm.minutes);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }
    
    /**
     * Changes the state of the alarm when the on/off-button is pressed. When the alarm is disabled the alarm get canceled, when the alarm is
     * enabled the alarm gets set
     *
     * @param alarmOnOffView the ImageView of the on/off-button
     */
    private void changeAlarmStateOnClick(ImageView alarmOnOffView, Alarm alarm) {
        alarmOnOffView.setOnClickListener(view14 -> {
            if (alarmOnOffView.getBackground().getConstantState() == activity.getResources().getDrawable(R.drawable.ic_alarm_on).getConstantState()) {
                alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_off);
                alarm.setEnabled(false);
                if (alarmMgr != null) {
                    alarmMgr.cancel(pendingIntent);
                }
            } else if (alarmOnOffView.getBackground().getConstantState() == activity.getResources().getDrawable(R.drawable.ic_alarm_off).getConstantState()) {
                alarmOnOffView.setBackgroundResource(R.drawable.ic_alarm_on);
                alarm.setEnabled(true);
                
                Calendar cal = createCal(alarm);
                setAlarm(cal);
            }
            AsyncTask.execute(() -> {
                AppDatabase.db().alarmDao().store(alarm);
            });
        });
    }
    
    //ToDo: update alarmManager
    //ToDO: delete alarm
}