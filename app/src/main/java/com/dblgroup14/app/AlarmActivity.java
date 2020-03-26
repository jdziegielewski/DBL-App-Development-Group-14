package com.dblgroup14.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
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
import com.dblgroup14.support.entities.Alarm;
import com.dblgroup14.support.entities.Challenge;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity {
    
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
    
    private boolean mVisible;
    private Handler mRepeatHandler;
    private Handler mHideHandler;
    private AudioManager mAudioManager;
    
    private Alarm mCurrentAlarm;
    private ChallengeFragment mChallengeFragment;
    
    /* Runnables */
    
    private final Runnable mHidePart2Runnable = () -> decorView.setSystemUiVisibility(hideSystemBars());
    private final Runnable mShowPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    };
    private final Runnable mHideRunnable = this::hide;
    
    /* View listeners */
    
    private final View.OnClickListener hideOnClickListener = v -> toggle();
    private final DialogInterface.OnClickListener losingPopupButtonListener = (o, i) -> finish();
    private final View.OnClickListener giveUpButtonListener = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
        builder.setCancelable(false);
        builder.setTitle(LOSING_POPUP_TITLE);
        builder.setMessage(LOSING_POPUP_MESSAGE);
        builder.setPositiveButton(LOSING_POPUP_BUTTON_MESSAGE, losingPopupButtonListener);
        builder.show();
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
            mCurrentAlarm = AppDatabase.db().alarmDao().get(alarmId);
            
            final List<Challenge> allChallenges = AppDatabase.db().challengeDao().allDirect();
            runOnUiThread(() -> initializeChallenge(allChallenges));
        });
        
        // Set ui to be visible
        mVisible = true;
        
        // Get audio manager
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        
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
        mHideHandler = new Handler();
        mRepeatHandler = new Handler();
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
        if (mChallengeFragment != null) {
            mChallengeFragment.onBackPressed();
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
        
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.removeCallbacks(mHidePart2Runnable);
    }
    
    private void updateView() {
        // Hide system bars
        mHidePart2Runnable.run();
        
        // Update time
        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
        timeTextView.setText(currentTime);
        
        // Change volume to alarm volume
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) ((mCurrentAlarm.volume / 100.0) * maxVolume), 0);
        
        // Post next update
        mRepeatHandler.postDelayed(this::updateView, 1000);
    }
    
    private void initializeChallenge(List<Challenge> allChallenges) {
        // Get a challenge that belongs to this alarm (or pick a random one if none are selected)
        Challenge challenge = null;
        if (mCurrentAlarm.challengeIds.size() == 0) {
            challenge = allChallenges.get(new Random().nextInt(allChallenges.size()));
        } else {
            int i = new Random().nextInt(mCurrentAlarm.challengeIds.size());
            for (Challenge c : allChallenges) {
                if (c.id == mCurrentAlarm.challengeIds.get(i)) {
                    challenge = c;
                    break;
                }
            }
            if (challenge == null) {
                // TODO: Handle the case when an alarm uses a deleted challenge
                throw new IllegalStateException("Invalid challenge id was set in alarm");
            }
        }
        
        // Instantiate challenge fragment
        try {
            mChallengeFragment = (ChallengeFragment) Class.forName(challenge.className).newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Invalid challenge class given");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Place challenge fragment in lay-out
        getSupportFragmentManager().beginTransaction().replace(R.id.challengeContainerLayout, mChallengeFragment).commit();
        
        // Start repeat handler
        mRepeatHandler.postDelayed(this::updateView, 1);
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
        if (mVisible) {
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
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;
        
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
    
    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        
        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }
    
    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide() {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 100);
    }
}
