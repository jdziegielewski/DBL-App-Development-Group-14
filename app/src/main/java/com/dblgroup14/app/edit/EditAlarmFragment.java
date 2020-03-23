package com.dblgroup14.app.edit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.AlarmActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.dao.HostDaoInterface;
import com.dblgroup14.support.entities.Alarm;
import java.util.Calendar;

/**
 * The fragment that is used to edit an alarm instance.
 * <p>
 * TODO: automatic next day alarm
 * TODO: repeat button
 * TODO: on lock screen alarm
 */
public class EditAlarmFragment extends EditFragment<Alarm> {
    private TextView time;
    private EditText nameEdit;
    private TextView selectedChallenge;
    private TextView[] repeatDays;
    private SeekBar seekBar;
    
    private AudioManager audioManager;
    private AlertDialog alertDialog;
    private AppCompatActivity activity;
    
    @Override
    protected void initialize(View view) {
        // Get activity
        activity = (AppCompatActivity) getActivity();
        
        // Set title
        if (isEdit) {
            activity.getSupportActionBar().setTitle("Edit My Alarm");
        } else {
            activity.getSupportActionBar().setTitle("My New Alarm");
        }
        
        // Initialize repeat days
        repeatDays = new TextView[]{
                view.findViewById(R.id.repeatDay0), view.findViewById(R.id.repeatDay1), view.findViewById(R.id.repeatDay2),
                view.findViewById(R.id.repeatDay3), view.findViewById(R.id.repeatDay4), view.findViewById(R.id.repeatDay5),
                view.findViewById(R.id.repeatDay6)
        };
        for (TextView repeatDay : repeatDays) {
            setOnClick(repeatDay);
        }
        updateRepeatDays();
        
        // Initialize the time view
        time = view.findViewById(R.id.time);
        time.setOnClickListener(v -> {
            // Get current time
            Calendar currentTime = Calendar.getInstance();
            int curHour = currentTime.get(Calendar.HOUR_OF_DAY);
            int curMin = currentTime.get(Calendar.MINUTE);
            
            // Show time picker
            TimePickerDialog timePicker = new TimePickerDialog(activity, (tp, hour, min) -> {
                editObject.setTime(hour, min);
                updateTimeView();
            }, curHour, curMin, true); //Yes 24 hour time
            timePicker.show();
        });
        updateTimeView();
        
        // Initialize name edit text
        nameEdit = view.findViewById(R.id.nameEdit);
        nameEdit.setText(editObject.name);
        
        // Initialize repeat button
        ImageView repeatButton = view.findViewById(R.id.repeatButton);
        if (editObject.repeats) {
            repeatButton.setBackgroundResource(R.drawable.ic_repeat);
        } else {
            repeatButton.setBackgroundResource(R.drawable.ic_repeat_one);
        }
        repeatButton.setOnClickListener(v -> {
            if (editObject.repeats) {
                repeatButton.setBackgroundResource(R.drawable.ic_repeat);
            } else {
                repeatButton.setBackgroundResource(R.drawable.ic_repeat_one);
            }
            editObject.setRepeats(!editObject.repeats);
        });
        
        // Initialize volume seek bar
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setMax(maxVolume);
        seekBar.setProgress((int) ((editObject.volume / 100.0) * maxVolume));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int vol, boolean userAction) {
                if (userAction) {
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, vol, 0);
                    editObject.setVolume((int) ((vol / (double) maxVolume) * 100));
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        // Initialize select selected challenge text view
        selectedChallenge = view.findViewById(R.id.selectChallenge);
        // TODO: Initialize selected challenge value
        selectedChallenge.setOnClickListener(v -> showChooseChallengeDialog());
    }
    
    @Override
    protected boolean update() {
        // Get and trim name
        String name = nameEdit.getText().toString().trim();
        if (name.isEmpty()) {
            return false;
        }
        
        // Set alarm name
        editObject.setName(name);
        
        return true;
    }
    
    @Override
    protected Alarm createNew() {
        int curHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int curMin = Calendar.getInstance().get(Calendar.MINUTE);
        return new Alarm("New Alarm", curHour, curMin, true, 80, false, 1);
    }
    
