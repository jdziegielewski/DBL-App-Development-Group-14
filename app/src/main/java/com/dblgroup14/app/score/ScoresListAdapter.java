package com.dblgroup14.app.score;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dblgroup14.app.R;
import com.dblgroup14.support.entities.UserScore;

public class ScoresListAdapter extends ArrayAdapter<UserScore> {
    private final Activity activity;
    
    public ScoresListAdapter(Activity activity) {
        super(activity.getApplicationContext(), R.layout.score_rows);
        this.activity = activity;
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get UserScore object
        UserScore userScore = getItem(position);
        
        // Get inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        
        // Inflate new row view
        View rowView = inflater.inflate(R.layout.score_rows, null, true);
        
        // Set values
        TextView scoreNameTextView = rowView.findViewById(R.id.score_name);
        TextView scoreTextView = rowView.findViewById(R.id.score_score);
        scoreTextView.setText(String.valueOf(userScore.score));
        
        if (userScore.username.equals("(default)")) {    // handle own score case
            scoreNameTextView.setText("You");
        } else {
            scoreNameTextView.setText(userScore.username);
        }
        
        return rowView;
    }
}

