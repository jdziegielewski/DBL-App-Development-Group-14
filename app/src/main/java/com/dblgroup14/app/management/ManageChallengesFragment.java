package com.dblgroup14.app.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.AlarmActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.challenges.BarcodeScanChallenge;
import com.dblgroup14.app.challenges.challenge1;
import com.dblgroup14.app.challenges.editChallenges;
import com.dblgroup14.app.challenges.shakeChallenge;

public class ManageChallengesFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_challenges, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        view.findViewById(R.id.goToChallengeBtn).setOnClickListener(v -> {
            Intent challengesIntent = new Intent(getContext(), challenge1.class);
            startActivity(challengesIntent);
        });
        
        view.findViewById(R.id.goToShakeITChallenge).setOnClickListener(v -> {
            Intent challengesIntent = new Intent(getContext(), shakeChallenge.class);
            startActivity(challengesIntent);
        });
        
        view.findViewById(R.id.goToBarcodeScanChallenge).setOnClickListener(v -> {
            Intent challengesIntent = new Intent(getContext(), BarcodeScanChallenge.class);
            startActivity(challengesIntent);
        });
        
        view.findViewById(R.id.tochallengebutton).setOnClickListener(v -> {
            Intent myIntent = new Intent(getActivity().getBaseContext(), AlarmActivity.class);
            startActivity(myIntent);
        });
    
        view.findViewById(R.id.edit_challenges_button).setOnClickListener(v -> {
            Intent editChallengesIntent = new Intent(getContext(), editChallenges.class);
            startActivity(editChallengesIntent);
        });
    }
}
