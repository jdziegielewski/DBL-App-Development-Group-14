package com.dblgroup14.support.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dblgroup14.support.RemoteDatabase;
import com.dblgroup14.support.entities.local.UserScore;
import java.util.List;

@Dao
public abstract class UserScoreDao {
    @Query("SELECT * FROM user_scores")
    public abstract LiveData<List<UserScore>> all();
    
    @Query("SELECT * FROM user_scores WHERE `username` = :username LIMIT 1")
    public abstract UserScore scoreOfUser(String username);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void store(UserScore score);
    
    @Delete
    public abstract void delete(UserScore score);
    
    /**
     * Gets the user score object of the current user.
     *
     * @return The UserScore object belonging to the current user, or null if no user is logged in
     */
    public UserScore getActiveUserScore() {
        // Check if there is a user logged in
        String currentUsername = RemoteDatabase.getCurrentUsername();
        
        if (currentUsername == null) {
            return scoreOfUser("(default)");
        } else {
            return scoreOfUser(currentUsername);
        }
    }
}
