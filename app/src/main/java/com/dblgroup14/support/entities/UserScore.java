package com.dblgroup14.support.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The entity class for user scores.
 * Please use the setXXX() methods to set instance variables!
 */
@Entity(tableName = "user_scores")
public class UserScore {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    @ColumnInfo(name = "user_name")
    public String userName;
}
