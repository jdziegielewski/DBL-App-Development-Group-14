package com.dblgroup14.support;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.dblgroup14.support.entities.Alarm;

@Database(entities = {Alarm.class}, version = 1)
@TypeConverters({RoomTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {
}
