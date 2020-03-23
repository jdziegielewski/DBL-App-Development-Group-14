package com.dblgroup14.app.challenges;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dblgroup14.app.R;
import com.squareup.seismic.ShakeDetector;

import static android.content.Context.SENSOR_SERVICE;

public class ShakeChallengeFragment extends ChallengeFragment implements ShakeDetector.Listener {
    
    private ShakeDetector detector;
    private TextView shakeCounter;
    
    private int shakesLeft = 10;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_shake, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get shake count text view
        shakeCounter = view.findViewById(R.id.shake_counter);
        
        // Start shake sensor
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        detector = new ShakeDetector(this);
        detector.start(sensorManager);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detector.stop();
    }
    
    @Override
    public void hearShake() {
        // Decrement shakes left
        shakesLeft--;
        
        // Update shake counter
        shakeCounter.setText(String.valueOf(shakesLeft));
        
        // Check for challenge completion
        if (shakesLeft == 0) {
            detector.stop();
            completeChallenge();
        }
    }
    
}
