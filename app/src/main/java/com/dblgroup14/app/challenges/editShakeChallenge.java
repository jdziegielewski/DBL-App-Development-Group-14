package com.dblgroup14.app.challenges;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.R;

public class editShakeChallenge extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_shake_challenge);
    
        ((AppCompatActivity) this).getSupportActionBar().setTitle("Edit shake challenge");
        EditText ShakeNumberInput;
        ShakeNumberInput = findViewById(R.id.UserShakesInput);
        ShakeNumberInput.getText().toString();
        
        Button edit_shake_challenge_button = findViewById(R.id.edit_shake_save_button);
        
        edit_shake_challenge_button.setOnClickListener(view -> {
            if (TextUtils.isEmpty(ShakeNumberInput.getText().toString())){
                Toast.makeText(editShakeChallenge.this, "Empty field not allowed!", Toast.LENGTH_SHORT).show();
            }else {
                
                //SharedPreferences.Editor editor = getSharedPreferences("PreferencesName", MODE_PRIVATE).edit();
                //editor.putInt("myInt", EditText);
                //editor.apply();
                
                Intent EndEditShakeIntent = new Intent(getApplicationContext(), editChallenges.class);
                startActivity(EndEditShakeIntent);
            }
        });
    }
}
