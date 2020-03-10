package com.dblgroup14.app.ui.management;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dblgroup14.app.R;
import java.util.ArrayList;

//https://appsandbiscuits.com/listview-tutorial-android-12-ccef4ead27cc
public class CustomListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;
    
    //to store the list of alarms
    private final ArrayList<String> nameAlarms;
    
    //to store the list of countries
    private final ArrayList<String> timeAlarms;

    public CustomListAdapter(Activity context, ArrayList<String> nameAlarms, ArrayList<String> timeAlarms){
        
        super(context, R.layout.listview_row , nameAlarms);
        
        this.context = context;
        this.nameAlarms = nameAlarms;
        this.timeAlarms = timeAlarms;
        
    }
    
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null,true);
        
        //this code gets references to objects in the listview_row.xml file
        TextView alarmTimeTextView = (TextView) rowView.findViewById(R.id.alarmTimeTextView);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.nameTextView);
        
        //this code sets the values of the objects to values from the arrays
        alarmTimeTextView.setText(timeAlarms.get(position));
        nameTextView.setText(nameAlarms.get(position));
        
        return rowView;
        
    };
}