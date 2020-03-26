package com.dblgroup14.support;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dblgroup14.app.R;
import com.dblgroup14.support.entities.Challenge;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class ImageArrayAdapter extends ArrayAdapter<Integer> {
    
    
    
    public ImageArrayAdapter(Context context, List<Integer> challenges) {
        
        super(context, 0, challenges);
    }
    
    
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ImageView imageView;
        
        if (convertView == null) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(6, 6, 6, 6);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(getItem(position));
        //imageView.setImageResource(ChallengeToImageMapping.get(getItem(position).className));
        return imageView;
    }
}
    

