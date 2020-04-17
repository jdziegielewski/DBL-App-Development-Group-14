package com.dblgroup14.app.score;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.R;
import com.dblgroup14.database_support.AppDatabase;
import com.dblgroup14.database_support.RemoteDatabase;
import com.dblgroup14.SimpleDatabase;
import com.dblgroup14.database_support.entities.local.UserScore;
import com.dblgroup14.database_support.entities.remote.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * For presenting the percentage of challenges completed by the user we have used github library com.github.PhilJay:MPAndroidChart.
 */
public class ScoreFragment extends Fragment {
    private LinearLayout scoresListContainer;
    private PieChart pieChart;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score, container, false);
    }
    
    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get components
        scoresListContainer = view.findViewById(R.id.scoresListContainer);
        pieChart = view.findViewById(R.id.scoresPieChart);
        TextView scoreTime = view.findViewById(R.id.time_completed);
        
        // Load data and initialize pie chart
        loadPieChartData();
        
        // Set time of last challenge completion
        long lastCompletionMillis = SimpleDatabase.getSharedPreferences().getLong(SimpleDatabase.LAST_COMPLETED_TIME, 0);
        String timeOfLastCompletion = lastCompletionMillis == 0 ? "-" : new SimpleDateFormat("MMM dd - HH:mm").format(new Date(lastCompletionMillis));
        scoreTime.setText(String.format("Last challenge completed: %s", timeOfLastCompletion));
        
        // Load user score database content
        LiveData<List<UserScore>> liveUserScores = AppDatabase.db().userScoreDao().all();
        liveUserScores.observe(getViewLifecycleOwner(), this::updateUserScores);
    }
    
    /**
     * Fetches the user's challenge completed challenges data from the SimpleDatabase and initializes the pie chart.
     */
    private void loadPieChartData() {
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
    }
    
    /**
     * Sets the lay-out of the pie chart and fills it with the given data.
     *
     * @param data The data to fill the pie chart with
     */
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
    
    /**
     * Called whenever the user scores in the local database change.
     *
     * @param data The newly fetched list of user scores
     */
    private void updateUserScores(final List<UserScore> data) {
        // Sort user scores
        Collections.sort(data, (u1, u2) -> -Long.compare(u1.score, u2.score));
        
        // Grab current user data
        DatabaseReference userRef = RemoteDatabase.getCurrentUserReference();
        if (userRef != null) {
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    fillScoreList(data, currentUser.username);
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        } else {
            getActivity().runOnUiThread(() -> fillScoreList(data, null));
        }
    }
    
    /**
     * Fills the scores list with newly fetched scores.
     *
     * @param data            The fetched list of user scores
     * @param currentUsername The username of the currently logged in user, or null if no user is logged in
     */
    private void fillScoreList(List<UserScore> data, String currentUsername) {
        // Clear all child views
        scoresListContainer.removeAllViews();
        
        // Spawn new views
        int position = 0;
        for (UserScore score : data) {
            View newRowView = null;
            if (currentUsername == null) {
                newRowView = createScoreView(position, score, "(default)");
            } else if (!score.username.equals("(default)")) {
                newRowView = createScoreView(position, score, currentUsername);
            }
            
            if (newRowView != null) {
                scoresListContainer.addView(newRowView);
                position++;
            }
        }
    }
    
    /**
     * Creates a view for a row to put in the list of scores.
     *
     * @param position        The position of the row in the scores list
     * @param userScore       The UserScore object that is to be represented in this row
     * @param currentUsername The username of the currently logged in user, or '(default)' if no user is logged in
     * @return A new row view
     */
    private View createScoreView(int position, UserScore userScore, String currentUsername) {
        // Inflate new row view
        View rowView = getLayoutInflater().inflate(R.layout.score_row, scoresListContainer, false);
        
        // Set values
        TextView scoreNameTextView = rowView.findViewById(R.id.score_name);
        TextView scoreTextView = rowView.findViewById(R.id.score_score);
        scoreTextView.setText(String.valueOf(userScore.score));
        
        // Get formatted name of user
        String username;
        if (userScore.username.equals(currentUsername)) {    // handle own score case
            username = "You";
        } else {
            username = userScore.username;
        }
        
        // Set user name
        scoreNameTextView.setText(String.format("%d. %s", position + 1, username));
        
        return rowView;
    }
}
