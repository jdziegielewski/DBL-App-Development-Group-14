package com.dblgroup14.support.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.ArrayList;

/**
 * Entity to store a challenge series ('playlist').
 * Please use the public methods to modify instance variables!
 */
@Entity(tableName = "challenge_series")
public class ChallengeSeries {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public ArrayList<Integer> challengeIds;
    
    public ChallengeSeries() {
        challengeIds = new ArrayList<>();
    }
    
    /**
     * Adds a challenge type to this list.
     *
     * @param id ID of challenge type
     * @throws IllegalArgumentException if challenge type already in the list
     */
    public void addChallenge(int id) {
        if (challengeIds.contains(id)) {
            throw new IllegalArgumentException("Challenge type already in list");
        }
        challengeIds.add(id);
    }
    
    /**
     * Removes a challenge type from this list.
     *
     * @param id ID of challenge type
     * @throws IllegalArgumentException if challenge type not in list
     */
    public void removeChallenge(int id) {
        if (!challengeIds.contains(id)) {
            throw new IllegalArgumentException("Challenge type not in list");
        }
        challengeIds.remove((Integer) id);
    }
    
    /**
     * Swap two challenges types from position in the order of this list.
     *
     * @param i Index of first challenge
     * @param j Index of second challenge
     * @throws IllegalArgumentException if i or j out of array bounds
     */
    public void swap(int i, int j) {
        if (i < 0 || j < 0 || i >= challengeIds.size() || j >= challengeIds.size()) {
            throw new IllegalArgumentException("Given index out of bounds");
        }
        
        Integer a = challengeIds.get(i);
        challengeIds.set(i, challengeIds.get(j));
        challengeIds.set(j, a);
    }
}
