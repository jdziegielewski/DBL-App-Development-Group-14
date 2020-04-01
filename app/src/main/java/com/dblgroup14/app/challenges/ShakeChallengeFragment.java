package com.dblgroup14.app.challenges;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dblgroup14.app.AlarmActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.edit.EditShakeChallengeFragment;
import com.dblgroup14.support.entities.Challenge;
import com.squareup.seismic.ShakeDetector;

import static android.content.Context.SENSOR_SERVICE;

/**
 * For detecting a shake of the phone in the shake challenge we use seismic library: com.squareup:seismic:1.0.2
 */
public class ShakeChallengeFragment extends ChallengeFragment implements ShakeDetector.Listener {
    private TextView shakeCountText;
    
    private ShakeDetector shakeDetector;
    private int shakesLeft = 10;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_shake, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get amount of shakes from challenge data
        Challenge c = ((AlarmActivity) getActivity()).getCurrentChallenge();
        shakesLeft = c.getInt(EditShakeChallengeFragment.KEY_SHAKE_COUNT);
        
        // Get shake count text view
        shakeCountText = view.findViewById(R.id.shakeCountText);
        updateShakeCount();
        
        // Start shake sensor
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sensorManager);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shakeDetector.stop();
    }
    
    @Override
    public void hearShake() {
        // Decrement shakes left
        shakesLeft--;
        
        // Update shake counter
        updateShakeCount();
        
        // Check for challenge completion
        if (shakesLeft == 0) {
            shakeDetector.stop();
            completeChallenge();
        }
    }
    
    private void updateShakeCount() {
        shakeCountText.setText(String.valueOf(shakesLeft));
    }
}
