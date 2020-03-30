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
import com.dblgroup14.app.R;
import java.util.Random;

public class MathChallengeFragment extends ChallengeFragment {
    /* Built-in math question content */
    @IdRes
    private static final int[] IMAGE_RESOURCES = {
            R.drawable.math_ans_5,
            R.drawable.math_ans_05,
            R.drawable.math_ans_9,
            R.drawable.math_ans_9_,
            R.drawable.math_ans_65
    };
    private static final float[] CORRECT_ANSWERS = {
            5,
            0.05f,
            9,
            9,
            65
    };
    
    private EditText answerInput;
    private int selectedImageIndex;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_math, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
    
    private void checkAnswer() {
        // Get user answer
        float userAnswer = 0;
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