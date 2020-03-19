package com.dblgroup14.app.challenges;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.dblgroup14.app.MainActivity;
import com.dblgroup14.app.R;
import java.util.Random;

import static com.dblgroup14.app.R.*;
import static com.dblgroup14.app.R.drawable.*;

public class challenge1 extends AppCompatActivity {
    ImageView imageView;
    Button checkbutton;
    String password;
    EditText textinput;
    Random r;
    Integer[] images = {
        misunderstood,
        mixed_emotions,
        balanced_diet,
        bucket_list,
        fade_away,
        feel_free,
        square_root,
        stakes_are_high,
        stick_around,
        time_is_money,
        too_big_to_fail
        };
    String[] correct = {
            "misunderstood",
            "mixed emotions",
            "balanced diet",
            "bucket list",
            "fade away",
            "feel free",
            "square root",
            "stakes are high",
            "stick around",
            "time is money",
            "too big to fail"
    };
    int number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        r = new Random();
        number = r.nextInt(images.length);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_challenge1);
        textinput = (EditText) findViewById(id.textinput1);
        checkbutton = (Button) findViewById(id.checkbutton);
        imageView = (ImageView) findViewById(id.rebusImage);
        imageView.setImageResource(images[number]);
        checkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = textinput.getText().toString();
                if(password.equals(correct[number])) {
                    Toast.makeText(challenge1.this, "Correct", Toast.LENGTH_SHORT).show();
                    finish(); //finishes
                }
                else textinput.setError("Incorrect Password");
            }
        });
    }
}
