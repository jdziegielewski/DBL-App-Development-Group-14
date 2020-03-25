package com.dblgroup14.app.challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Random;

import static com.dblgroup14.app.R.drawable.rebus_balanced_diet;
import static com.dblgroup14.app.R.drawable.rebus_bucket_list;
import static com.dblgroup14.app.R.drawable.rebus_fade_away;
import static com.dblgroup14.app.R.drawable.rebus_feel_free;
import static com.dblgroup14.app.R.drawable.rebus_mixed_emotions;
import static com.dblgroup14.app.R.drawable.rebus_square_root;
import static com.dblgroup14.app.R.drawable.rebus_stakes_are_high;
import static com.dblgroup14.app.R.drawable.rebus_stick_around;
import static com.dblgroup14.app.R.drawable.rebus_time_is_money;
import static com.dblgroup14.app.R.id;
import static com.dblgroup14.app.R.layout;

public class RebusChallengeFragment extends ChallengeFragment {
    /* Built-in rebus content */
    private static final Integer[] REBUS_IMAGES = {
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
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layout.fragment_challenge_rebus, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get components
        final EditText answerInput = view.findViewById(id.answerInput);
        Button checkBtn = view.findViewById(id.checkBtn);
        ImageView rebusImage = view.findViewById(id.rebusImage);
        
        // Set random rebus image
        final int i = new Random().nextInt(REBUS_IMAGES.length);
        rebusImage.setImageResource(REBUS_IMAGES[i]);
        
        // Add check button listener
        checkBtn.setOnClickListener(v -> {
            String userAnswer = answerInput.getText().toString().trim().toLowerCase();
            if (userAnswer.equals(CORRECT_ANSWERS[i])) {
                Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
                completeChallenge();
            } else {
                answerInput.setError("Incorrect answer!");
            }
        });
    }
}
