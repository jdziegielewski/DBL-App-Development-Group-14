package com.dblgroup14.app;

import android.app.Application;
import android.content.Context;
import com.dblgroup14.support.AppDatabase;

public class SleapApplication extends Application {
    private static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    
        // Instantiate database
        AppDatabase.createDatabase(context);
    }
    
    @Override
    public void onTerminate() {
        super.onTerminate();
        
        // Close database
        AppDatabase.closeDatabase();
    }
    
    /**
     * Gets the application context.
     *
     * @return The application context
     */
    public static Context getContext() {
        return context;
    }
}
