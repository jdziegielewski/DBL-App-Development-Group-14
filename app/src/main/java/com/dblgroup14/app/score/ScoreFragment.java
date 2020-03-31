package com.dblgroup14.app.score;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.SimpleDatabase;
import com.dblgroup14.support.entities.UserScore;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * For presenting the percentage of challenges completed by the user we have used github library com.github.PhilJay:MPAndroidChart:v3.0.3
 */
public class ScoreFragment extends Fragment {
    private LinearLayout scoresListContainer;
    private PieChart pieChart;
    private TextView scoreTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    
        // Set last completed time
        scoreTime = view.findViewById(R.id.time_completed);
        String currentTime = new SimpleDateFormat("MMM dd - HH:mm").format(new Date());
        String scoreTimeText = "Last completed:  " + currentTime;
        scoreTime.setText(scoreTimeText);
        
        // Get components
        scoresListContainer = view.findViewById(R.id.scoresListContainer);
        pieChart = view.findViewById(R.id.scoresPieChart);
        
        // Check if the user has done any challenges
        SharedPreferences db = SimpleDatabase.getSharedPreferences();
        int totalChallenges = db.getInt(SimpleDatabase.TOTAL_CHALLENGES, 0);
        
        if (totalChallenges > 0) {
            // Get pie chart data
            int completedChallenges = db.getInt(SimpleDatabase.COMPLETED_CHALLENGES, 0);
            int notCompletedChallenges = totalChallenges - completedChallenges;
            List<PieEntry> data = new ArrayList<>();
            if (completedChallenges > 0) {
                data.add(new PieEntry(completedChallenges, "Completed"));
            }
            if (notCompletedChallenges > 0) {
                data.add(new PieEntry(notCompletedChallenges, "Not completed"));
            }
            
            // Initialize pie chart
            initializePieChart(data);
        } else {
            // Set pie chart height to wrap content, if it does not contain any data
            pieChart.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            pieChart.requestLayout();
        }
        
        // Load user score database content
        LiveData<List<UserScore>> liveUserScores = AppDatabase.db().userScoreDao().all();
        liveUserScores.observe(getViewLifecycleOwner(), this::updateUserScores);
    }
    
    private void initializePieChart(final List<PieEntry> data) {
        // Initialize pie chart
        PieDataSet pieDataSet = new PieDataSet(data, " ");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        
        // Style pie chart
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateXY(1400, 1400);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(14f);
        pieData.setValueTextSize(16f);
        pieData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> String.format("%.0f%% (%d)", value, (int) entry.getY()));
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
    }
    
    private void updateUserScores(final List<UserScore> data) {
        // Sort user scores
        Collections.sort(data, (u1, u2) -> -Integer.compare(u1.score, u2.score));
        
        // Update container
        getActivity().runOnUiThread(() -> {
            // Clear all child views
            scoresListContainer.removeAllViews();
            
            // Spawn new views
            for (int i = 0; i < data.size(); i++) {
                scoresListContainer.addView(createScoreView(i, data.get(i)));
            }
        });
    }
    
    private View createScoreView(int index, UserScore userScore) {
        // Inflate new row view
        View rowView = getLayoutInflater().inflate(R.layout.score_row, scoresListContainer, false);
        
        // Set values
        TextView scoreNameTextView = rowView.findViewById(R.id.score_name);
        TextView scoreTextView = rowView.findViewById(R.id.score_score);
        scoreTextView.setText(String.valueOf(userScore.score));
        
        // Get formatted name of user
        String username;
        if (userScore.username.equals("(default)")) {    // handle own score case
            username = "You";
        } else {
            username = userScore.username;
        }
        
        // Set user name
        scoreNameTextView.setText(String.format("%d. %s", index + 1, username));
        
        return rowView;
    }
}
