package com.example.runningtracker;

//References on android database
//https://developer.android.com/reference/android/database/sqlite/package-summary
//https://stackoverflow.com/questions/16890774/android-sqlite-database-and-app-update

import android.net.Uri;
import android.content.UriMatcher;
import android.provider.BaseColumns;

//This class will be responsible for initializing the constant and
//creating table for 1.recipes, 2.ingredients and 3. recipe_ingredients

public class AppRunningTrackerContract {

    //constant for database information
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "runningtracker.db";

    //constant for table information corresponding to its name
    public static final String TABLE_RUN_INFORMATION = "run_information";

    //Replace in my content provider = AppContentProvider
    public static final String AUTHORITY = "com.example.runningtracker.AppContentProvider";
    //public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    //Put in constant defined for table information into uri to identify each correspond location
    public static final Uri RUN_INFO_URI =  Uri.parse("content://" + AUTHORITY + "/" + TABLE_RUN_INFORMATION);

    //Assign a number as identification purpose
    public static final int INFORMATION = 1;
    public static final int INFORMATION_ID = 2;

    //field names for run_information table
    public static final String INFO_ID = "id";
    public static final String INFO_DATE = "date";
    public static final String INFO_TIME = "time";
    public static final String INFO_DISTANCE = "distance";

    public static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, TABLE_RUN_INFORMATION, INFORMATION);
        sURIMatcher.addURI(AUTHORITY, TABLE_RUN_INFORMATION + "/#", INFORMATION_ID);
    }

    // Having an empty constructor to Avoid accidentally instantiating the contract class.
    private AppRunningTrackerContract() {}

    // function to create table for run_information
    public static abstract class RunInformationTable implements BaseColumns {
        public static final String TABLE_RUN_INFORMATION = "run_information";
        public static final String CREATE_RUN_INFORMATION_TABLE = "CREATE TABLE " +
                TABLE_RUN_INFORMATION + "("
                + INFO_ID + " INTEGER PRIMARY KEY, "
                + INFO_DATE
                + " TEXT, " + INFO_TIME + " TEXT, "
                + INFO_DISTANCE + " REAL" + ")";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_RUN_INFORMATION;
    }

} // end class
