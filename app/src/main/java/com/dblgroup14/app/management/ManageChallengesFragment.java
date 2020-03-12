package com.dblgroup14.app.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.PlayChallengePage;
import com.dblgroup14.app.R;
import com.dblgroup14.app.challenges.challenge1;
import com.dblgroup14.app.challenges.shakechallenge;

public class ManageChallengesFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_challenges, container, false);
    }
    
    private Button toChallengePageButton;
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        view.findViewById(R.id.goToChallengeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent challengesIntent = new Intent(getContext(), challenge1.class);
                startActivity(challengesIntent);
            }
            
        });
        toChallengePageButton = (Button) getActivity().findViewById(R.id.tochallengebutton);
       
        toChallengePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity().getBaseContext(), PlayChallengePage.class);
                startActivity(myIntent);
            }
        });
    }
}
