package com.dblgroup14.app.challenges;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.dblgroup14.app.R;
import java.util.Random;
/* */
public class TriviaChallengeFragment extends ChallengeFragment {
    /* Questions, answers and correct answers */
    private static final String[] QUESTIONS = {
            "What is the capital of the USA?",
            "What is the longest river in the world?",
            "Who won the most grand slams in tennis?",
    };
    private static final String[][] ANSWERS = {
            {"New York", "Washington DC", "Seattle", "Los Angeles"},
            {"Nile", "Amazon River", "Yangtze", "Yellow River"},
            {"Rafael Nadal", "Novak Djokovic", "Roger Federer", "Pete Sampras"}
    };
    private static final Integer[] CORRECT_ANSWER_INDICES = {1, 0, 2};
    //additional variables
    private TextView questionTextView;
    private RadioGroup answerRadioGroup;
    private RadioButton[] answerRadioButtons;
    private int questionIndex;
    
    //layout created when fragment first opened
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_trivia, container, false);
    }
    
    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get components
        questionTextView = view.findViewById(R.id.Question);
        answerRadioGroup = view.findViewById(R.id.radioGroup);
        answerRadioButtons = new RadioButton[] {
                view.findViewById(R.id.radioButton1),
                view.findViewById(R.id.radioButton2),
                view.findViewById(R.id.radioButton3),
                view.findViewById(R.id.radioButton4)
        };
        
        // Select a random question
        selectRandomQuestion();
        
        // Add click listener to check answer button
        view.findViewById(R.id.check_ans_button).setOnClickListener(v -> {
            int selectedRadioButtonId = answerRadioGroup.getCheckedRadioButtonId();
            
            // Handle case when no answer is selected
            if (selectedRadioButtonId < 0) {
                Toast.makeText(getContext(), "Select an answer first!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Get selected answer index
            int selectedAnswerIndex = -1;
            for (int i = 0; i < answerRadioButtons.length; i++) {
                if (answerRadioButtons[i].getId() == selectedRadioButtonId) {
                    selectedAnswerIndex = i;
                    break;
                }
            }
            
            if (selectedAnswerIndex == CORRECT_ANSWER_INDICES[questionIndex]) {
                Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
                completeChallenge();
            } else {
                Toast.makeText(getContext(), "Incorrect!", Toast.LENGTH_SHORT).show();
                selectRandomQuestion();
            }
        });
    }
    //randomization of the questions to be presented to the user
    private void selectRandomQuestion() {
        // Select random question index
        questionIndex = new Random().nextInt(QUESTIONS.length);
        
        // Set question text
        questionTextView.setText(QUESTIONS[questionIndex]);
        
        // Set answers text
        for (int i = 0; i < answerRadioButtons.length; i++) {
            answerRadioButtons[i].setText(ANSWERS[questionIndex][i]);
        }
        
        // Clear selected radio button
        answerRadioGroup.clearCheck();
    }
}
