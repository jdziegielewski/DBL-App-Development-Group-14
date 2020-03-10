package com.dblgroup14.app.score;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dblgroup14.app.R;
import java.util.ArrayList;

public class ScoreListFriends extends ArrayAdapter {
   
    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> score;

    public ScoreListFriends(Activity context, ArrayList<String> name, ArrayList<String> score){
        super(context, R.layout.score_rows, name);
        this.context = context;
        this.name = name;
        this.score = score;
    }
    
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.score_rows, null,true);
        TextView ScoreNameTextView = (TextView) rowView.findViewById(R.id.score_name);
        TextView ScoreTextView = (TextView) rowView.findViewById(R.id.score_score);
        ScoreTextView.setText(score.get(position));
        ScoreNameTextView.setText(name.get(position));
        return rowView;
    }
}

