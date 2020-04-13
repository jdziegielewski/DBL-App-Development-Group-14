package com.dblgroup14.support;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.dblgroup14.support.dao.AlarmDao;
import com.dblgroup14.support.dao.ChallengeDao;
import com.dblgroup14.support.dao.UserScoreDao;
import com.dblgroup14.support.entities.local.Alarm;
import com.dblgroup14.support.entities.local.Challenge;
import com.dblgroup14.support.entities.local.UserScore;

@Database(entities = {Alarm.class, Challenge.class, UserScore.class}, version = 1)
@TypeConverters({RoomTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database = null;
    
    /**
     * Creates an internal database instance (only once).
     *
     * @param context The application context in which to create the database
     */
    public static void createDatabase(Context context) {
        createDatabase(context, false);
    }
    
    /**
     * Creates an internal database instance (only once).
     *
     * @param context      The application context in which to create the database
     * @param testDatabase Whether this database is used for instrumented tests
     */
    public static void createDatabase(Context context, boolean testDatabase) {
        if (database == null) {
            if (testDatabase) {
                database = Room.databaseBuilder(context, AppDatabase.class, "sleap-db-test").build();
            } else {
                database = Room.databaseBuilder(context, AppDatabase.class, "sleap-db")
                        .createFromAsset("prefab.db")
                        .build();
            }
        }
    }
    
    /**
     * Closes the internal database instance.
     */
    public static void closeDatabase() {
        if (database != null) {
            database.close();
        }
    }
    
    /**
     * Gets the internal database instance. Will return null if @{link #createDatabase(Context context) createDatabase} has not been called first.
     *
     * @return Internal AppDatabase instance
     */
    public static AppDatabase db() {
        return database;
    }
    
    /* DAO definitions */
    
    public abstract AlarmDao alarmDao();
    
    public abstract ChallengeDao challengeDao();
    
    public abstract UserScoreDao userScoreDao();
}
