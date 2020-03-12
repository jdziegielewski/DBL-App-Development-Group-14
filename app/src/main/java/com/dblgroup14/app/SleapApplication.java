package com.dblgroup14.app;

import android.app.Application;
import android.content.Context;

public class SleapApplication extends Application {
    private static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    
    /**
     * Gets the application context.
     *
     * @return The pplication context
     */
    public static Context getContext() {
        return context;
    }
}
