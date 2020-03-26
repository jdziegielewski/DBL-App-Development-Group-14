package com.dblgroup14.support.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dblgroup14.support.entities.Challenge;
import java.util.List;

@Dao
public abstract class ChallengeDao implements HostDaoInterface<Challenge> {
    @Query("SELECT * FROM challenges")
    public abstract LiveData<List<Challenge>> all();
    
    @Query("SELECT * FROM challenges")
    public abstract List<Challenge> allDirect();
    
    @Query("SELECT * FROM challenges WHERE `id`=:id")
    public abstract Challenge get(int id);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long store(Challenge challenge);
    
    @Delete
    public abstract void delete(Challenge challenge);
}
