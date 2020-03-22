package com.dblgroup14.app.challenges;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.seismic.ShakeDetector;
import com.dblgroup14.app.R;

public class shakeChallenge extends AppCompatActivity implements ShakeDetector.Listener {
    
    private ShakeDetector detector;
    private int count = 0;
    private TextView shake_counter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakechallenge);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        detector = new ShakeDetector(this);
        detector.start(sensorManager);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        detector.stop();
    }
    
    @Override
    public void hearShake() {
        count++;
        TextView counter = findViewById(R.id.shake_counter);
        counter.setText(Integer.toString(10 - count));
        //Toast.makeText(this, "You shaked " + count + " times\n Shake, shake\n" +
        // "Shake, shake, shake it\n.", Toast.LENGTH_SHORT).show();
        
        if (count == 10) {
            detector.stop();
            finish();
        }
    }
    
}
