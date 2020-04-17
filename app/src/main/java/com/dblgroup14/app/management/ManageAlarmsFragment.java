package com.dblgroup14.app.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.EditActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.database_support.AppDatabase;
import com.dblgroup14.database_support.entities.local.Alarm;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ManageAlarmsFragment extends Fragment {
    private AlarmListAdapter alarmListAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_alarms, container, false);
    }
    
    /**
     * Sets the title, the list adapter and floating action button
     *
     * @param view               the view
     * @param savedInstanceState the bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();
        createListAdapter(view);
        setListAdapter();
        setAddAlarmButton(view);
    }
    
    /**
     * Sets the title in the action bar.
     */
    private void setTitle() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Alarms");
    }
    
    /**
     * Creates the list adapter
     *
     * @param view the view
     */
    private void createListAdapter(View view) {
        alarmListAdapter = new AlarmListAdapter(getActivity());
        ListView listView = view.findViewById(R.id.alarmView);
        listView.setAdapter(alarmListAdapter);
    }
    
    /**
     * Sets the floating action button for creating a new alarm in the edit activity
     *
     * @param view the view
     */
    private void setAddAlarmButton(View view) {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent editAlarmIntent = new Intent(getContext(), EditActivity.class);
            editAlarmIntent.putExtra(EditActivity.KEY_OBJECT_TYPE, EditActivity.VAL_OBJECT_ALARM);
            startActivity(editAlarmIntent);
        });
    }
    
    /**
     * Registers the live data binding with database
     */
    private void setListAdapter() {
        LiveData<List<Alarm>> liveAlarms = AppDatabase.db().alarmDao().all();
        liveAlarms.observe(getViewLifecycleOwner(), l -> {
            alarmListAdapter.clear();
            alarmListAdapter.addAll(l);
            alarmListAdapter.notifyDataSetChanged();
        });
    }
}