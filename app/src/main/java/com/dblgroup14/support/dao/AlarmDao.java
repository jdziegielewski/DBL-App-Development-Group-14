package com.dblgroup14.support.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.dblgroup14.support.entities.Alarm;
import java.util.List;

@Dao
public abstract class AlarmDao {
    @Query("SELECT * FROM alarms")
    public abstract LiveData<List<Alarm>> all();
    
    @Query("SELECT * FROM alarms WHERE `enabled`=1")
    public abstract LiveData<List<Alarm>> allActive();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void store(Alarm alarm);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void storeAll(Alarm... alarms);
    
    @Delete
    public abstract void delete(Alarm alarm);
}
