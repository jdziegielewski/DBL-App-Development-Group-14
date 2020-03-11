package com.dblgroup14.app.management;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dblgroup14.app.R;
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
        
        return rowView;
    }
   
}