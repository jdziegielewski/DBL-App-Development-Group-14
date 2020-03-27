package com.dblgroup14.app.score;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.entities.UserScore;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        
        //// PieChart ////
        PieChart pieChart = view.findViewById(R.id.piechart);
        // Data
        List<PieEntry> value = new ArrayList<>();
        value.add(new PieEntry(70f, "Completed"));
        value.add(new PieEntry(30f, "Not completed"));
        PieDataSet pieDataSet = new PieDataSet(value, " ");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        // Styling
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);
        Legend l = pieChart.getLegend();
        l.setEnabled(false);
        pieData.setValueTextSize(16f);
        pieData.setValueFormatter(new PercentFormatter());
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        pieChart.animateXY(1400, 1400);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(14f);
        
        // Load database content
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
