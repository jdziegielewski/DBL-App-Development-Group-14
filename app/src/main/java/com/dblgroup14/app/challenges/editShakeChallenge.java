package com.dblgroup14.app.challenges;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.R;

public class editShakeChallenge extends AppCompatActivity {
    
    private NumberPicker picker1;
    private String pickerVals;
    public int valuePicker1;
    SharedPreferences preferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_shake_challenge);
    
        ((AppCompatActivity) this).getSupportActionBar().setTitle("Edit shake challenge");
        EditText ShakeNumberInput;
        Button edit_shake_challenge_button = findViewById(R.id.edit_shake_save_button);
    
        NumberPicker picker1 = findViewById(R.id.number_picker);
        picker1.setMaxValue(19);
        picker1.setMinValue(0);
        String[] pickerVals = new String[]{"5", "10", "15", "20", "25", "30", "35","40","45","50","55","60","65","70","75","80","85","90","95","100"};
        picker1.setDisplayedValues(pickerVals);
    
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        picker1.setValue(preferences.getInt("shakes", 5));
        picker1.setWrapSelectorWheel(false);
        
        picker1.setOnValueChangedListener((numberPicker, i, i1) -> {
            valuePicker1 = picker1.getValue();
        });
        
        edit_shake_challenge_button.setOnClickListener(view -> {
                Intent EndEditShakeIntent = new Intent(getApplicationContext(), editChallenges.class);
                
                preferences.edit().putInt("shakes", valuePicker1).apply();
                preferences.getInt("shakes", 5);
                picker1.setValue(preferences.getInt("shakes", 5));
    
                startActivity(EndEditShakeIntent);
        });
    }
}
