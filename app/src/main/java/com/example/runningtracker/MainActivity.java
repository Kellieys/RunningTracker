package com.example.runningtracker;

import android.view.View;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Local variable
        Button startTimer_button = findViewById(R.id.startTimer_button);
        Button runHistory_button = findViewById(R.id.runHistory_button);
    }

    // Timer button clicked function
    public void timer_onClick(View view)
    {
        Intent intent = new Intent(this, TimerActivity.class);
        Intent serviceIntent = new Intent(this, ServiceActivity.class);

        // start activity and start service
        startActivity(intent);
        startService(serviceIntent);
    }

    // go to sorting activity
    public void runHistory_OnClick(View view)
    {
        Intent intent = new Intent(this, SortActivity.class);
        startActivity(intent);
    }

} // end class