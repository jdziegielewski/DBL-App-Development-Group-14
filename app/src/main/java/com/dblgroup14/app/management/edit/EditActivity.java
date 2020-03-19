package com.dblgroup14.app.management.edit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.challenges.challenge1;
import com.dblgroup14.app.management.ManageChallengesFragment;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.entities.Alarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;

//https://abhiandroid.com/ui/timepicker
public class EditActivity extends AppCompatActivity {
    
    TextView time;
    Alarm alarm;
    SeekBar seekBar;
    AudioManager audioManager;
    
    Button button;
    TextView selectedChallenge;
    AlertDialog alertDialog1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActionBarWithGradient();
    
        // getIntent() is a method from the started activity
        Intent myIntent = getIntent(); // gets the previously created intent
        boolean editAlarm = myIntent.getBooleanExtra("edit_alarm", false);
        
        if(editAlarm){
            ((AppCompatActivity) this).getSupportActionBar().setTitle("Edit My Alarm");
            int id = myIntent.getIntExtra("alarm_id", 0);
            Toast.makeText(this,"ID = " + id, Toast.LENGTH_LONG).show();
            AsyncTask.execute(() -> {
                alarm = AppDatabase.db().alarmDao().get(id);
            });
        } else {
            ((AppCompatActivity) this).getSupportActionBar().setTitle("My New Alarm");
            alarm = new Alarm("My Alarm", 7, 0, true, 80, false, 1);
        }
        
        setContentView(R.layout.activity_edit);
        // initiate the edit text
        time = findViewById(R.id.time);
        setTimeView(alarm.hours, alarm.minutes);
        
        // perform click event listener on edit text
        time.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog timePicker;
            timePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    setTimeView(selectedHour, selectedMinute);
                    alarm.setHours(selectedHour);
                    alarm.setMinutes(selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            timePicker.show();
        });
        
        EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        nameEdit.setText(alarm.name);
    
        // Create add alarm button
        FloatingActionButton fab = findViewById(R.id.addAlarmButton);
        fab.setOnClickListener(view1 -> {
            alarm.setName(nameEdit.getText().toString());
            AsyncTask.execute(() -> {
                AppDatabase.db().alarmDao().store(alarm);
            });
            setAlarm();
            finish();
        });
        
        ImageView repeat = findViewById(R.id.repeat);
        repeat.setOnClickListener(view -> {
           //repeat is true/false
        });
        
        TextView[] repeatDays = {findViewById(R.id.repeatDay0), findViewById(R.id.repeatDay1), findViewById(R.id.repeatDay2),
                findViewById(R.id.repeatDay3), findViewById(R.id.repeatDay4), findViewById(R.id.repeatDay5), findViewById(R.id.repeatDay6)};
        
        for (TextView rDays : repeatDays) {
            setBackground(rDays);
        }
    
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekBar.setMax(maxVolume);
        int initialVol = maxVolume* alarm.volume/100;
        seekBar.setProgress(initialVol);
    
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, i, 0);
                alarm.setVolume(i/maxVolume*100);
            }
        
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            
            }
        
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            
            }
        });
    
        selectedChallenge = findViewById(R.id.selectChallenge);
    
        selectedChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAlertDialogWithRadioButtonGroup() ;
            }
        });
        
    }
    
    public void CreateAlertDialogWithRadioButtonGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setTitle("Select Your Choice");
        String[] value = {"Rebus","Shaker"};
        builder.setSingleChoiceItems(value, -1, (dialog, item) -> {
        
            switch (item) {
                case 0:
                    selectedChallenge.setText(value[0]);
                    break;
                case 1:
                    selectedChallenge.setText(value[1]);
                    break;
                case 2:
                    selectedChallenge.setText(value[2]);
                    break;
            }
            alertDialog1.dismiss();
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    
    }
    
    public void setTimeView(int hours, int min) {
        if (min >= 0 && min < 10) {
            time.setText(hours + ":0" + min);
        } else {
            time.setText(hours + ":" + min);
        }
    }
    
    public void setBackground(final TextView repeatDay) {
        repeatDay.setOnClickListener(view -> {
            if (repeatDay.getBackground().getConstantState() == getResources().getDrawable(R.drawable.circle_unused).getConstantState()) {
                repeatDay.setTextColor(Color.WHITE);
                repeatDay.setBackgroundResource(R.drawable.circle_used);
            } else if (repeatDay.getBackground().getConstantState() == getResources().getDrawable(R.drawable.circle_used).getConstantState()) {
                repeatDay.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                repeatDay.setBackgroundResource(R.drawable.circle_unused);
            }
            
        });
    }
    
    public void setAlarm(){
        Calendar cal = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, alarm.hours);
        cal.set(Calendar.MINUTE, alarm.minutes);
        cal.set(Calendar.SECOND, 0);
    
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, challenge1.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        
    
        if (cal.compareTo(currentTime) <= 0) {
            // The set Date/Time already passed
            Toast.makeText(getApplicationContext(),
                    "Invalid Date/Time", Toast.LENGTH_LONG).show();
        } else {
            assert alarmMgr != null;
            alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            Toast.makeText(getApplicationContext(),
                    ""+cal.getTime(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void createActionBarWithGradient() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
    }
    
}
//ToDo: automatic next day alarm