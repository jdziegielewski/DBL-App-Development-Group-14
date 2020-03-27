package com.dblgroup14.app.edit;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AlarmScheduler;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.dao.HostDaoInterface;
import com.dblgroup14.support.entities.Alarm;
import com.dblgroup14.support.entities.Challenge;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * The fragment that is used to edit an alarm instance.
 */
public class EditAlarmFragment extends EditFragment<Alarm> {
    private EditText nameEdit;
    private SeekBar volumeSeekBar;
    private double maxVolume;
    private List<Challenge> challengesList;
    private AppCompatActivity activity;
    private View view;
    
    @Override
    protected void initialize(View views) {
        view = views;
        
        // Get activity
        activity = (AppCompatActivity) getActivity();
        
        // Initialize UI
        setTitle();
        initializeTimeView();
        initializeNameEditText();
        initializeRepeatButton();
        initializeRepeatDays();
        initializeSeekBar();
        initializeChallengesLiveData();
    }
    
    @Override
    protected boolean update() {
        // Set alarm name
        String name = nameEdit.getText().toString().trim();
        if (name.isEmpty()) {
            return false;
        }
        editObject.setName(name);
        editObject.setVolume((int) ((volumeSeekBar.getProgress() / maxVolume) * 100));
        return true;
    }
    
    @Override
    protected void saveComplete(long rowId) {
        // Set ID of alarm object if not already set
        if (editObject.id <= 0) {
            editObject.id = (int) rowId;
        }
        
        // Schedule next alarm
        getActivity().runOnUiThread(() -> AlarmScheduler.scheduleNext(editObject));
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
    
    /**
     * Sets the title in the action bar depending on if the alarm need to be created or edited
     */
    private void setTitle() {
        if (activity.getSupportActionBar() != null) {
            if (isEdit) {
                activity.getSupportActionBar().setTitle("Edit My Alarm");
            } else {
                activity.getSupportActionBar().setTitle("My New Alarm");
            }
        }
    }
    
    /**
     * Sets the timeView to the current time, sets a timePicker when the user clicks on the timeView
     */
    private void initializeTimeView() {
        TextView time = view.findViewById(R.id.time);
        time.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            int curHour = currentTime.get(Calendar.HOUR_OF_DAY);
            int curMin = currentTime.get(Calendar.MINUTE);
            
            TimePickerDialog timePicker = new TimePickerDialog(activity, (tp, hour, min) -> {
                editObject.setTime(hour, min);
                updateTimeView(time);
            }, curHour, curMin, true); //Yes 24 hour time
            timePicker.show();
        });
        updateTimeView(time);
    }
    
    /**
     * Updates the timeView to the chosen time
     *
     * @param time the TextView that shows the time
     */
    private void updateTimeView(TextView time) {
        int hours = editObject.hours, min = editObject.minutes;
        time.setText(String.format(Locale.getDefault(), "%02d:%02d", hours, min));
    }
    
    /**
     * Sets the name of the alarm in the nameEditText
     */
    private void initializeNameEditText() {
        nameEdit = view.findViewById(R.id.alarmNameInput);
        nameEdit.setText(editObject.name);
    }
    
    /**
     * Sets the repeat button to the right drawable and changes the layout and sets the repeat variable when the repeat button is clicked
     */
    private void initializeRepeatButton() {
        ImageView repeatButton = view.findViewById(R.id.alarmRepeatBtn);
        if (editObject.repeats) {
            repeatButton.setBackgroundResource(R.drawable.ic_repeat);
        } else {
            repeatButton.setBackgroundResource(R.drawable.ic_repeat_gray);
        }
        repeatButton.setOnClickListener(v -> {
            if (editObject.repeats) {
                repeatButton.setBackgroundResource(R.drawable.ic_repeat_gray);
            } else {
                repeatButton.setBackgroundResource(R.drawable.ic_repeat);
            }
            editObject.setRepeats(!editObject.repeats);
        });
    }
    
    /**
     * Sets for every day an onClicklistener and changes the layout of the days to the enabled layouts if the day is enabled
     */
    private void initializeRepeatDays() {
        TextView[] repeatDays = new TextView[] {
                view.findViewById(R.id.repeatDay0), view.findViewById(R.id.repeatDay1), view.findViewById(R.id.repeatDay2),
                view.findViewById(R.id.repeatDay3), view.findViewById(R.id.repeatDay4), view.findViewById(R.id.repeatDay5),
                view.findViewById(R.id.repeatDay6)
        };
        
        for (TextView repeatDay : repeatDays) {
            setOnClick(repeatDay);
        }
        updateRepeatDays(repeatDays);
    }
    
    /**
     * Sets the max volume and current volume in the seek bar
     */
    private void initializeSeekBar() {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar = view.findViewById(R.id.alarmVolumeSeekBar);
        volumeSeekBar.setMax((int) maxVolume);
        volumeSeekBar.setProgress((int) (editObject.volume * maxVolume / 100));
    }
    
    /**
     * Puts all the available challenges in the layout
     */
    private void initializeChallengesLiveData() {
        LinearLayout challengesListContainer = view.findViewById(R.id.challengesListContainer);
        LiveData<List<Challenge>> allChallenges = AppDatabase.db().challengeDao().all();
        allChallenges.observe(getViewLifecycleOwner(), l -> {
            challengesList = l;
            inflateChallengesList(challengesListContainer);
        });
    }
    
    /**
     * Sets the layout of the days to corresponding layout: enabled or not
     *
     * @param repeatDays an arrayList of the textViews of the days
     */
    private void updateRepeatDays(TextView[] repeatDays) {
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
    
    /**
     * Sets onClickListeners for all days: when the day is clicked, the layout changes
     *
     * @param repeatDay the textView of a certain day
     */
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
    
    /**
     * Clears any existing views, inflates a new view for each challenge
     *
     * @param challengesListContainer the linearLayout of the containers
     */
    private void inflateChallengesList(LinearLayout challengesListContainer) {
        challengesListContainer.removeAllViews();
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