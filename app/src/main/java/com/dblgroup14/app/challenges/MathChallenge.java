package com.dblgroup14.app.challenges;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.dblgroup14.app.R;
import java.util.Random;

public class MathChallenge extends AppCompatActivity {
    ImageView imageView1;
    Button checkbutton1;
    EditText textinput1;
    String result;
    Random r;
    int number;
    Integer[] mathImages = {
        R.drawable.math_ans_5,
        R.drawable.math_ans_05,
        R.drawable.math_ans_9,
        R.drawable.math_ans_9_,
        R.drawable.math_ans_65
    };
    String[] correct_result = {
        "5",
        "0.05",
        "9",
        "9",
        "65"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_challenge);
        textinput1 = (EditText) findViewById(R.id.textinput1);
        checkbutton1 = (Button) findViewById(R.id.checkbutton);
        imageView1 = (ImageView) findViewById(R.id.mathImage);
        r = new Random();
        number = r.nextInt(mathImages.length);
        imageView1.setImageResource(mathImages[number]);
        checkbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = textinput1.getText().toString();
                if(result.equals(correct_result[number])) {
                    Toast.makeText(MathChallenge.this, "Correct", Toast.LENGTH_SHORT).show();
                    finish(); //finishes
                }
                else textinput1.setError("Incorrect Result");
            }
        });
    }
}