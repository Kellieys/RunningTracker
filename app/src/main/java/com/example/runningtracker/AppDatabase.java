package com.example.runningtracker;

import android.net.Uri;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//This class purpose as a create,read,update,delete function handler for database
//In this project, it will be focus on create, read, and update

public class AppDatabase extends SQLiteOpenHelper {

    private ContentResolver content_resolver;

    // constructor
    public AppDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, AppRunningTrackerContract.DATABASE_NAME, factory, AppRunningTrackerContract.DATABASE_VERSION);
        content_resolver = context.getContentResolver();
    }

    // Function that create the table
    // CREATE_RUN_INFORMATION_TABLE = "CREATE TABLE "
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(AppRunningTrackerContract.RunInformationTable.CREATE_RUN_INFORMATION_TABLE);
    }

    // Not implemented at this stage
    // Drop correspond table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL(AppRunningTrackerContract.RunInformationTable.DELETE_TABLE);
    }

    // Add run Function for Timer activity
    public void add_run(RunGetterSetter runGetterSetter)
    {
        // put record contents into contentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put(AppRunningTrackerContract.INFO_DATE, runGetterSetter.get_date());
        contentValues.put(AppRunningTrackerContract.INFO_TIME, runGetterSetter.get_time());
        contentValues.put(AppRunningTrackerContract.INFO_DISTANCE, runGetterSetter.get_distance());

        // Insert to the TABLE_RUN_INFORMATION by using content resolvers
        content_resolver.insert(AppRunningTrackerContract.RUN_INFO_URI, contentValues);
    }



} // end class
