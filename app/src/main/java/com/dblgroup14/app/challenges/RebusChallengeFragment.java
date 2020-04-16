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
/*This is a rebus challenge with user giving the answer as an input
 * The framework is the same as the math challenge*/
public class RebusChallengeFragment extends ChallengeFragment {
    /* Built-in rebus content */
    /*rebus*/
    @IdRes
    private static final Integer[] IMAGE_RESOURCES = {
            R.drawable.rebus_mixed_emotions,
            R.drawable.rebus_balanced_diet,
            R.drawable.rebus_bucket_list,
            R.drawable.rebus_fade_away,
            R.drawable.rebus_feel_free,
            R.drawable.rebus_square_root,
            R.drawable.rebus_stakes_are_high,
            R.drawable.rebus_stick_around,
            R.drawable.rebus_time_is_money,
    };
    /*rebus solution*/
    private static final String[] CORRECT_ANSWERS = {
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
    //additional variables
    private EditText answerInput;
    private int selectedImageIndex;
    //layout created when fragment first opened
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_rebus, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get components
        answerInput = view.findViewById(R.id.answerInput);
        Button checkBtn = view.findViewById(R.id.checkBtn);
        ImageView rebusImage = view.findViewById(R.id.rebusImage);
        
        // Set random rebus image
        selectedImageIndex = new Random().nextInt(IMAGE_RESOURCES.length);
        rebusImage.setImageResource(IMAGE_RESOURCES[selectedImageIndex]);
        
        // Add check button listener
        checkBtn.setOnClickListener(v -> checkAnswer());
    }
    // Checks if the answer is correct, if it is correct the challenge will be completed
    private void checkAnswer() {
        // Get the user answer
        String userAnswer = answerInput.getText().toString().trim().toLowerCase();
        // Check answer
        if (userAnswer.equals(CORRECT_ANSWERS[selectedImageIndex])) {
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
            completeChallenge();
        } else {
            answerInput.setError("Incorrect answer!");
        }
    }
}
