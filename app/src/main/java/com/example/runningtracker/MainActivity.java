package com.example.runningtracker;

import java.util.Date;
import android.view.View;
import android.os.Bundle;
import java.util.Calendar;
import java.util.ArrayList;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import androidx.appcompat.app.AppCompatActivity;

// This class is the first intent that will be reflect to the UI application where user interact

public class MainActivity extends AppCompatActivity {

    private Button startTimer_button;
    private Button runHistory_button;

    private TextView displayDistanceMonth;
    private TextView displayDistanceToday;
    private TextView displayFastestToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Local variable
        startTimer_button = findViewById(R.id.startTimer_button);
        runHistory_button = findViewById(R.id.runHistory_button);

        displayDistanceMonth = findViewById(R.id.display_total_distance_month);
        displayDistanceToday = findViewById(R.id.display_total_distance_today);
        displayFastestToday = findViewById(R.id.display_fastest_today);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date datetime = Calendar.getInstance().getTime();
        ArrayList <RunGetterSetter> returnRun;
        String selection;
        String[] selectionArgs;

        AppDatabase myDBHandler = new AppDatabase(this, null, null, 1);
        selection = AppRunningTrackerContract.INFO_DATE + " LIKE ?";
        selectionArgs = new String[]{"%" + simpleDateFormat.format(datetime)};
        returnRun = myDBHandler.selectedRun(null, selection, selectionArgs, null);

        float totalDistance = 0;

        if (returnRun.size() != 0)
        {
            displayFastestToday.setText(Float.toString(returnRun.get(0).getDistance()) + "m");

            for (int i = 0; i < returnRun.size(); i++)
            {
                totalDistance = totalDistance + returnRun.get(i).getDistance();
            }
        }
        else
        {
            displayFastestToday.setText("0.00 mins");
        }

        displayDistanceToday.setText(decimalFormat.format(totalDistance) + " m");

        totalDistance = 0;
        returnRun = null;

        simpleDateFormat = new SimpleDateFormat("MMM-yyyy");
        selectionArgs = new String[]{"%" + simpleDateFormat.format(datetime)};
        returnRun = myDBHandler.selectedRun(null, selection, selectionArgs, null);

        if (returnRun.size() != 0)
        {
            for (int i = 0; i < returnRun.size(); i++)
            {
                totalDistance = totalDistance + returnRun.get(i).getDistance();
            }
        }
        else
        {
            displayDistanceMonth.setText("0.00 Meters (Month)");
        }

        displayDistanceMonth.setText(decimalFormat.format(totalDistance) + " m");
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