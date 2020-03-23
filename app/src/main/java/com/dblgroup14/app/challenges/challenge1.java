package com.dblgroup14.app.challenges;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Random;

import static com.dblgroup14.app.R.*;
import static com.dblgroup14.app.R.drawable.*;

public class challenge1 extends AppCompatActivity {
    ImageView imageView;
    Button checkbutton;
    String password;
    EditText textinput;
    Random r;
    int number;
    //array of pictures of rebuses
    Integer[] images = {
            rebus_mixed_emotions,
            rebus_balanced_diet,
            rebus_bucket_list,
            rebus_fade_away,
            rebus_feel_free,
            rebus_square_root,
            rebus_stakes_are_high,
            rebus_stick_around,
            rebus_time_is_money,
        };
    //array of passwords to rebuses
    //needs to go with images[]
    String[] correct = {
            "mixed emotions",
            "balanced diet",
            "bucket list",
            "fade away",
            "feel free",
            "square root",
            "stakes are high",
            "stick around",
            "time is money",
    };

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
