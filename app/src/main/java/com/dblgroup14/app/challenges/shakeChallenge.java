package com.dblgroup14.app.challenges;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;
import com.squareup.seismic.ShakeDetector;
import com.dblgroup14.app.R;

public class shakeChallenge extends AppCompatActivity implements ShakeDetector.Listener {
    
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakechallenge);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sensorManager);
        count=0;
    }
 
    
    @Override
    public void hearShake() {
        count++;
        Toast.makeText(this, "You shaked "+count+" time\n Shake, shake\n" +
                "Shake, shake, shake it\n.", Toast.LENGTH_SHORT).show();
        //i dont know how to print it other way
        if(count==10){
            count=0;
            finish();
        }
    }

}
