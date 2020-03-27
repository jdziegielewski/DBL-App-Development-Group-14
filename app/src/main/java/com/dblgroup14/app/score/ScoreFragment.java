package com.dblgroup14.app.score;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 For presenting the percentage of challenges completed by the user we have used github library com.github.PhilJay:MPAndroidChart:v3.0.3
 */

public class ScoreFragment extends Fragment {
    private ScoresListAdapter scoresListAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Create and set scores list adapter
        scoresListAdapter = new ScoresListAdapter(getActivity());
        ListView scoresList = view.findViewById(R.id.scores_list);
        scoresList.setAdapter(scoresListAdapter);
        
        // Get pie chart data
        SharedPreferences db = SimpleDatabase.getSharedPreferences();
        int totalChallenges = db.getInt(SimpleDatabase.TOTAL_CHALLENGES, 0);
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
        PieChart pieChart = view.findViewById(R.id.piechart);
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
        
        // Load user score database content
        LiveData<List<UserScore>> liveUserScores = AppDatabase.db().userScoreDao().all();
        liveUserScores.observe(getViewLifecycleOwner(), this::updateUserScores);
    }
    
    private void updateUserScores(final List<UserScore> data) {
        // Sort user scores
        Collections.sort(data, (u1, u2) -> -Integer.compare(u1.score, u2.score));
        
        // Update adapter
        scoresListAdapter.clear();
        scoresListAdapter.addAll(data);
        scoresListAdapter.notifyDataSetChanged();
    }
}
