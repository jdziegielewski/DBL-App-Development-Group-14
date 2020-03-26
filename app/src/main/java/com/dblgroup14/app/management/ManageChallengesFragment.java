package com.dblgroup14.app.management;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.Add_Challenge;
import com.dblgroup14.app.AlarmActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.support.ImageAdapter;
import com.dblgroup14.support.ImageArrayAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageChallengesFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
       
        
        return inflater.inflate(R.layout.fragment_manage_challenges, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    
        GridView gv = (GridView) getActivity().findViewById(R.id.gv);
        List<Integer> images = new ArrayList<Integer>(Arrays.asList(ImageAdapter.mThumbIds));
        final ImageArrayAdapter gridViewArrayAdapter = new ImageArrayAdapter(getActivity(),images);
        gv.setAdapter(gridViewArrayAdapter);
        
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Go to Create Custom Challenge Fragment
            
                        break;
                    case 1:
                        // Go to Friends Challenges Fragment
                        break;
                    case 2:
                        // Go to Default Challenges Fragment
                        break;
                    default:
            
                        Toast.makeText(getActivity().getApplicationContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
                        break;
        
                }
                
            }
        });
        Button btn = getActivity().findViewById(R.id.edit_challenge_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
                images.add(images.size(),R.drawable.ic_shake_challenge);

                gridViewArrayAdapter.notifyDataSetChanged();


            }
        });
    }
}
