package com.example.runningtracker;

import android.widget.Toast;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

//REFERENCES: https://developer.android.com/guide/components/broadcasts
//https://www.tutorialspoint.com/android/android_broadcast_receivers.htm

public class AppBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent serviceIntent = new Intent(context, AppServiceActivity.class);
//        serviceIntent.setAction(AppServiceActivity.ACTION_STOP);
        context.startService(serviceIntent);
    }

} // end class
