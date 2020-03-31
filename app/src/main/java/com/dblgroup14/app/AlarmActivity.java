package com.dblgroup14.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.challenges.ChallengeFragment;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.SimpleDatabase;
import com.dblgroup14.support.entities.Alarm;
import com.dblgroup14.support.entities.Challenge;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity implements ChallengeFragment.OnChallengeCompletedListener {
    
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 100;
    
    /**
     * Lose message related strings.
     */
    private static final String LOSING_POPUP_MESSAGE = "Snoozer looser";
    private static final String LOSING_POPUP_BUTTON_MESSAGE = "I'm a sleap...";
    private static final String LOSING_POPUP_TITLE = "You lost!";
    
    private TextView timeTextView;
    private View decorView;
    
    private boolean visible;
    private Alarm currentAlarm;
    private Challenge currentChallenge;
    private ChallengeFragment challengeFragment;
    
    private Handler repeatHandler;
    private Handler hideHandler;
    private AudioManager audioManager;
    private MediaPlayer alarmSoundPlayer;
    
    /* Runnables */
    
    private final Runnable hidePart2Runnable = () -> decorView.setSystemUiVisibility(hideSystemBars());
    private final Runnable showPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };
    private final Runnable hideRunnable = this::hide;
    
    /* View listeners */
    
    private final View.OnClickListener hideOnClickListener = v -> toggle();
    private final DialogInterface.OnClickListener losingPopupButtonListener = (o, i) -> finish();
    private final View.OnClickListener giveUpButtonListener = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
        builder.setCancelable(true);
        builder.setTitle(LOSING_POPUP_TITLE);
        builder.setMessage(LOSING_POPUP_MESSAGE);
        builder.setPositiveButton(LOSING_POPUP_BUTTON_MESSAGE, losingPopupButtonListener);
        builder.show();
        
        alarmSoundPlayer.stop();
        alarmSoundPlayer.release();
    };
    private final View.OnSystemUiVisibilityChangeListener visibilityChangeListener = v -> {
        if (v == 0) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        
        // Get alarm object that is run
        final int alarmId = getIntent().getIntExtra("alarm_id", 0);
        if (alarmId <= 0) {
            throw new IllegalArgumentException("No alarm id provided");
        }
        AsyncTask.execute(() -> {
            // Get alarm from database
            currentAlarm = AppDatabase.db().alarmDao().get(alarmId);
            initializeAlarm();
            
            // Initialize challenge
            final List<Challenge> allChallenges = AppDatabase.db().challengeDao().allDirect();
            runOnUiThread(() -> initializeChallenge(allChallenges));
        });
        
        // Set ui to be visible
        visible = true;
        
        // Get audio manager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        
        // Initialize decor view
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibilityChangeListener);
        decorView.setOnClickListener(hideOnClickListener);
        
        // Initialize give up button
        ImageButton giveUpButton = findViewById(R.id.giveUpBtn);
        giveUpButton.setOnClickListener(giveUpButtonListener);
        
        // Initialize time text view
        timeTextView = findViewById(R.id.systemTimeText);
        
        // Initialize hide bars view
        TextView hideBarsView = findViewById(R.id.hideBarsView);
        hideBarsView.setOnClickListener(hideOnClickListener);
        
        // Initialize handlers
        hideHandler = new Handler();
        repeatHandler = new Handler();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        delayedHide();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        delayedHide();
    }
    
    @Override
    public void onBackPressed() {
        if (challengeFragment != null) {
            challengeFragment.onBackPressed();
        }
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        hideHandler.removeCallbacks(showPart2Runnable);
        hideHandler.removeCallbacks(hidePart2Runnable);
    }
    
    @Override
    public void finish() {
        // Stop ringtone sound
        if (alarmSoundPlayer != null && alarmSoundPlayer.isPlaying()) {
            alarmSoundPlayer.stop();
            alarmSoundPlayer.release();
        }
        
        // Stop repeat handler
        if (repeatHandler != null) {
            repeatHandler.removeCallbacks(this::updateView);
        }
    }
    
    @Override
    public void onChallengeCompleted() {
        // Increase amount of completed challenges
        SharedPreferences db = SimpleDatabase.getSharedPreferences();
        int completedChallenges = db.getInt(SimpleDatabase.COMPLETED_CHALLENGES, 0) + 1;
        db.edit().putInt(SimpleDatabase.COMPLETED_CHALLENGES, completedChallenges).apply();
        
        // Close activity
        finish();
    }
    
    private void updateView() {
        // Hide system bars
        hidePart2Runnable.run();
        
        // Update time
        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
        timeTextView.setText(currentTime);
        
        // Change volume to alarm volume
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) ((currentAlarm.volume / 100.0) * maxVolume), 0);
        
        // Post next update
        repeatHandler.postDelayed(this::updateView, 1000);
    }
    
    private void initializeAlarm() {
        // TODO: Reschedule alarm if set to repeat
        
        try {
            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            alarmSoundPlayer = MediaPlayer.create(getApplicationContext(), ringtoneUri);
            alarmSoundPlayer.setScreenOnWhilePlaying(true);
            alarmSoundPlayer.setLooping(true);
            alarmSoundPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initializeChallenge(List<Challenge> allChallenges) {
        // Find a (random) challenge that belongs to this alarm
        currentChallenge = findAlarmChallenge(allChallenges);
        
        // Instantiate challenge fragment
        try {
            challengeFragment = (ChallengeFragment) Class.forName(currentChallenge.getShowFragmentClassName()).newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Invalid challenge class given");
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        
        // Set on completion handler
        challengeFragment.setOnChallengeCompletedListener(this);
        
        // Place challenge fragment in lay-out
        getSupportFragmentManager().beginTransaction().replace(R.id.challengeContainerLayout, challengeFragment).commit();
        
        // Start repeat handler
        repeatHandler.postDelayed(this::updateView, 1);
    }
    
    private Challenge findAlarmChallenge(List<Challenge> allChallenges) {
        if (currentAlarm.challengeIds.size() > 0) {
            int i = new Random().nextInt(currentAlarm.challengeIds.size());
            for (Challenge c : allChallenges) {
                if (c.id == currentAlarm.challengeIds.get(i)) {
                    return c;
                }
            }
        }
        
        // Return a random challenge
        return allChallenges.get(new Random().nextInt(allChallenges.size()));
    }
    
    public Challenge getCurrentChallenge() {
        return currentChallenge;
    }
    
    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }
    
    private void toggle() {
        if (visible) {
            hide();
        } else {
            show();
        }
    }
    
    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        visible = false;
        
        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable);
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY);
    }
    
    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        visible = true;
        
        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable);
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY);
    }
    
    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide() {
        hideHandler.removeCallbacks(hideRunnable);
        hideHandler.postDelayed(hideRunnable, 100);
    }
}
