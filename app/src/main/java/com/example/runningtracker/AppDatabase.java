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
        contentValues.put(AppRunningTrackerContract.INFO_DATE, runGetterSetter.getDate());
        contentValues.put(AppRunningTrackerContract.INFO_TIME, runGetterSetter.getTime());
        contentValues.put(AppRunningTrackerContract.INFO_COMMENT, runGetterSetter.getComment());
        contentValues.put(AppRunningTrackerContract.INFO_DISTANCE, runGetterSetter.getDistance());

        // Insert to the TABLE_RUN_INFORMATION by using content resolvers
        content_resolver.insert(AppRunningTrackerContract.RUN_INFO_URI, contentValues);
    }

    public ArrayList<RunGetterSetter> runningHistoryList(String sortOrder) {
        ArrayList<RunGetterSetter> arrayList = new ArrayList<>();

        // View by Date selected
        if(sortOrder.equals("Date")) {
            Cursor cursor = content_resolver.query(AppRunningTrackerContract.RUN_INFO_URI, null, null, null, AppRunningTrackerContract.INFO_ID + " DESC");

            // move cursor to first result
            if (cursor.moveToFirst()) {
                // while cursor is not after result
                while (!cursor.isAfterLast()) {
                    RunGetterSetter runGetterSetter = new RunGetterSetter();
                    runGetterSetter.setDate(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DATE)));
                    if (runGetterSetter.getDate() == null)
                    {
                        // move cursor to next result
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
        // sort by Time
        else if (sortOrder.equals("Time"))
        {
            Cursor cursor = content_resolver.query(AppRunningTrackerContract.RUN_INFO_URI, null, null, null, null);

            // move cursor to first result
            if (cursor.moveToFirst()) {
                // while cursor is not after result
                while (!cursor.isAfterLast()) {
                    RunGetterSetter runGetterSetter = new RunGetterSetter();
                    runGetterSetter.setDate(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DATE)));
                    if (runGetterSetter.getDate() == null)
                    {
                        // move cursor to next result
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

            // bubble sort to sort the records with time taken in descending order
            for (int j = 0; j < arrayList.size(); j++)
            {
                for (int i = 1; i < arrayList.size(); i++) {
                    RunGetterSetter runGetterSetter1 = arrayList.get(i - 1);
                    RunGetterSetter runGetterSetter2 = arrayList.get(i);

                    String timeStringA[] = runGetterSetter1.getTime().split(":");
                    String timeStringB[] = runGetterSetter2.getTime().split(":");

                    int timeA = Integer.parseInt(timeStringA[2]) + (Integer.parseInt(timeStringA[1]) * 60) + (Integer.parseInt(timeStringA[0]) * 3600);
                    int timeB = Integer.parseInt(timeStringB[2]) + (Integer.parseInt(timeStringB[1]) * 60) + (Integer.parseInt(timeStringB[0]) * 3600);

                    if (timeB < timeA)
                    {
                        arrayList.set(i - 1, runGetterSetter2);
                        arrayList.set(i, runGetterSetter1);
                    }
                }
            }
        }

        // sort by distance
        else if (sortOrder.equals("Distance"))
        {
            Cursor cursor = content_resolver.query(AppRunningTrackerContract.RUN_INFO_URI, null, null, null, AppRunningTrackerContract.INFO_DISTANCE + " DESC");

            // move cursor to first result
            if (cursor.moveToFirst()) {
                // while cursor is not after result
                while (!cursor.isAfterLast()) {
                    RunGetterSetter runGetterSetter = new RunGetterSetter();
                    runGetterSetter.setDate(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DATE)));
                    if (runGetterSetter.getDate() == null)
                    {
                        // move cursor to next result
                        cursor.moveToNext();
                        continue;
                    }
                    runGetterSetter.setTime(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_TIME)));
                    runGetterSetter.setComment(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_COMMENT)));
                    runGetterSetter.setDistance(cursor.getFloat(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DISTANCE)));
                    arrayList.add(runGetterSetter);

                    // move cursor to next result
                    cursor.moveToNext();
                }
            }
        }

        return arrayList;
    }

    public ArrayList<RunGetterSetter> selectedRun(String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        ArrayList<RunGetterSetter> arrayList = new ArrayList<>();

        Cursor cursor = content_resolver.query(AppRunningTrackerContract.RUN_INFO_URI, projection, selection, selectionArgs, sortOrder);

        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                RunGetterSetter runGetterSetter = new RunGetterSetter();
                runGetterSetter.setDate(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DATE)));
                if (runGetterSetter.getDate() == null)
                {
                    // move cursor to next result
                    cursor.moveToNext();
                    continue;
                }
                runGetterSetter.setTime(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_TIME)));
                runGetterSetter.setComment(cursor.getString(cursor.getColumnIndex(AppRunningTrackerContract.INFO_COMMENT)));
                runGetterSetter.setDistance(cursor.getFloat(cursor.getColumnIndex(AppRunningTrackerContract.INFO_DISTANCE)));
                arrayList.add(runGetterSetter);

                // move cursor to next result
                cursor.moveToNext();
            }
        }

        // bubble sort to sort the records with time taken in ascending order
        for (int j = 0; j < arrayList.size(); j++)
        {
            for (int i = 1; i < arrayList.size(); i++) {
                RunGetterSetter runGetterSetter1 = arrayList.get(i - 1);
                RunGetterSetter runGetterSetter2 = arrayList.get(i);

                String timeStringA[] = runGetterSetter1.getTime().split(":");
                String timeStringB[] = runGetterSetter2.getTime().split(":");

                int timeA = Integer.parseInt(timeStringA[2]) + (Integer.parseInt(timeStringA[1]) * 60) + (Integer.parseInt(timeStringA[0]) * 3600);
                int timeB = Integer.parseInt(timeStringB[2]) + (Integer.parseInt(timeStringB[1]) * 60) + (Integer.parseInt(timeStringB[0]) * 3600);

                if (timeB > timeA)
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
        // return update recipe result
        return result;
    }



} // end class
