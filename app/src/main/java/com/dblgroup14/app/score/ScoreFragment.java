package com.dblgroup14.app.score;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

public class ScoreFragment extends Fragment {
    
    ListView listView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Create lists of names and scores
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> scores = new ArrayList<String>();
        names.add("1. Mike"); names.add("2. YOU"); names.add("3. Dad"); names.add("4. Sophie"); names.add("5. Jess"); names.add("6. Danny");
        scores.add("100 xp"); scores.add("80 xp"); scores.add("20 xp"); scores.add("10 xp"); scores.add("7 xp"); scores.add("1 xp");
    
        // Display list of names and scores
        ScoreListFriends adapter = new ScoreListFriends(getActivity(), names, scores);
        listView = (ListView) view.findViewById(R.id.scores_list);
        listView.setAdapter(adapter);
        
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
    }
}
