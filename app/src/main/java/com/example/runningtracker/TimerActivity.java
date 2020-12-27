package com.example.runningtracker;

import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// This class is the intent after the user start the timer, time and distance will be reflect in this intent

public class TimerActivity extends AppCompatActivity {

    public static TextView current_timer_textView;
    public static TextView current_distance_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        current_timer_textView = findViewById(R.id.current_timer_textView);
        current_distance_textView = findViewById(R.id.current_distance_textView);
        Button stop_button = findViewById(R.id.stop_button);
    }

    public void stop_timer_OnClick(View view)
    {
        // The service activity class is called to post the STOP action
        Intent appService = new Intent(this, AppServiceActivity.class);
        appService.setAction(AppServiceActivity.ACTION_STOP);
        startService(appService);

        // Return to the main activity, first intent of the app
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);

        // Dismiss any dialogs the activity was managing
        finish();
    }

} // end class
