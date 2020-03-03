package com.dblgroup14.app.ui.management;

public class Alarm {
    private String name;
    private int hours;
    private int min;
    private int day;
    private int volume;
    private boolean repeat;
    
    public Alarm(String newName, int newHours, int newMin, int newDay, int newVolume, boolean newRepeat){
        name = newName;
        hours = newHours;
        min = newMin;
        day = newMin;
        volume = newVolume;
        repeat = newRepeat;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getVolume() {
        return volume;
    }
    
    public void setVolume(int volume) {
        this.volume = volume;
    }
    
    public boolean isRepeat() {
        return repeat;
    }
    
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
    
    public int getHours() {
        return hours;
    }
    
    public int getMin() {
        return min;
    }
    
    public int getDay() {
        return day;
    }
}
