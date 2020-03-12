package com.dblgroup14.support;

import android.content.Context;
import android.content.SharedPreferences;
import com.dblgroup14.app.SleapApplication;

/**
 * This class is used to access a simple database through Android's SharedPreferences API.
 */
public abstract class SimpleDatabase {
    /* Global keys */
    public static final String CURRENT_USERNAME = "100_username";
    public static final String CURRENT_USER_TOKEN = "100_user_token";
    public static final String CURRENT_USER_SUBSCRIPTIONS = "100_user_subscriptions";
    
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
     * Returns a SharedPreferences.Editor instance that can be used to edit the simple database directly.
     *
     * @return The SharedPreferences.Editor instance
     */
    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
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
