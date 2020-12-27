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

// OFFICIAL DOCUMENTATION: https://developer.android.com/reference/java/util/Timer
// https://developer.android.com/training/notify-user/build-notification
// REFERENCES (Service): https://www.javatpoint.com/android-service-tutorial#:~:text=Android%20service%20is%20a%20component,even%20if%20application%20is%20destroyed.&text=app.
// REFERENCES (Timer): https://stackoverflow.com/questions/4597690/how-to-set-timer-in-android
// This class will be the service implementation class by inheriting the Service class and override its callback methods
// Perform operations on the background for listening location, broadcast receiver and handle notification

public class AppServiceActivity extends Service {

    private static Timer timer;
    private Date dateTime;
    private static Handler handler;
    private static int elapsedTime;
    private RunGetterSetter runGetterSetter;
    private LocationManager locationManager;
    private String channel_id = "channel_id";
    private static DecimalFormat decimalFormat;
    private AppLocationListener locationListener;
    private AppBroadcastReceiver broadcastReceiver;
    public static final String ACTION_STOP = "ACTION_STOP";

    // Not implemented
    // Allows other applications to bind to it and interact with it
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    // Called when it is created (by starting it or binding to it)
    // Initialise object for DecimalFormat, AppBroadcastReceiver, IntentFilter
    @Override
    public void onCreate()
    {
        decimalFormat = new DecimalFormat("0.00");
        broadcastReceiver = new AppBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        registerReceiver(broadcastReceiver, filter);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Returns a pointer id and an event (i.e., up, down, move) information
        String action = intent.getAction();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");

        // ACTION_STOP is to handle the event to stop, in this case, stop timer
        if (action == ACTION_STOP)
        {
            runGetterSetter.setDate(simpleTimeFormat.format(dateTime) + "  " + simpleDateFormat.format(dateTime));
            runGetterSetter.setTime(TimerActivity.current_timer_textView.getText().toString());
            runGetterSetter.setDistance(Float.parseFloat(decimalFormat.format(AppLocationListener.distance)));

            // Remove location update
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            locationListener = null;

            // Reflect changes to database
            AppDatabase database_handler = new AppDatabase(this, null ,null, 1);
            database_handler.add_run(runGetterSetter);
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
        }

        // Else start foreground
        else
        {
            // Initialise run object
            runGetterSetter = new RunGetterSetter();

            // Get current time of the android system
            dateTime = Calendar.getInstance().getTime();

            // Called function to create a notification
            createNotification();

            // The initial time will be 0 and elapsed time display the time past
            elapsedTime = 0;

            // Creates a new timer
            timer = new Timer();
            handler = new Handler() {
                public void handleMessage(Message message) {
                    int hour = (int) elapsedTime / 3600;
                    int minutes = (int) elapsedTime / 60;
                    int seconds = (int) elapsedTime % 60;

                    // Reflect to Timer Activity
                    TimerActivity.current_timer_textView.setText(hour + ":" + minutes + ":" + seconds);
                }
            };

            // Start the timer by calling startTimer()
            startTimer();

            // Error handling
            try
            {
                // Location tracking
                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                locationListener = new AppLocationListener();
                // After onLocationChange executed, always set current location to null before next execution
                locationListener.currentLocation = null;
                // Request for location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            }
            catch (SecurityException error)
            {
                Log.d("SecurityException", error.toString());
            }
        }

        return START_STICKY;
    }

    // Function that handle the time by increasing in seconds
    protected static void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime += 1;
                handler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }

    // Function to create a notification
    public void createNotification()
    {
        // Set special flags controlling how notificationIntent intent is handled
        Intent notificationIntent = new Intent(this, TimerActivity.class);
        notificationIntent.putExtra("source", "from notification");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Handle intent of notification's stop button
        Intent stopIntent = new Intent(this, AppBroadcastReceiver.class);

        // PendingIntent to give a token that to a foreign application e.g. NotificationManager
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this , 0 , notificationIntent, 0);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);

        //  Set the notification's content and channel using a NotificationCompat.Builder object
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this , channel_id);
        builder.setContentTitle("Running Tracker is tracking.")
                .setContentText("Tap to see more.")
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .addAction(android.R.drawable.checkbox_off_background, "Stop Logging", stopPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_STOP)))
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_STOP));

        // Call methods to post and start foreground service
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
        startForeground(1, builder.build());
    }

    public void createNotificationChannel()
    {
        // Only runs on API > 28
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Running Tracker";
            String description = "Running Tracker";
            NotificationChannel channel = new NotificationChannel(channel_id, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            // Return the handle to a system-level service by class
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

} // end class