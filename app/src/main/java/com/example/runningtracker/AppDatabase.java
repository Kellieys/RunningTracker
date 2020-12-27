package com.example.runningtracker;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// OFFICIAL DOCUMENTATION https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper
// This class manage database creation and version management;purpose as a create,read,update,delete function handler for database
// In this project, it will be focus on create, read, and update

public class AppDatabase extends SQLiteOpenHelper {

    private ContentResolver content_resolver;

    // constructor
    public AppDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
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
        // Create contentValues that will be used to store run information
        ContentValues contentValues = new ContentValues();
        contentValues.put(AppRunningTrackerContract.INFO_DATE, runGetterSetter.getDate());
        contentValues.put(AppRunningTrackerContract.INFO_TIME, runGetterSetter.getTime());
        contentValues.put(AppRunningTrackerContract.INFO_COMMENT, runGetterSetter.getComment());
        contentValues.put(AppRunningTrackerContract.INFO_DISTANCE, runGetterSetter.getDistance());

        // Insert to the TABLE_RUN_INFORMATION by using content resolvers
        content_resolver.insert(AppRunningTrackerContract.RUN_INFO_URI, contentValues);
    }

    // Handle the option selected by user
    public ArrayList<RunGetterSetter> runningHistoryList(String sort_order)
    {
        ArrayList<RunGetterSetter> arrayList = new ArrayList<>();

        // View by Date selected
        if(sort_order.equals("Date")) {
            Cursor cursor = content_resolver.query(AppRunningTrackerContract.RUN_INFO_URI, null, null, null, AppRunningTrackerContract.INFO_ID + " DESC");

            // Moves the cursor to the first result when the set is not empty
            if (cursor.moveToFirst()) {

                // While loop until the cursor to all run info
                while (!cursor.isAfterLast()) {
                    RunGetterSetter runGetterSetter = new RunGetterSetter();
                    runGetterSetter.setDate(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DATE)));
                    if (runGetterSetter.getDate() == null)
                    {
                        // Moves the cursor to the next result
                        cursor.moveToNext();
                        continue;
                    }
                    runGetterSetter.setTime(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_TIME)));
                    runGetterSetter.setComment(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_COMMENT)));
                    runGetterSetter.setDistance(cursor.getFloat(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DISTANCE)));
                    arrayList.add(runGetterSetter);

                    // Cursor continue to next
                    cursor.moveToNext();
                }
            }
        }

        // View by Time selected
        else if (sort_order.equals("Time"))
        {
            Cursor cursor = content_resolver.query(AppRunningTrackerContract.RUN_INFO_URI, null, null, null, null);

            // Moves the cursor to the first result when the set is not empty
            if (cursor.moveToFirst()) {

                // While loop until the cursor to all run info
                while (!cursor.isAfterLast()) {
                    RunGetterSetter runGetterSetter = new RunGetterSetter();
                    runGetterSetter.setDate(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DATE)));
                    if (runGetterSetter.getDate() == null)
                    {
                        // Moves the cursor to the next result
                        cursor.moveToNext();
                        continue;
                    }
                    runGetterSetter.setTime(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_TIME)));
                    runGetterSetter.setComment(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_COMMENT)));
                    runGetterSetter.setDistance(cursor.getFloat(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DISTANCE)));
                    arrayList.add(runGetterSetter);

                    // Cursor continue to next
                    cursor.moveToNext();
                }
            }

            // BUBBLE SORT in JAVA: https://beginnersbook.com/2014/07/java-program-for-bubble-sort-in-ascending-descending-order/
            // Descending order by time
            for (int i = 0; i < arrayList.size(); i++) {
                for (int j = 1; j < arrayList.size(); j++) {
                    RunGetterSetter runGetterSetter1 = arrayList.get(j - 1);
                    RunGetterSetter runGetterSetter2 = arrayList.get(j);

                    String time1[] = runGetterSetter1.getTime().split(":");
                    String time2[] = runGetterSetter2.getTime().split(":");

                    // Convert to same unit for comparison purpose
                    int new_time1 = Integer.parseInt(time1[2]) + (Integer.parseInt(time1[1]) * 60) + (Integer.parseInt(time1[0]) * 3600);
                    int new_time2 = Integer.parseInt(time2[2]) + (Integer.parseInt(time2[1]) * 60) + (Integer.parseInt(time2[0]) * 3600);

                    // If smaller, then swap place to back
                    if (new_time2 < new_time1)
                    {
                        arrayList.set(j - 1, runGetterSetter2);
                        arrayList.set(j, runGetterSetter1);
                    }
                } }
        }

        // View by Distance selected
        else if (sort_order.equals("Distance"))
        {
            Cursor cursor = content_resolver.query(AppRunningTrackerContract.RUN_INFO_URI, null, null, null, AppRunningTrackerContract.INFO_DISTANCE + " DESC");

            // Moves the cursor to the first result when the set is not empty
            if (cursor.moveToFirst()) {

                // While loop until the cursor to all run info
                while (!cursor.isAfterLast()) {
                    RunGetterSetter runGetterSetter = new RunGetterSetter();
                    runGetterSetter.setDate(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DATE)));
                    if (runGetterSetter.getDate() == null)
                    {
                        // Moves the cursor to the next result
                        cursor.moveToNext();
                        continue;
                    }
                    runGetterSetter.setTime(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_TIME)));
                    runGetterSetter.setComment(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_COMMENT)));
                    runGetterSetter.setDistance(cursor.getFloat(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DISTANCE)));
                    arrayList.add(runGetterSetter);

                    // Cursor continue to next
                    cursor.moveToNext();
                }
            }
        }

        return arrayList;
    }

    public ArrayList<RunGetterSetter> selectedRun(String[] projection, String selection, String[] selectionArgs, String sort_order)
    {
        ArrayList<RunGetterSetter> arrayList = new ArrayList<>();

        Cursor cursor = content_resolver.query(AppRunningTrackerContract.RUN_INFO_URI, projection, selection, selectionArgs, sort_order);

        // Moves the cursor to the first result when the set is not empty
        if (cursor.moveToFirst())
        {
            // While loop until the cursor to all run info
            while (!cursor.isAfterLast())
            {
                RunGetterSetter runGetterSetter = new RunGetterSetter();
                runGetterSetter.setDate(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DATE)));
                if (runGetterSetter.getDate() == null)
                {
                    // Moves the cursor to the next result
                    cursor.moveToNext();
                    continue;
                }
                runGetterSetter.setTime(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_TIME)));
                runGetterSetter.setComment(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_COMMENT)));
                runGetterSetter.setDistance(cursor.getFloat(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DISTANCE)));
                arrayList.add(runGetterSetter);

                // Cursor continue to next
                cursor.moveToNext();
            }
        }

        // BUBBLE SORT in JAVA: https://beginnersbook.com/2014/07/java-program-for-bubble-sort-in-ascending-descending-order/
        // Ascending order by time
        for (int i = 0; i < arrayList.size(); i++)
        {
            for (int j = 1; j < arrayList.size(); j++) {
                RunGetterSetter runGetterSetter1 = arrayList.get(j - 1);
                RunGetterSetter runGetterSetter2 = arrayList.get(j);

                String time1[] = runGetterSetter1.getTime().split(":");
                String time2[] = runGetterSetter2.getTime().split(":");

                int new_time1 = Integer.parseInt(time1[2]) + (Integer.parseInt(time1[1]) * 60) + (Integer.parseInt(time1[0]) * 3600);
                int new_time2 = Integer.parseInt(time2[2]) + (Integer.parseInt(time2[1]) * 60) + (Integer.parseInt(time2[0]) * 3600);

                // If larger, then swap place to back
                if (new_time2 > new_time1)
                {
                    arrayList.set(i - 1, runGetterSetter2);
                    arrayList.set(i, runGetterSetter1);
                }
            }
        }

        return arrayList;
    }

    // Update function for comment when the user annotate a run
    public boolean updateComment(RunGetterSetter comment)
    {
        // Create contentValues that will be used to store input
        ContentValues contentValues = new ContentValues();

        contentValues.put(AppRunningTrackerContract.INFO_COMMENT, comment.getComment());

        boolean result = false;

        String selection = "\""+ AppRunningTrackerContract.INFO_DISTANCE + "\"=\"" + comment.getDistance() + "\"";

        int rowsUpdated = content_resolver.update(AppRunningTrackerContract.RUN_INFO_URI, contentValues, selection, null);

        if(rowsUpdated > 0)
        {
            result = true;
        }
        // return update comment result
        return result;
    }

} // end class