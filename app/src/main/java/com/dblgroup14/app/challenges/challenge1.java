package com.dblgroup14.app.challenges;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.dblgroup14.app.MainActivity;
import com.dblgroup14.app.R;

public class challenge1 extends AppCompatActivity {
    Button checkbutton;
    String password;
    String correct = "pass";
    EditText textinput;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge1);
        textinput = (EditText) findViewById(R.id.textinput1);
        checkbutton = (Button) findViewById(R.id.checkbutton);
        checkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = textinput.getText().toString();
                if(password.equals(correct)) {
                    Toast.makeText(challenge1.this, "Correct", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else textinput.setError("Incorrect Password");
            }
        });
    }
}
