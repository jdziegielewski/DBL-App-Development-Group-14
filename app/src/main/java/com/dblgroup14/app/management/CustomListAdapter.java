package com.dblgroup14.app.management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dblgroup14.app.R;
import com.dblgroup14.app.management.edit.EditActivity;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.entities.Alarm;

//https://appsandbiscuits.com/listview-tutorial-android-12-ccef4ead27cc
public class CustomListAdapter extends ArrayAdapter<Alarm> {
    private final Activity activity;    // reference to the activity
    
    public CustomListAdapter(Activity activity) {
        super(activity.getApplicationContext(), R.layout.listview_row);
        this.activity = activity;
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null, true);
        
        //this code gets references to objects in the listview_row.xml file
        TextView alarmTimeTextView = rowView.findViewById(R.id.alarmTimeTextView);
        TextView nameTextView = rowView.findViewById(R.id.nameTextView);
        
        //this code sets the values of the objects to values from the arrays
        Alarm alarm = getItem(position);
        String hours = String.format("%02d", alarm.hours);
        String min = String.format("%02d", alarm.minutes);
        
        alarmTimeTextView.setText(String.format("%s:%s", hours, min));
        nameTextView.setText(alarm.name);
    
        ImageView deleteView = rowView.findViewById(R.id.deleteAlarmView);
        deleteView.setOnClickListener(view1 -> {
            AsyncTask.execute(() -> {
                AppDatabase.db().alarmDao().delete(alarm);
            });
        });
    
        ConstraintLayout dropDownView = rowView.findViewById(R.id.dropDownView);
        ConstraintLayout mainBoxAlarm = rowView.findViewById(R.id.mainBoxAlarm);
        dropDownView.setVisibility(View.GONE);
        mainBoxAlarm.setOnClickListener(view12 -> {
            if(dropDownView.getVisibility() == rowView.VISIBLE){
                dropDownView.setVisibility(View.GONE);
            } else if(dropDownView.getVisibility() == rowView.GONE){
                dropDownView.setVisibility(View.VISIBLE);
            }
        });
        
        ImageView editAlarmButton = rowView.findViewById(R.id.editAlarmButton);
        editAlarmButton.setOnClickListener(view13 -> {
            Intent intentEditAlarm = new Intent(activity, EditActivity.class);
            intentEditAlarm.putExtra("edit_alarm", true);
            intentEditAlarm.putExtra("alarm_id", alarm.getID());
            activity.startActivity(intentEditAlarm);
        });
        
        return rowView;
    }
   
}