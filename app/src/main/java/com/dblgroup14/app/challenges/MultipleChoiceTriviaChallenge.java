package com.dblgroup14.app.challenges;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dblgroup14.app.R;
import java.util.Random;

public class MultipleChoiceTriviaChallenge extends AppCompatActivity {
    private TextView textViewQuestion;
    private RadioGroup radioGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button checkAns;
    Random r;
    String[] questions = {
            "What is the capitol of USA?",
            "What is the longest river in the world?",
            "Who won the most grand slams in tennis?",
    };
    String[] answer1 = {
            "New York",
            "Nile",
            "Rafael Nadal"
    };
    String[] answer2 = {
            "Washington DC",
            "Amazon River",
            "Novak Djokovic"
    };
    String[] answer3 = {
            "Seattle",
            "Yangtze",
            "Roger Federer"
    };
    String[] answer4 = {
            "Los Angeles",
            "Yellow River",
            "Pete Sampras"
    };
    Integer[] correctans = {
            2,
            1,
            3,
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_trivia_challenge);
        textViewQuestion = findViewById(R.id.Question);
        radioGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);
        checkAns = findViewById(R.id.check_ans_button);
        radioGroup.clearCheck();

        r = new Random();
        int number = r.nextInt(questions.length);
        textViewQuestion.setText(questions[number]);
        rb1.setText(answer1[number]);
        rb2.setText(answer2[number]);
        rb3.setText(answer3[number]);
        rb4.setText(answer4[number]);
    
        checkAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                    RadioButton selected = findViewById(radioGroup.getCheckedRadioButtonId());
                    int ansNr = radioGroup.indexOfChild(selected) + 1;
                    if(ansNr == correctans[number]) {
                        Toast.makeText(MultipleChoiceTriviaChallenge.this, "Correct", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(MultipleChoiceTriviaChallenge.this, "Incorrect, Try Again", Toast.LENGTH_SHORT).show();
                        radioGroup.clearCheck();
                    }
                } else {
                    Toast.makeText(MultipleChoiceTriviaChallenge.this, "Select an answer first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        
    }
    
}
