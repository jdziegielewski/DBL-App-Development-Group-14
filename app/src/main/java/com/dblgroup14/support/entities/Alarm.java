package com.dblgroup14.support.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarms")
public class Alarm {
    @PrimaryKey
    protected int id;
    protected String name;
    protected int volume;
    
    protected int hours;
    protected int minutes;
    protected boolean repeats;
    protected boolean[] days;
    
    /**
     * Create an empty alarm instance.
     */
    public Alarm() {
        this("", 0, 0, 0, false);
    }
    
    /**
     * Create an alarm object with default values.
     *
     * @param name    The name of the alarm
     * @param hours   The hour at which the alarm will ring
     * @param minutes The minute at which the alarm will ring
     * @param volume  The volume of the alarm
     * @param repeats Whether the alarm will repeat
     * @param days    The days at which the alarm is set (to repeat)
     */
    public Alarm(String name, int hours, int minutes, int volume, boolean repeats, int... days) {
        setName(name);
        setTime(hours, minutes);
        setVolume(volume);
        setRepeats(repeats);
        
        this.days = new boolean[7];
        for (int day : days) {
            setDay(day, true);
        }
    }
    
    /**
     * Get alarm name.
     *
     * @return The name of this alarm
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get alarm volume.
     *
     * @return The volume of this alarm
     */
    public int getVolume() {
        return volume;
    }
    
    /**
     * Get the hour at which this alarm will ring.
     *
     * @return The hour at which the alarm will ring
     */
    public int getHours() {
        return hours;
    }
    
    /**
     * Get the minute at which this alarm will ring.
     *
     * @return The minute at which this alarm will ring.
     */
    public int getMinutes() {
        return minutes;
    }
    
    /**
     * Get whether this alarm will repeat on set days.
     *
     * @return Whether the alarm will repeat
     */
    public boolean getRepeats() {
        return repeats;
    }
    
    /**
     * Get whether the alarm is set to ring on a given day.
     *
     * @param day Day to check (0 - 6 : monday - sunday)
     * @return Whether the alarm is set to ring on this day
     */
    public boolean getDay(int day) {
        if (day < 0 || day > 6) {
            throw new IllegalArgumentException("Invalid day ID");
        }
        return days[day];
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
     * Set alarm volume.
     *
     * @param volume The new volume of this alarm
     */
    public void setVolume(int volume) {
        if (volume < 0) {
            throw new IllegalArgumentException("Invalid volume given");
        }
        this.volume = volume;
    }
    
    /**
     * Set the hour at which this alarm will ring.
     *
     * @param hours The hour at which the alarm will ring
     */
    public void setHours(int hours) {
        if (hours < 0 || hours > 23) {
            throw new IllegalArgumentException("Invalid hours given");
        }
        this.hours = hours;
    }
    
    /**
     * Set the minute at which this alarm will ring.
     *
     * @param minutes The minute at which the alarm will ring
     */
    public void setMinutes(int minutes) {
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
     * Whether to repeat this alarm on the set days.
     *
     * @param repeats Whether to repeat this alarm
     */
    public void setRepeats(boolean repeats) {
        this.repeats = repeats;
    }
    
    /**
     * Set whether to ring the alarm on a given day.
     *
     * @param day    Day to repeat (0 - 6 : monday - sunday)
     * @param repeat Whether to ring on this day
     */
    public void setDay(int day, boolean repeat) {
        if (day < 0 || day > 6) {
            throw new IllegalArgumentException("Invalid day ID");
        }
        days[day] = repeat;
    }
}
