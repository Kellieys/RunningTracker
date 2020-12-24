package com.example.runningtracker;

// This class purpose is to locate all
// get and set functions related to run activity in one file to keep it neat

public class RunGetterSetter {

    // Initialization for all variables
    private String time;
    private String date;
    private float distance;

    // Empty constructor if there are no arguments for reflection
    public RunGetterSetter() {}

    // Constructor that contain arguments to take care of
    public RunGetterSetter(String time, String date, float distance)
    {
        this.time = time;
        this.date = date;
        this.distance = distance;
    }

    // Getter and setters functions that helped to protect the data
    public String get_date() {
        return date;
    }
    public String get_time() {
        return time;
    }
    public float get_distance() { return distance; }

    public void set_time(String time) {
        this.time = time;
    }
    public void set_date(String date) {
        this.date = date;
    }
    public void set_distance( float distance ) { this.distance = distance; }

} // end class