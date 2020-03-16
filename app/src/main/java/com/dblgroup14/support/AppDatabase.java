package com.dblgroup14.support;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.dblgroup14.support.dao.AlarmDao;
import com.dblgroup14.support.dao.ChallengeDao;
import com.dblgroup14.support.dao.UserScoreDao;
import com.dblgroup14.support.entities.Alarm;
import com.dblgroup14.support.entities.Challenge;
import com.dblgroup14.support.entities.UserScore;

@Database(entities = {Alarm.class, UserScore.class, Challenge.class}, version = 1, exportSchema = false)
@TypeConverters({RoomTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database = null;
    
    /**
     * Creates an internal database instance (only once).
     *
     * @param context The application context in which to create the database
     */
    public static void createDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "sleap-db").build();
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
    
    public abstract UserScoreDao userScoreDao();
    
    public abstract ChallengeDao challengeDao();
}
