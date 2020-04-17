package com.dblgroup14.database_support.entities.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.ArrayList;

/**
 * The entity class for an Alarm object.
 * Please use the setXXX() methods to set instance variables!
 */
@Entity(tableName = "alarms")
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    @NonNull
    public String name;
    public boolean enabled;
    public int volume;
    public int hours;
    public int minutes;
    public boolean repeats;
    public boolean[] days;
    public ArrayList<Integer> challengeIds;
    
    /**
     * Public constructor to satisfy ROOM. Do not use directly!
     */
    public Alarm() { this("", 0, 0, false, 0, false); }
    
    /**
     * Create an alarm object with default values.
     *
     * @param name    The name of the alarm
     * @param hours   The hour at which the alarm will ring
     * @param minutes The minute at which the alarm will ring
     * @param enabled Whether the alarm is enabled
     * @param volume  The volume of the alarm
     * @param repeats Whether the alarm will repeat
     * @param days    The days at which the alarm is set (to repeat)
     */
    public Alarm(String name, int hours, int minutes, boolean enabled, int volume, boolean repeats, int... days) {
        setName(name);
        setTime(hours, minutes);
        setVolume(volume);
        
        this.enabled = enabled;
        this.repeats = repeats;
        this.days = new boolean[7];
        this.challengeIds = new ArrayList<>();
        
        for (int day : days) {
            setDay(day, false);
        }
    }
    
    /**
     * Set alarm name.
     *
     * @param name The new name of this alarm
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Null name given");
        }
        this.name = name;
    }
    
    /**
     * Set alarm volume in the range [0-100]
     *
     * @param volume The new volume of this alarm
     */
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Invalid volume given");
        }
        this.volume = volume;
    }
    
    /**
     * Set the hour at which this alarm will ring.
     *
     * @param hours The hour at which the alarm will ring
     */
    private void setHours(int hours) {
        if (hours < 0 || hours > 23) {
            throw new IllegalArgumentException("Invalid hours given");
        }
        this.hours = hours;
    }
    
    /**
     * Set if the alarm is enabled
     *
     * @param enabled If the alarm is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Set the minute at which this alarm will ring.
     *
     * @param minutes The minute at which the alarm will ring
     */
    private void setMinutes(int minutes) {
        if (minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Invalid minutes given");
        }
        this.minutes = minutes;
    }
    
    /**
     * Set the time at which this alarm will ring.
     *
     * @param hours   The hour at which the alarm will ring
     * @param minutes This minute at which the alarm will ring
     */
    public void setTime(int hours, int minutes) {
        setHours(hours);
        setMinutes(minutes);
    }
    
    /**
     * Set whether to ring the alarm on a given day.
     *
     * @param day   Day to repeat (0 - 6 : monday - sunday)
     * @param rings Whether to ring on this day
     */
    public void setDay(int day, boolean rings) {
        if (day < 0 || day > 6) {
            throw new IllegalArgumentException("Invalid day ID");
        }
        days[day] = rings;
    }
    
    /**
     * Set alarm repeat.
     */
    public void setRepeats(boolean repeat) {
        this.repeats = repeat;
    }
    
    /**
     * Adds a challenge to run to this alarm.
     *
     * @param id The ID of the challenge to add
     */
    public void addChallenge(int id) {
        if (!challengeIds.contains(id)) {
            challengeIds.add(id);
        }
    }
    
    /**
     * Checks whether a challenge is currently added to this alarm.
     *
     * @param id The id of the challenge to check
     * @return Whether the challenge is added to this alarm
     */
    public boolean hasChallenge(int id) {
        return challengeIds.contains(id);
    }
    
    /**
     * Removes a challenge to run from this alarm.
     *
     * @param id The ID of the challenge to remove
     */
    public void removeChallenge(int id) {
        challengeIds.remove((Integer) id);
    }
}
