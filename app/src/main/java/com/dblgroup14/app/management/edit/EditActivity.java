package com.dblgroup14.app.management.edit;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.management.ManageAlarmsFragment;
import com.dblgroup14.support.entities.Alarm;
import java.util.Calendar;

//https://abhiandroid.com/ui/timepicker
public class EditActivity extends AppCompatActivity {
    // TODO: Determine whether to edit existing object or to create a new one
    
    TextView time;
    Alarm newAlarm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newAlarm = new Alarm();
        
        setContentView(R.layout.activity_edit);
        // initiate the edit text
        time = findViewById(R.id.time);
        setTimeView(newAlarm.getHours(), newAlarm.getMinutes());
        
        // perform click event listener on edit text
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTimeView(selectedHour, selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePicker.show();
            }
        });
        
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        nameEdit.setText(newAlarm.getName());
        newAlarm.setName(nameEdit.getText().toString());
        ManageAlarmsFragment b = new ManageAlarmsFragment();
        b.getAlarmsList().add(newAlarm);
    }
    
    public void setTimeView(int hours, int min) {
        if (min >= 0 && min < 10) {
            time.setText(hours + ":0" + min);
        } else {
            time.setText(hours + ":" + min);
        }
    }
}
//ToDo: repeat & days
//ToDo: automatic next day alarm