package com.example.runningtracker;

import android.view.View;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.Button;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class RunHistoryActivity extends AppCompatActivity {

    private Spinner dropdown;
    private Button selectBtn;
    // Dropdown string array for user selection
    private String[] dropdownList = {"Distance", "Date", "Time"};

    private ListView allRunListView;
    private AppDatabase database_handler;
    private ArrayList<RunGetterSetter> allRun;
    private String sort_order;
    private ArrayList<String> runInfo;
    private String selectedRun;
    private String[] singleRun;
    private String selectedDate;
    private String selectedTime;
    private float selectedDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_history);
        dropdown = findViewById(R.id.dropdown);
        selectBtn = findViewById(R.id.selectBtn);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dropdownList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);

        allRunListView = findViewById(R.id.allRunListView);
        database_handler = new AppDatabase(this, null, null, 1);

    }

    // OnClick get value and call display list function
    public void select_OnClick(View view)
    {
        sort_order = dropdown.getSelectedItem().toString();
        displayRunList();
    }

    // Handle the listView
    public void displayRunList()
    {
        // Find all run according to the input(sort_order)
        allRun = database_handler.runningHistoryList(sort_order);
        runInfo = new ArrayList<String>();

        // Display information
        for (int i = 0; i < allRun.size(); i++)
        {
            RunGetterSetter runGetterSetter = allRun.get(i);
            runInfo.add("Distance:" + Float.toString(runGetterSetter.getDistance()) + " m"+ " Time Taken:" + runGetterSetter.getTime() + " DateTime:" +  runGetterSetter.getDate());
        }

        // ListView initialisation
        allRunListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, runInfo));

        // Via onClick list item, handle the details of that single run
        allRunListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedRun = allRunListView.getItemAtPosition(position).toString();
                singleRun = selectedRun.split("    ");
                selectedDate = singleRun[0].split(": ")[1];
                selectedTime = singleRun[1].split(": ")[1];
                selectedDistance = Float.parseFloat(singleRun[2].split(" ")[1]);
                requestSingleRunActivity();
            }
        });
    }

    public void requestSingleRunActivity()
    {
        // put all the selected record data into the intent and start next activity
        Intent intent = new Intent(this, SingleRunActivity.class);
        intent.putExtra("date data", singleRun[0]);
        intent.putExtra("selected date", selectedDate);
        intent.putExtra("selected time", selectedTime);
        intent.putExtra("selected distance", selectedDistance);
        startActivity(intent);
    }


} // end class
