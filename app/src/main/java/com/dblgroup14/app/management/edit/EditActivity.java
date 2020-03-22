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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.AlarmActivity;
import com.dblgroup14.app.R;
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
    
    AlarmManager alarmMgr;
    Intent intent;
    PendingIntent pendingIntent;
    
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
            AsyncTask.execute(() -> {
                alarm = AppDatabase.db().alarmDao().get(id);
            });
        } else {
            ((AppCompatActivity) this).getSupportActionBar().setTitle("My New Alarm");
            alarm = new Alarm("My Alarm", Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true, 80, false
                    , 1);
        }
        
        setContentView(R.layout.activity_edit);

        TextView[] repeatDays = {findViewById(R.id.repeatDay0), findViewById(R.id.repeatDay1), findViewById(R.id.repeatDay2),
                findViewById(R.id.repeatDay3), findViewById(R.id.repeatDay4), findViewById(R.id.repeatDay5), findViewById(R.id.repeatDay6)};
    
        for (int i = 0; i<repeatDays.length;i++) {
            setBackground(repeatDays,i);
            setOnClick(repeatDays[i]);
        }
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
        
        ImageView repeatButton = findViewById(R.id.repeatButton);
        if(alarm.repeats){
            repeatButton.setBackgroundResource(R.drawable.ic_repeat);
        } else {
            repeatButton.setBackgroundResource(R.drawable.ic_repeat_one);
        }
        repeatButton.setOnClickListener(view -> {
           if(repeatButton.getBackground().getConstantState() == getResources().getDrawable(R.drawable.ic_repeat_one).getConstantState()){
               repeatButton.setBackgroundResource(R.drawable.ic_repeat);
               alarm.setRepeats(true);
           } else if(repeatButton.getBackground().getConstantState() == getResources().getDrawable(R.drawable.ic_repeat).getConstantState()){
               repeatButton.setBackgroundResource(R.drawable.ic_repeat_one);
               alarm.setRepeats(false);
           }
        });
        
        
    
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
    
        selectedChallenge.setOnClickListener(view -> CreateAlertDialogWithRadioButtonGroup());
        
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
    
    public void setBackground(TextView[] repeatDay, int j) {
        for(int i = 0; i < 7; i++){
            if(alarm.days[i]){
                repeatDay[i].setTextColor(Color.WHITE);
                repeatDay[i].setBackgroundResource(R.drawable.circle_used);
            } else {
                repeatDay[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                repeatDay[i].setBackgroundResource(R.drawable.circle_unused);
            }
        }
    }
    
    public void setOnClick(final TextView repeatDay) {
        repeatDay.setOnClickListener(view -> {
            if (repeatDay.getBackground().getConstantState() == getResources().getDrawable(R.drawable.circle_unused).getConstantState()) {
                repeatDay.setTextColor(Color.WHITE);
                repeatDay.setBackgroundResource(R.drawable.circle_used);
                
                switch(view.getId()){
                    case R.id.repeatDay0:
                        alarm.days[0] = true;
                        break;
                    case R.id.repeatDay1:
                        alarm.days[1] = true;
                        break;
                    case R.id.repeatDay2:
                        alarm.days[2] = true;
                        break;
                    case R.id.repeatDay3:
                        alarm.days[3] = true;
                        break;
                    case R.id.repeatDay4:
                        alarm.days[4] = true;
                        break;
                    case R.id.repeatDay5:
                        alarm.days[5] = true;
                        break;
                    case R.id.repeatDay6:
                        alarm.days[6] = true;
                        break;
                }
                
                
            } else if (repeatDay.getBackground().getConstantState() == getResources().getDrawable(R.drawable.circle_used).getConstantState()) {
                repeatDay.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                repeatDay.setBackgroundResource(R.drawable.circle_unused);
    
                switch(view.getId()){
                    case R.id.repeatDay0:
                        alarm.days[0] = false;
                        break;
                    case R.id.repeatDay1:
                        alarm.days[1] = false;
                        break;
                    case R.id.repeatDay2:
                        alarm.days[2] = false;
                        break;
                    case R.id.repeatDay3:
                        alarm.days[3] = false;
                        break;
                    case R.id.repeatDay4:
                        alarm.days[4] = false;
                        break;
                    case R.id.repeatDay5:
                        alarm.days[5] = false;
                        break;
                    case R.id.repeatDay6:
                        alarm.days[6] = false;
                        break;
                }

            }
        });
    }
    
    public void setAlarm(){
        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(getBaseContext(), AlarmActivity.class);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), alarm.id, intent, 0);
        
        if(alarm.enabled){
            if(alarm.repeats){
                //repeat
                Calendar cal = setAlarmTimeRepeat();
                assert alarmMgr != null;
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                //Toast.makeText(getApplicationContext(), ""+cal.getTime(), Toast.LENGTH_LONG).show();
            } else {
                //no repeat
                Calendar cal = setAlarmTimeNoRepeat();
                assert alarmMgr != null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                    }
                } else {
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                Toast.makeText(getApplicationContext(), "" + cal.getTime(), Toast.LENGTH_LONG).show();
            }
        } else {
            if (alarmMgr!= null) {
                alarmMgr.cancel(pendingIntent);
            }
        }
        
        
        /*if(alarm.repeats){
            Calendar cal = setAlarmTimeRepeat();
            if(alarm.enabled){
                assert alarmMgr != null;
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                Toast.makeText(getApplicationContext(), ""+cal.getTime(), Toast.LENGTH_LONG).show();
            } else {
                if (alarmMgr!= null) {
                    alarmMgr.cancel(pendingIntent);
                }
            }
        } else {
            Calendar cal = setAlarmTimeNoRepeat();
            if (alarm.enabled) {
                assert alarmMgr != null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                    }
                } else {
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                Toast.makeText(getApplicationContext(),
                        "" + cal.getTime(), Toast.LENGTH_LONG).show();
            } else {
                if (alarmMgr != null) {
                    alarmMgr.cancel(pendingIntent);
                }
            }
        }*/
    }
    
    private void createActionBarWithGradient() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
    }
    
    private Calendar setAlarmTimeRepeat() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, alarm.hours);
        cal.set(Calendar.MINUTE, alarm.minutes);
        cal.set(Calendar.SECOND, 0);
        setRepeatDay(cal);
        return cal;
    }
    
    public Calendar setAlarmTimeNoRepeat(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, alarm.hours);
        cal.set(Calendar.MINUTE, alarm.minutes);
        cal.set(Calendar.SECOND, 0);
        Calendar currentTime = Calendar.getInstance();
        if (cal.compareTo(currentTime) <= 0) {
            cal.add(Calendar.DATE, 1);
        } else {
            setNoRepeatDay(cal);
        }
        return cal;
    }
    
    public Calendar setNoRepeatDay(Calendar cal){
        for(int i = 0; i < alarm.days.length; i++){
            if(alarm.days[i]){
                setDayOfWeek(cal, i);
            }
        }
        return cal;
    }
    
    private Calendar setDayOfWeek(Calendar cal, int i) {
        switch (i){
            case 0:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 1:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 2:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 3:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 4:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 5:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 6:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    cal.add(Calendar.DATE, 1);
                }
                break;
        }
        return cal;
    }
    
    public void setRepeatDay(Calendar cal){
        for(int i = 0; i < alarm.days.length; i++){
            if(alarm.days[i]){
                setDayOfWeekRepeat(cal, i);
            }
        }
    }
    
    private void setDayOfWeekRepeat(Calendar cal, int i) {
        switch (i){
            case 0:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    cal.add(Calendar.DATE, 1);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                }
                break;
            case 1:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
                    cal.add(Calendar.DATE, 1);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                }
                break;
            case 2:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
                    cal.add(Calendar.DATE, 1);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                }
                break;
            case 3:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
                    cal.add(Calendar.DATE, 1);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                }
                break;
            case 4:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                    cal.add(Calendar.DATE, 1);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                }
                break;
            case 5:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    cal.add(Calendar.DATE, 1);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                }
                break;
            case 6:
                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    Toast.makeText(getApplicationContext(), "Zondag set", Toast.LENGTH_LONG).show();
                    cal.add(Calendar.DATE, 1);
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
                }
                break;
        }
    }
}
//ToDo: automatic next day alarm
//ToDo: repeat button
//ToDo: on lock screen alarm