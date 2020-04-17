package com.dblgroup14.database_support.entities.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The entity class for user scores.
 */
@Entity(tableName = "user_scores")
public class UserScore {
    @PrimaryKey
    @NonNull
    public String username;
    public long score;
    
    public UserScore()
    {
        this.username = "UNDEFINED";
        this.score = 0;
    }
    
    /**
     * Set new username for this user score object.
     *
     * @param username Name of user to whom this score belongs
     */
    public void setUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Null username given");
        }
        this.username = username;
    }
    
    /**
     * Add points to this user's score.
     *
     * @param points The amount of points to add
     */
    public void addPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Adding negative points!");
        }
        score += points;
    }
    
    /**
     * Subtract points from this user's score.
     *
     * @param points The amount of points to subtract
     */
    public void subtractPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Subtracting negative points!");
        }
        score -= points;
    }
}
