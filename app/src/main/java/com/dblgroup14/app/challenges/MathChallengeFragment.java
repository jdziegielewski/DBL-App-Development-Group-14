package com.dblgroup14.app.challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import com.dblgroup14.app.R;
import java.util.Random;
/*This is a math equation challenge with user giving the answer as an input
* The framework is the same as the rebus challenge*/
public class MathChallengeFragment extends ChallengeFragment {
    /* Built-in math question content */
    /*equation*/
    @IdRes
    private static final int[] IMAGE_RESOURCES = {
            R.drawable.math_ans_5,
            R.drawable.math_ans_05,
            R.drawable.math_ans_9,
            R.drawable.math_ans_9_,
            R.drawable.math_ans_65
    };
    /*equation solution*/
    private static final float[] CORRECT_ANSWERS = {
            5,
            0.05f,
            9,
            9,
            65
    };
    //additional variables
    private EditText answerInput;
    private int selectedImageIndex;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_math, container, false);
    }
    
    //layout created when fragment first opened
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get components
        answerInput = view.findViewById(R.id.answerInput);
        Button checkBtn = view.findViewById(R.id.checkBtn);
        ImageView mathImage = view.findViewById(R.id.mathImage);
        
        // Select random image
        selectedImageIndex = new Random().nextInt(IMAGE_RESOURCES.length);
        mathImage.setImageResource(IMAGE_RESOURCES[selectedImageIndex]);
        
        // Add check listener
        checkBtn.setOnClickListener(v -> checkAnswer());
    }
    // Checks if the answer is correct, if it is correct the challenge will be completed
    private void checkAnswer() {
        // Get the user answer
        float userAnswer;
        try {
            userAnswer = Float.parseFloat(answerInput.getText().toString());
        } catch (Exception e) {
            answerInput.setError("Invalid answer format!");
            return;
        }
        
        // Check answer
        if (userAnswer == CORRECT_ANSWERS[selectedImageIndex]) {
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
            completeChallenge();
        } else {
            answerInput.setError("Incorrect Result");
        }
    }
}