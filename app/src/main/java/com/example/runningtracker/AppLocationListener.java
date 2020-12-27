package com.example.runningtracker;

import android.os.Bundle;
import java.text.DecimalFormat;
import android.location.Location;
import android.location.LocationListener;

// OFFICIAL DOCUMENTATION AND REFERENCES (Location Listener)
// https://developer.android.com/reference/android/location/LocationListener
// https://stackoverflow.com/questions/42218419/how-do-i-implement-the-locationlistener
// https://stackoverflow.com/questions/8600688/android-when-exactly-is-onlocationchanged-called
// OFFICIAL DOCUMENTATION (Decimal Format)
// https://developer.android.com/reference/java/text/DecimalFormat

public class AppLocationListener implements LocationListener {

    public static float distance;
    public static Location currentLocation;
    private static DecimalFormat time_format = new DecimalFormat("0.00");

    // Called each time when the location has changed
    @Override
    public void onLocationChanged(Location location)
    {
        // If the location has no changes detected, the distance will remain 0
        if (currentLocation == null)
        { distance = 0; }

        // Once the location is updated and no longer NULL, else condition will be fulfilled
        // and will continuously being added to calculate the total distance
        else
        { distance =  distance + location.distanceTo(currentLocation); }

        // Assign location as the latest location
        currentLocation = location;

        // Reflect the changes to TimerActivity
        TimerActivity.current_distance_textView.setText(time_format.format(AppLocationListener.distance) + " m");
    }

    // Not implemented in this project
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    // Not implemented in this project
    @Override
    public void onProviderEnabled(String provider) {}

    // Not implemented in this project
    @Override
    public void onProviderDisabled(String provider) {}

} // end class