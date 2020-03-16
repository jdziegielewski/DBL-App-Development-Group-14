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
public abstract class ChallengeDao {
    @Query("SELECT * FROM challenges")
    public abstract LiveData<List<Challenge>> all();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void store(Challenge challenge);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void storeAll(List<Challenge> challenges);
    
    @Delete
    public abstract void delete(Challenge challenge);
}
