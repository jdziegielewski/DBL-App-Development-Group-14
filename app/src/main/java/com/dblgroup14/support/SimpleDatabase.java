package com.dblgroup14.support;

import android.content.Context;
import android.content.SharedPreferences;
import com.dblgroup14.app.SleapApplication;

/**
 * This class is used to access a simple database through Android's SharedPreferences API.
 */
public abstract class SimpleDatabase {
    /* Global keys */
    public static final String CURRENT_USERNAME = "LGN-username";
    public static final String COMPLETED_CHALLENGES = "CHA-completed";
    public static final String TOTAL_CHALLENGES = "CHA-total";
    
    private static final String PREFERENCES_NAME = "sleap-pref";
    
    /**
     * Returns a SharedPreferences instance that can be used to access the simple database directly.
     *
     * @return The SharedPreferences instance
     */
    public static SharedPreferences getSharedPreferences() {
        return SleapApplication.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * Gets the username of the currently logged in user.
     *
     * @return The current user's username
     */
    public static String getCurrentUsername() {
        return getSharedPreferences().getString(CURRENT_USERNAME, null);
    }
}
