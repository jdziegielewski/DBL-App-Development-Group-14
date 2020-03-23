package com.dblgroup14.app;

import android.annotation.SuppressLint;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AlarmActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
    
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 100;
    
    private static final String LOSING_POPUP_MESSAGE = "Snoozer looser";
    private static final String LOSING_POPUP_BUTTON_MESSAGE = "I'm a sleap...";
    private static final String LOSING_POPUP_TITLE = "You lost!";
    
    private ImageButton giveUpButton;
    private TextView timeTextView;
    private TextView hideBarsView;
    
    private Handler repeatingHandler;
    private final Handler mHideHandler = new Handler();
    
    private View decorView;
    
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    };
    
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            // mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private View.OnClickListener hideOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggle();
        }
    };
    
    private DialogInterface.OnClickListener losingPopupButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(myIntent);
        }
    };
    
    
    private View.OnClickListener giveUpbuttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
            builder.setCancelable(false);
            builder.setTitle(LOSING_POPUP_TITLE);
            builder.setMessage(LOSING_POPUP_MESSAGE);
            builder.setPositiveButton(LOSING_POPUP_BUTTON_MESSAGE, losingPopupButtonListener);
            builder.show();
        }
    };
    
    View.OnSystemUiVisibilityChangeListener visibilityChangeListener = new View.OnSystemUiVisibilityChangeListener() {
        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            if (visibility == 0) {
                decorView.setSystemUiVisibility(hideSystemBars());
            }
        }
    };
    private AudioManager audioManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_play_challenge_page);
        
        decorView = getWindow().getDecorView();
        mVisible = true;
        
        decorView.setOnSystemUiVisibilityChangeListener(visibilityChangeListener);
        decorView.setOnClickListener(hideOnClickListener);
        giveUpButton = (ImageButton) findViewById(R.id.giveUpButton);
        timeTextView = (TextView) findViewById(R.id.showTimeTextView);
        hideBarsView = (TextView) findViewById(R.id.hideBarsView);
        hideBarsView.setOnClickListener(hideOnClickListener);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        repeatingHandler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                repeatingHandler.postDelayed(this, 100);
                mHidePart2Runnable.run();
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
                timeTextView.setText(currentTime);
            }
        };
        repeatingHandler.postDelayed(r, 0000);
        giveUpButton.setOnClickListener(giveUpbuttonListener);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        delayedHide(100);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        delayedHide(100);
        
    }
    
    
    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }
    
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
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
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
