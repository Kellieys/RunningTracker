package com.example.runningtracker;

import android.widget.Toast;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

// OFFICIAL DOCUMENTATION: https://developer.android.com/guide/components/broadcasts
// REFERENCE: https://www.tutorialspoint.com/android/android_broadcast_receivers.htm
// This class implements broadcast receiver by inheriting BroadcastReceiver class and override its callback method
// Purpose as register for system or application events

public class AppBroadcastReceiver extends BroadcastReceiver {

    // After the onReceive() of the receiver class has finished, the Android system is allowed to recycle the receiver
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent serviceIntent = new Intent(context, AppServiceActivity.class);
        serviceIntent.setAction(AppServiceActivity.ACTION_STOP);
        context.startService(serviceIntent);
    }

} // end class
