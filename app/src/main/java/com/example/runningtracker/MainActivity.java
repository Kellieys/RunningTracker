package com.example.runningtracker;

import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

// This class is the first intent that will be reflect to the UI application where user interact

public class MainActivity extends AppCompatActivity {

    private Button startTimer_button;
    private Button runHistory_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Local variable
        startTimer_button = findViewById(R.id.startTimer_button);
        runHistory_button = findViewById(R.id.runHistory_button);
    }

    // Timer button clicked function
    public void timer_onClick(View view)
    {
        Intent intent = new Intent(this, TimerActivity.class);
        Intent serviceIntent = new Intent(this, AppServiceActivity.class);

        // start activity and start service
        startActivity(intent);
        startService(serviceIntent);
    }

    // go to sorting activity
    public void runHistory_OnClick(View view)
    {
        Intent intent = new Intent(this, RunHistoryActivity.class);
        startActivity(intent);
    }

} // end class