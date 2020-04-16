package com.dblgroup14.support.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dblgroup14.support.entities.local.UserScore;
import java.util.List;

/**
 *
 *
 *
 *
 */
@Dao
public abstract class UserScoreDao {
    @Query("SELECT * FROM user_scores")
    public abstract LiveData<List<UserScore>> all();
    
    @Query("SELECT * FROM user_scores WHERE `username` = :username LIMIT 1")
    public abstract UserScore scoreOfUser(String username);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void store(UserScore score);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void storeAll(List<UserScore> scores);
    
    @Query("DELETE FROM user_scores WHERE `username` != '(default)'")
    public abstract void deleteAll();

}
