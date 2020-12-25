package com.example.runningtracker;

import android.os.Bundle;
import java.text.DecimalFormat;
import java.util.Timer;

import android.location.Location;
import android.location.LocationListener;

// REFERENCES https://developer.android.com/reference/android/location/LocationListener
// https://stackoverflow.com/questions/42218419/how-do-i-implement-the-locationlistener

public class AppLocationListener implements LocationListener {

    // 2 decimal format
    private static DecimalFormat df = new DecimalFormat("0.00");
    public static Location previousLocation;
    public static float distance;

    // everytime a location is updated
    // it will be used to calculate distance with the previous location
    // and get assigned as the previous location
    @Override
    public void onLocationChanged(Location location) {

        // if there is no previous location
        // ie first change in location
        // distance should be 0
        if (previousLocation == null)
        {
            distance = 0;
        }
        else
        {
            // accumulate distance
            distance =  distance + location.distanceTo(previousLocation);
        }

        // current location becomes previous location
        previousLocation = location;
        // update the textview
        TimerActivity.current_distance_textView.setText(df.format(AppLocationListener.distance) + " m");
    } 

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

} // end class