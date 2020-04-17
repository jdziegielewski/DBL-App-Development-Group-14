package com.dblgroup14.database_support.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dblgroup14.database_support.entities.local.Alarm;
import java.util.List;

/**
 * Class for the alarm related DB queries
 */
@Dao
public abstract class AlarmDao implements EditDaoInterface<Alarm> {
    /**
     * Query that gets all the alarms of the user
     */
    @Query("SELECT * FROM alarms")
    public abstract LiveData<List<Alarm>> all();
    
    /**
     * Query that gets a specific alarm by id
     */
    @Query("SELECT * FROM alarms WHERE `id`=:id")
    public abstract Alarm get(int id);
    
    /**
     * Method that stores an alarm into the database.
     * Conflicts are solved by replacing the old alarm with the new one.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long store(Alarm alarm);
    
    /**
     * Method for removing an alarm from the database
     */
    @Delete
    public abstract void delete(Alarm alarm);
}
