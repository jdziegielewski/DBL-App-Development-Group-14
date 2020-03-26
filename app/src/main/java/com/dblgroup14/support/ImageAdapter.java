package com.dblgroup14.support;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.dblgroup14.app.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    
    public ImageAdapter(Context c) {
        mContext = c;
    }
    
    public int getCount() {
        return mThumbIds.length;
    }
    
    public Object getItem(int position) {
        return null;
    }
    
    public long getItemId(int position) {
        return 0;
    }
    
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
    
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_add_circle_dp_black, R.drawable.math_equation_logo,
           
    };
}
