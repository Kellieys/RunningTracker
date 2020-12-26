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
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;
import androidx.core.app.NotificationManagerCompat;
import android.support.v4.media.session.PlaybackStateCompat;


// REFERENCE
// https://www.javatpoint.com/android-service-tutorial#:~:text=Android%20service%20is%20a%20component,even%20if%20application%20is%20destroyed.&text=app.

// This class will be the service implementation class by inheriting the Service class and overridding its callback methods
// Perform operations on the background for listening location, broadcast receiver and handle notification

public class AppServiceActivity extends Service {

    private static DecimalFormat time_format;
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String channelID = "channelID";
    private LocationManager locationManager;
    private AppLocationListener locationListener;
    private RunGetterSetter runGetterSetter;
    private Date startDateTime;
    private AppBroadcastReceiver broadcastReceiver;
    private static Handler handler;
    private static int elapsedTime;
    private static Timer timer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    // initializing components
    // create new broadcast recevier
    // create notification channel for API > 28 only
    @Override
    public void onCreate()
    {
        time_format = new DecimalFormat("0.00");
        broadcastReceiver = new AppBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        registerReceiver(broadcastReceiver, filter);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // get action
        String action = intent.getAction();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");

        // for intent that wants to stop the service
        // will have an action called ACTION_STOP
        // else prepare to start foreground
        if (action == ACTION_STOP)
        {
            runGetterSetter.setDate(simpleTimeFormat.format(startDateTime) + "  " + simpleDateFormat.format(startDateTime));
            runGetterSetter.setTime(TimerActivity.current_timer_textView.getText().toString());
            runGetterSetter.setDistance(Float.parseFloat(time_format.format(AppLocationListener.distance)));
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            locationListener = null;
            AppDatabase mydbhandler = new AppDatabase(this, null ,null, 1);
            mydbhandler.add_run(runGetterSetter);
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
        }
        else
        {
            // new record object
            runGetterSetter = new RunGetterSetter();
            // get current datetime
            startDateTime = Calendar.getInstance().getTime();
            // create notification
            createNotification();
            // initial time = 0
            elapsedTime = 0;
            // timer to record the duration of each logging session
            timer = new Timer();
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    int hour = (int) elapsedTime / 3600;
                    int minutes = (int) elapsedTime / 60;
                    int seconds = (int) elapsedTime % 60;
                    // update textview
                    TimerActivity.current_timer_textView.setText(hour + ":" + minutes + ":" + seconds);
                }
            };
            // start time
            startTimer();

            try
            {
                // start location tracking
                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                locationListener = new AppLocationListener();
                // this is an android problem
                // the gps will keep on update even though it has been terminated
                // assigning this variable to null is required for the algorithm to run
                // more details please check the comments in MyLocationListener
                locationListener.currentLocation = null;
                // request for location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            }
            catch (SecurityException e)
            {
                Log.d("SecurityException", e.toString());
            }
        }

        return START_STICKY;
    }

    // create notification
    public void createNotification()
    {
        // intent for tapping the notification
        Intent notificationIntent = new Intent(this, TimerActivity.class);
        notificationIntent.putExtra("source", "from notification");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // intent for stop button in notification
        Intent stopIntent = new Intent(this, AppBroadcastReceiver.class);

        // create pending intent based on the intent above
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this , 0 , notificationIntent, 0);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);

        // build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this , channelID);
        builder.setContentTitle("Running Tracker is logging. Tap to see more")
                .setContentText("Tap the right hand side button to stop logging")
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .addAction(android.R.drawable.checkbox_off_background, "Stop Logging", stopPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_STOP)))
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_STOP));

        // notification manager
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
        // start foreground service
        startForeground(1, builder.build());
    }

    public void createNotificationChannel()
    {
        // only applicable to API > 28
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Running Tracker";
            String description = "Running Tracker";
            NotificationChannel channel = new NotificationChannel(channelID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // start timer
    protected static void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime += 1; //increase every second
                handler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }


} // end class