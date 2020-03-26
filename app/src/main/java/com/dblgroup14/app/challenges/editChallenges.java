package com.dblgroup14.app.challenges;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.R;

public class editChallenges extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list_default_challenges);
    
        ((AppCompatActivity) this).getSupportActionBar().setTitle("Edit Challenges");
        
        Button edit_shake_challenge_button = findViewById(R.id.edit_shake_challenge_button);
        edit_shake_challenge_button.setOnClickListener(view -> {
//            Intent EditShakeIntent = new Intent(getApplicationContext(), editShakeChallenge.class);
//            startActivity(EditShakeIntent);
        });
    }
}
