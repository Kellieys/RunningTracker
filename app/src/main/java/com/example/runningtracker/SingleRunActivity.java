package com.example.runningtracker;

import android.view.View;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SingleRunActivity extends AppCompatActivity {

    private TextView displayDateTextView;
    private TextView displayTimeTextView;
    private TextView displayDistanceTextView;
    private EditText displayCommentTextView;
    private AppDatabase database_handler;
    private RunGetterSetter runGetterSetter;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_run);
        displayDateTextView = findViewById(R.id.display_date_textView);
        displayTimeTextView = findViewById(R.id.display_time_textView);
        displayCommentTextView = findViewById(R.id.display_comment_editText);
        displayDistanceTextView = findViewById(R.id.display_distance_textView);
        updateBtn = findViewById(R.id.single_update_button);

        Intent intent = getIntent();

        displayDateTextView.setText(intent.getStringExtra("selected date"));
        displayTimeTextView.setText(intent.getStringExtra("selected time"));
        displayDistanceTextView.setText(Float.toString(intent.getFloatExtra("selected distance", 0)) + " m");
        displayCommentTextView.setText(intent.getStringExtra("selected comment"));
    }

    public void update_comment_OnClick(View view)
    {

        runGetterSetter = new RunGetterSetter();
        database_handler = new AppDatabase(this,null, null,1);

        Intent intent = getIntent();

        // Read and GET new comment user input
        runGetterSetter.setComment(displayCommentTextView.getText().toString());
        runGetterSetter.setDistance(intent.getFloatExtra("selected distance", 0));

        // Update the comment
        boolean result = database_handler.updateComment(runGetterSetter);

        // if update success, hint user
        if(result)
        {
            Toast toast = Toast.makeText(getApplicationContext(),"Comment Updated", Toast.LENGTH_SHORT);
            toast.show();
        }

        // else, hint error
        else {
            Toast toast = Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

} // end class
