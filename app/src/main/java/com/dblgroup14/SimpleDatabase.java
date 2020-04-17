package com.dblgroup14;

import android.content.Context;
import android.content.SharedPreferences;
import com.dblgroup14.SleapApplication;

/**
 * This class is used to access a simple database through Android's SharedPreferences API.
 */
public abstract class SimpleDatabase {
    public static final String COMPLETED_CHALLENGES = "CHA-completed";
    public static final String TOTAL_CHALLENGES = "CHA-total";
    public static final String LAST_COMPLETED_TIME = "CHA-last_completed";
    
    private static final String PREFERENCES_NAME = "sleap-pref";
    
    /**
     * Returns a SharedPreferences instance that can be used to access the simple database directly.
     *
     * @return The SharedPreferences instance
     */
    public static SharedPreferences getSharedPreferences() {
        return SleapApplication.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
