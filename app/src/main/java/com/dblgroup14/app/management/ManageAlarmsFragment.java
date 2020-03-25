package com.dblgroup14.app.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.EditActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.entities.Alarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ManageAlarmsFragment extends Fragment {
    private ListView listView;
    private CustomListAdapter alarmsListAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_alarms, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Alarms");
        
        // Create list adapter
        alarmsListAdapter = new CustomListAdapter(getActivity());
        listView = view.findViewById(R.id.alarmView);
        listView.setAdapter(alarmsListAdapter);
        
        // Register live data binding with database
        LiveData<List<Alarm>> liveAlarms = AppDatabase.db().alarmDao().all();
        liveAlarms.observe(getViewLifecycleOwner(), l -> {
            alarmsListAdapter.clear();
            alarmsListAdapter.addAll(l);
            alarmsListAdapter.notifyDataSetChanged();
        });
        
        // Create add alarm button
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent editAlarmIntent = new Intent(getContext(), EditActivity.class);
            editAlarmIntent.putExtra("object", "alarm");
            startActivity(editAlarmIntent);
        });
    }
}