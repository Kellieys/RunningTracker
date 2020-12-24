package com.example.runningtracker;


import java.util.Date;
import java.util.Timer;
import android.util.Log;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import java.util.Calendar;
import java.util.TimerTask;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import java.text.DecimalFormat;
import android.app.PendingIntent;
import java.text.SimpleDateFormat;
import android.content.IntentFilter;
import androidx.annotation.Nullable;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.location.LocationManager;

public class AppServiceActivity extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }



} // end class