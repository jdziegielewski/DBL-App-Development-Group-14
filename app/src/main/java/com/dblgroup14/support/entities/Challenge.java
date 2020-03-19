package com.dblgroup14.support.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity to store data representing a type of challenge.
 * Please use the public methods to modify instance variables!
 */
@Entity(tableName = "challenges")
public class Challenge {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    public String name;
    @NonNull
    public String className;
    public Map<String, String> dataStorage;
    
    public Challenge() {
        dataStorage = new HashMap<>();
    }
    
    /**
     * Sets the name of this challenge.
     *
     * @param name The name of the challenge
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Given name is null");
        }
        this.name = name;
    }
    
    /**
     * Sets the name of the built-in class that belongs to this challenge.
     *
     * @param className The built-in class name belonging to this challenge
     */
    public void setClassName(String className) {
        if (className == null) {
            throw new IllegalArgumentException("Given name is null");
        }
        this.className = className;
    }
    
    /**
     * Put a new key-value pair in the data storage.
     *
     * @param key   The key string
     * @param value The value string
     */
    public void put(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("Null key given");
        } else if (value == null) {
            throw new IllegalArgumentException("Null value given");
        }
        dataStorage.put(key, value);
    }
    
    /**
     * Checks whether a value is set for a given key.
     *
     * @param key The key string
     * @return Whether the data storage contains the given key
     */
    public boolean has(String key) {
        return dataStorage.containsKey(key);
    }
    
    /**
     * Return string value in data storage.
     *
     * @param key The key string
     * @return String value in data storage
     * @throws IllegalArgumentException if given key is not in data storage
     */
    public String getString(String key) {
        if (!has(key)) {
            throw new IllegalArgumentException("Key not in data storage");
        }
        return dataStorage.get(key);
    }
    
    /**
     * Return an integer value in data storage.
     *
     * @param key The key string
     * @return Integer value in data storage
     * @throws NumberFormatException    if the data value in storage is not a parsable integer
     * @throws IllegalArgumentException if given key is not in data storage
     */
    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
    
    /**
     * Return a boolean value in data storage.
     *
     * @param key The key string
     * @return Boolean value in data storage
     * @throws IllegalArgumentException if given key is not in data storage
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }
    
    /**
     * Delete a given key from data storage.
     *
     * @param key The key to delete
     * @throws IllegalArgumentException if given key is not in data storage
     */
    public void delete(String key) {
        if (!has(key)) {
            throw new IllegalArgumentException("Key not set in data storage");
        }
        dataStorage.remove(key);
    }
}
