package com.dblgroup14.app.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.dblgroup14.app.AlarmActivity;
import com.dblgroup14.app.EditActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.challenges.BarcodeChallengeFragment;
import com.dblgroup14.app.challenges.ChallengeFragment;
import com.dblgroup14.app.challenges.MathChallengeFragment;
import com.dblgroup14.app.challenges.MultipleChoiceTriviaChallenge;
import com.dblgroup14.app.challenges.RebusChallengeFragment;
import com.dblgroup14.app.challenges.ShakeChallengeFragment;
import com.dblgroup14.app.edit.EditShakeChallengeFragment;

public class ManageChallengesFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_challenges, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        view.findViewById(R.id.alarmBtn).setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), AlarmActivity.class);
            startActivity(myIntent);
        });
        
        view.findViewById(R.id.editChallengesBtn).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditActivity.class);
            intent.putExtra("id", -1);    // TODO: Change to shake challenge database id
            intent.putExtra("object", "challenge");
            intent.putExtra("edit_fragment_class", EditShakeChallengeFragment.class.getName());
            startActivity(intent);
        });
        
        view.findViewById(R.id.rebusChallengeBtn).setOnClickListener(v -> {
            showChallenge(new RebusChallengeFragment());
        });
        /*
        view.findViewById(R.id.mathChallengeBtn).setOnClickListener(v -> {
            showChallenge(new MathChallengeFragment());
        });
         */
        //for testing
        view.findViewById(R.id.mathChallengeBtn).setOnClickListener(v -> {
            Intent challengesIntent = new Intent(getContext(), MultipleChoiceTriviaChallenge.class);
            startActivity(challengesIntent);
        });
        
        view.findViewById(R.id.shakeChallengeBtn).setOnClickListener(v -> {
            showChallenge(new ShakeChallengeFragment());
        });
        
        view.findViewById(R.id.barcodeChallengeBtn).setOnClickListener(v -> {
            showChallenge(new BarcodeChallengeFragment());
        });
    }
    
    private void showChallenge(ChallengeFragment challengeFragment) {
        FragmentManager mgr = getChildFragmentManager();
        mgr.beginTransaction().replace(R.id.challengeFrameLayout, challengeFragment).commit();
    }
}
