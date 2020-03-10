package com.dblgroup14.app.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.R;
import com.dblgroup14.app.management.edit.EditActivity;
import com.dblgroup14.app.ui.management.CustomListAdapter;
import com.dblgroup14.support.entities.Alarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ManageAlarmsFragment extends Fragment {
    
    ListView listView;
    List<Alarm> alarms = new ArrayList<>();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_alarms, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Alarms");
        
        Alarm alarm1 = new Alarm("Morning", 7, 50, 100, false, 1);
        Alarm alarm2 = new Alarm("Noon", 12, 30, 80, false, 4);
        Alarm alarm3 = new Alarm("Late", 21, 15, 100, true, 1);
        alarms.add(alarm1);
        alarms.add(alarm2);
        alarms.add(alarm3);
        
        
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNewAlarm = new Intent(getActivity(), EditActivity.class);
                startActivity(intentNewAlarm);
            }
        });
        
        ArrayList<String> nameAlarms = new ArrayList<String>();
        ArrayList<String> timeAlarms = new ArrayList<String>();
        
        for (Alarm alarm : alarms) {
            nameAlarms.add(alarm.getName());
            timeAlarms.add(alarm.getHours() + ":" + alarm.getMinutes());
        }
        
        
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), nameAlarms, timeAlarms);
        
        listView = (ListView) view.findViewById(R.id.alarmView);
        listView.setAdapter(adapter);
        
        /*
        // Get components
        ListView alarmView = view.findViewById(R.id.alarmView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        for (Alarm alarm  : alarms) {
            adapter.add(alarm.getName());
        }
        
        alarmView.setAdapter(adapter); */
        
    }
    
    public List<Alarm> getAlarmsList() {
        return alarms;
    }
    
}