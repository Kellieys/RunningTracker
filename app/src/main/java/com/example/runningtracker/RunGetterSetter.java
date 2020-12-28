package com.example.runningtracker;

// This class purpose is to locate all
// get and set functions related to run activity in one file to keep it neat

public class RunGetterSetter {

    // Initialization for all variables
    private String time;
    private String date;
    private String comment;
    private float distance;

    // Empty constructor if there are no arguments for reflection
    public RunGetterSetter() {}

    // Constructor that contain arguments to take care of
    public RunGetterSetter(String time, String date, String comment, float distance)
    {
        this.time = time;
        this.date = date;
        this.comment = comment;
        this.distance = distance;
    }

    // Getter and setters functions that helped to protect the data
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getComment() {
        return comment;
    }
    public float getDistance() { return distance; }
    public void setTime(String time) { this.time = time; }
    public void setDate(String date) { this.date = date; }
    public void setComment(String comment) { this.comment = comment; }
    public void setDistance( float distance ) { this.distance = distance; }

} // end class