    @Override
    protected HostDaoInterface<Alarm> dao() {
        return AppDatabase.db().alarmDao();
    }
    
    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_edit_alarm;
    }
    
    private void showChooseChallengeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Your Choice");
        
        String[] values = {"Rebus", "Shaker"};
        builder.setSingleChoiceItems(values, -1, (dialog, item) -> {
            selectedChallenge.setText(values[item]);
            alertDialog.dismiss();
            // TODO: Set alarm challenge
        });
        
        alertDialog = builder.create();
        alertDialog.show();
    }
    
    private void updateTimeView() {
        int hours = editObject.hours, min = editObject.minutes;
        if (min >= 0 && min < 10) {
            time.setText(hours + ":0" + min);
        } else {
            time.setText(hours + ":" + min);
        }
    }
    
    private void updateRepeatDays() {
        for (int i = 0; i < repeatDays.length; i++) {
            if (editObject.days[i]) {
                repeatDays[i].setTextColor(Color.WHITE);
                repeatDays[i].setBackgroundResource(R.drawable.circle_used);
            } else {
                repeatDays[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                repeatDays[i].setBackgroundResource(R.drawable.circle_unused);
            }
        }
    }
    
    private void setOnClick(final TextView repeatDay) {
        repeatDay.setOnClickListener(view -> {
            if (repeatDay.getBackground().getConstantState() == getResources().getDrawable(R.drawable.circle_unused).getConstantState()) {
                repeatDay.setTextColor(Color.WHITE);
                repeatDay.setBackgroundResource(R.drawable.circle_used);
                switch (view.getId()) {
                    case R.id.repeatDay0:
                        editObject.days[0] = true;
                        break;
                    case R.id.repeatDay1:
                        editObject.days[1] = true;
                        break;
                    case R.id.repeatDay2:
                        editObject.days[2] = true;
                        break;
                    case R.id.repeatDay3:
                        editObject.days[3] = true;
                        break;
                    case R.id.repeatDay4:
                        editObject.days[4] = true;
                        break;
                    case R.id.repeatDay5:
                        editObject.days[5] = true;
                        break;
                    case R.id.repeatDay6:
                        editObject.days[6] = true;
                        break;
                }
            } else if (repeatDay.getBackground().getConstantState() == getResources().getDrawable(R.drawable.circle_used).getConstantState()) {
                repeatDay.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                repeatDay.setBackgroundResource(R.drawable.circle_unused);
                switch (view.getId()) {
                    case R.id.repeatDay0:
                        editObject.days[0] = false;
                        break;
                    case R.id.repeatDay1:
                        editObject.days[1] = false;
                        break;
                    case R.id.repeatDay2:
                        editObject.days[2] = false;
                        break;
                    case R.id.repeatDay3:
                        editObject.days[3] = false;
                        break;
                    case R.id.repeatDay4:
                        editObject.days[4] = false;
                        break;
                    case R.id.repeatDay5:
                        editObject.days[5] = false;
                        break;
                    case R.id.repeatDay6:
                        editObject.days[6] = false;
                        break;
                }
            }
        });
    }
    
    private void setAlarm() {
        Calendar cal = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, editObject.hours);
        cal.set(Calendar.MINUTE, editObject.minutes);
        cal.set(Calendar.SECOND, 0);
        
        AlarmManager alarmMgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), editObject.id, intent, 0);
        
        if (editObject.enabled) {
            if (cal.compareTo(currentTime) <= 0) {
                // The set Date/Time already passed
                Toast.makeText(getContext(), "Invalid Date/Time", Toast.LENGTH_LONG).show();
            } else {
                assert alarmMgr != null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                } else {
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                Toast.makeText(getContext(), cal.getTime().toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            if (alarmMgr != null) {
                alarmMgr.cancel(pendingIntent);
            }
        }
    }
    
    public void noRepeat() {
        int trueCount = 0;
        for (boolean b : editObject.days) {
            if (b) {
                trueCount++;
            }
        }
        
        if (trueCount == 0) {
        
        }
    }
    
    public void repeat() {
    
    }
}