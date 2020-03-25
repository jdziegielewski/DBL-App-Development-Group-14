package com.dblgroup14.app.edit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.AlarmActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AlarmScheduler;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.dao.HostDaoInterface;
import com.dblgroup14.support.entities.Alarm;
import com.dblgroup14.support.entities.Challenge;
import java.util.Calendar;
import java.util.List;

/**
 * The fragment that is used to edit an alarm instance.
 *
 * TODO: automatic next day alarm
 * TODO: repeat button
 * TODO: on lock screen alarm
 */
public class EditAlarmFragment extends EditFragment<Alarm> {
    private TextView time;
    private EditText nameEdit;
    private TextView[] repeatDays;
    private SeekBar volumeSeekBar;
    private LinearLayout challengesListContainer;
    
    private double maxVolume;
    private List<Challenge> challengesList;
    
    @Override
    protected void initialize(View view) {
        // Get activity
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        
        // Set title
        if (isEdit) {
            activity.getSupportActionBar().setTitle("Edit My Alarm");
        } else {
            activity.getSupportActionBar().setTitle("My New Alarm");
        }
        
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
        nameEdit = view.findViewById(R.id.alarmNameInput);
        nameEdit.setText(editObject.name);
        
        // Initialize repeat button
        ImageView repeatButton = view.findViewById(R.id.alarmRepeatBtn);
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
        
        // Initialize repeat days
        repeatDays = new TextView[] {
                view.findViewById(R.id.repeatDay0), view.findViewById(R.id.repeatDay1), view.findViewById(R.id.repeatDay2),
                view.findViewById(R.id.repeatDay3), view.findViewById(R.id.repeatDay4), view.findViewById(R.id.repeatDay5),
                view.findViewById(R.id.repeatDay6)
        };
        for (TextView repeatDay : repeatDays) {
            setOnClick(repeatDay);
        }
        updateRepeatDays();
        
        // Get device's maximum volume
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        
        // Initialize volume seek bar
        volumeSeekBar = view.findViewById(R.id.alarmVolumeSeekBar);
        volumeSeekBar.setMax((int) maxVolume);
        volumeSeekBar.setProgress((int) (editObject.volume * maxVolume / 100));
        
        // Get challenges list container
        challengesListContainer = view.findViewById(R.id.challengesListContainer);
        
        // Initialize challenges live data
        LiveData<List<Challenge>> allChallenges = AppDatabase.db().challengeDao().all();
        allChallenges.observe(getViewLifecycleOwner(), l -> {
            challengesList = l;
            inflateChallengesList();
        });
    }
    
    @Override
    protected boolean update() {
        // Set alarm name
        String name = nameEdit.getText().toString().trim();
        if (name.isEmpty()) {
            return false;
        }
        editObject.setName(name);
        
        // Set alarm volume
        editObject.setVolume((int) ((volumeSeekBar.getProgress() / maxVolume) * 100));
        
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
    
    @Override
    public boolean save() {
        // Save alarm
        if (!super.save()) {
            return false;
        }
        
        // Schedule next alarm time
        AlarmScheduler.scheduleNext(editObject);
        
        return true;
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
            int dayNumber = -1;
            switch (view.getId()) {
                case R.id.repeatDay0:
                    dayNumber = 0;
                    break;
                case R.id.repeatDay1:
                    dayNumber = 1;
                    break;
                case R.id.repeatDay2:
                    dayNumber = 2;
                    break;
                case R.id.repeatDay3:
                    dayNumber = 3;
                    break;
                case R.id.repeatDay4:
                    dayNumber = 4;
                    break;
                case R.id.repeatDay5:
                    dayNumber = 5;
                    break;
                case R.id.repeatDay6:
                    dayNumber = 6;
                    break;
            }
            
            if (editObject.days[dayNumber]) {
                repeatDay.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                repeatDay.setBackgroundResource(R.drawable.circle_unused);
                editObject.setDay(dayNumber, false);
            } else {
                repeatDay.setTextColor(Color.WHITE);
                repeatDay.setBackgroundResource(R.drawable.circle_used);
                editObject.setDay(dayNumber, true);
            }
        });
    }
    
    private void inflateChallengesList() {
        // Clear any existing views
        challengesListContainer.removeAllViews();
        
        // Inflate new view for each challenge
        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < challengesList.size(); i++) {
            Challenge c = challengesList.get(i);
            View listItem = inflater.inflate(R.layout.challenge_list_item, challengesListContainer, false);
            
            // Set name
            ((TextView) listItem.findViewById(R.id.challengeName)).setText(c.name);
            
            // Set on click change
            Switch enableChallengeSwitch = listItem.findViewById(R.id.enableChallengeSwitch);
            enableChallengeSwitch.setChecked(editObject.hasChallenge(c.id));
            enableChallengeSwitch.setOnCheckedChangeListener((o, e) -> {
                if (editObject.hasChallenge(c.id)) {
                    editObject.removeChallenge(c.id);
                } else {
                    editObject.addChallenge(c.id);
                }
            });
            
            challengesListContainer.addView(listItem);
        }
    }
}