package com.example.runningtracker;

import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    public TextView current_timer_textView;
    public TextView current_distance_textView;
    private Button stop_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        current_timer_textView = findViewById(R.id.current_timer_textView);
        current_distance_textView = findViewById(R.id.current_distance_textView);
        stop_button = findViewById(R.id.stop_button);
    }

    public void stop_timer_OnClick(View view)
    {
        // access service to stop the service
        Intent serviceIntent = new Intent(this, AppServiceActivity.class);
//        serviceIntent.setAction(ServiceActivity.ACTION_STOP);
        startService(serviceIntent);

        // mainly in use when user return to this activity via tapping the notification
        // go to main activity
        // because if finish is called
        // the app will exit as there is no activity in the activity stack
        // based on the android activity life cycle
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);

        finish();
    }

} // end class
