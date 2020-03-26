package com.dblgroup14.app.management;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.Add_Challenge;
import com.dblgroup14.app.AlarmActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.challenges.challenge1;
import com.dblgroup14.support.ImageAdapter;

public class ManageChallengesFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
       
        
        return inflater.inflate(R.layout.fragment_manage_challenges, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    
        GridView gv = (GridView) getActivity().findViewById(R.id.gv);
    
        final ImageAdapter gridViewArrayAdapter = new ImageAdapter(getActivity());
        gv.setAdapter(gridViewArrayAdapter);

    }
}
