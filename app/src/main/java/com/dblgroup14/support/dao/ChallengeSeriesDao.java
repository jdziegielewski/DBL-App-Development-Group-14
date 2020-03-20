package com.dblgroup14.support.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dblgroup14.support.entities.ChallengeSeries;
import java.util.List;

@Dao
public abstract class ChallengeSeriesDao {
    @Query("SELECT * FROM challenge_series")
    public abstract LiveData<List<ChallengeSeries>> all();
    
    @Query("SELECT * FROM challenge_series WHERE `id`=:id")
    public abstract ChallengeSeries get(int id);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void store(ChallengeSeries challengeSeries);
    
    @Delete
    public abstract void delete(ChallengeSeries challengeSeries);
}
