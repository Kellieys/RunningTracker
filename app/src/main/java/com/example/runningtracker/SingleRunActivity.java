package com.example.runningtracker;

import android.view.View;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SingleRunActivity extends AppCompatActivity {

    private TextView displayDateTextView;
    private TextView displayTimeTextView;
    private TextView displayDistanceTextView;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_run);
        displayDateTextView = findViewById(R.id.display_date_textView);
        displayTimeTextView = findViewById(R.id.display_time_textView);
        displayDistanceTextView = findViewById(R.id.display_distance_textView);
        updateBtn = findViewById(R.id.single_update_button);

        Intent intent = getIntent();

        displayDateTextView.setText(intent.getStringExtra("selected date"));
        displayTimeTextView.setText(intent.getStringExtra("selected time"));
        displayDistanceTextView.setText(Float.toString(intent.getFloatExtra("selected distance", 0)) + " m");
    }

} // end class
