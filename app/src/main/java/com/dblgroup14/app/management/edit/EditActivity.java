package com.dblgroup14.app.management.edit;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dblgroup14.app.R;
import com.dblgroup14.app.management.ManageAlarmsFragment;
import com.dblgroup14.support.entities.Alarm;
import java.lang.reflect.Array;
import java.util.Calendar;

//https://abhiandroid.com/ui/timepicker
public class EditActivity extends AppCompatActivity {
    // TODO: Determine whether to edit existing object or to create a new one
    
    TextView time;
    Alarm newAlarm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newAlarm = new Alarm("My Alarm", 7, 0, 80, false, 1);
        
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
//                getAlarmsList().add(newAlarm);
//                ListView alarmView = view.findViewById(R.id.alarmView);
//                alarmView.invalidate();
                finish();
            }
        });
        
        EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        nameEdit.setText(newAlarm.getName());
        newAlarm.setName(nameEdit.getText().toString());
    
        final ConstraintLayout repeatView = findViewById(R.id.repeatView);
        repeatView.setVisibility(View.GONE);
        ImageView repeat = findViewById(R.id.repeat);
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repeatView.isShown()){
                    repeatView.setVisibility(View.GONE);
                } else {
                    repeatView.setVisibility(View.VISIBLE);
                }
            }
        });
        
        final TextView repeatDay0 = findViewById(R.id.repeatDay0);
        final TextView repeatDay1 = findViewById(R.id.repeatDay1);
        final TextView repeatDay2 = findViewById(R.id.repeatDay2);
        final TextView repeatDay3 = findViewById(R.id.repeatDay3);
        final TextView repeatDay4 = findViewById(R.id.repeatDay4);
        final TextView repeatDay5 = findViewById(R.id.repeatDay5);
        final TextView repeatDay6 = findViewById(R.id.repeatDay6);
        TextView[] repeatDays = {repeatDay0, repeatDay1, repeatDay2, repeatDay3, repeatDay4, repeatDay5, repeatDay6};
        
        for(TextView rDays : repeatDays){
            setBackground(rDays);
        }
        
    }
    
    public void setTimeView(int hours, int min) {
        if (min >= 0 && min < 10) {
            time.setText(hours + ":0" + min);
        } else {
            time.setText(hours + ":" + min);
        }
    }
    
    public void setBackground(final TextView repeatDay){
        repeatDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatDay.getBackground().getConstantState()==getResources().getDrawable(R.drawable.circle_unused).getConstantState()){
                    repeatDay.setTextColor(Color.WHITE);
                    repeatDay.setBackgroundResource(R.drawable.circle_used);
                } else if(repeatDay.getBackground().getConstantState()==getResources().getDrawable(R.drawable.circle_used).getConstantState()){
                    repeatDay.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    repeatDay.setBackgroundResource(R.drawable.circle_unused);
                }
            
            }
        });
    }
    
}
//ToDo: repeat & days
//ToDo: automatic next day alarm