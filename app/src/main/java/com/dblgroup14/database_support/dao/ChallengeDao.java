package com.dblgroup14.database_support.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dblgroup14.database_support.entities.local.Challenge;
import java.util.List;

/**
 * Class for the challenge related queries.
 * This handles all the DB interaction for the challenges
 */
@Dao
public abstract class ChallengeDao implements EditDaoInterface<Challenge> {
    @Query("SELECT * FROM challenges ORDER BY LENGTH(name)")
    public abstract LiveData<List<Challenge>> all();
    
    @Query("SELECT * FROM challenges WHERE isDefault=1 ORDER BY LENGTH(name)")
    public abstract LiveData<List<Challenge>> allDefault();
    
    @Query("SELECT * FROM challenges WHERE isDefault=0 ORDER BY LENGTH(name)")
    public abstract LiveData<List<Challenge>> allCustom();
    
    @Query("SELECT * FROM challenges")
    public abstract List<Challenge> allDirect();
    
    @Query("SELECT * FROM challenges WHERE `id`=:id")
    public abstract Challenge get(int id);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long store(Challenge challenge);
    
    @Delete
    public abstract void delete(Challenge challenge);
}
