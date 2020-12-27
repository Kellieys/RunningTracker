package com.example.runningtracker;

import android.net.Uri;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.ContentProvider;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

// The purpose of this class are centralize content in one place and have many different applications access it as needed
// Also handle query, edit, add, delete and any CRUD methods
// Store in SQlite database

public class AppContentProvider extends ContentProvider {

    // Initialisation
    private AppDatabase database_handler;

    // Having an empty constructor to Avoid accidentally instantiating
    public AppContentProvider() {}

    // Called to initialize the provider
    @Override
    public boolean onCreate()
    {
        database_handler = new AppDatabase(getContext(), null, null, 1);
        return false;
    }

    // Returns data to the caller. Arguments are packed into a bundle.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selection_args, String sort_order)
    {
        // Build SQLite query
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(AppRunningTrackerContract.RunInformationTable.TABLE_RUN_INFORMATION);

        // Uri matching
        int uriType = AppRunningTrackerContract.sURIMatcher.match(uri);

        // Switch case to check for Uri
        switch(uriType){
            case AppRunningTrackerContract.INFORMATION_ID:
                queryBuilder.appendWhere(AppRunningTrackerContract.INFO_ID + '=' + uri.getLastPathSegment());
                break;
            case AppRunningTrackerContract.INFORMATION:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        // Cursor query from table and return result
        Cursor cursor = queryBuilder.query(database_handler.getReadableDatabase(), projection, selection, selection_args, null, null, sort_order);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // Not Implemented
    @Override
    public String getType( Uri uri) {
        // Not needed for this application
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        // Uri matching
        int uriType = AppRunningTrackerContract.sURIMatcher.match(uri);

        // Get databases
        SQLiteDatabase sqlDB = database_handler.getWritableDatabase();
        long id = 0;

        // Switch case to check for Uri
        // If case match , insert into database run_information table database
        switch (uriType){
            case AppRunningTrackerContract.INFORMATION:
                id = sqlDB.insert(AppRunningTrackerContract.RunInformationTable.TABLE_RUN_INFORMATION, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(AppRunningTrackerContract.TABLE_RUN_INFORMATION + "/" + id);
    }

    // Not Implemented
    public int delete(Uri uri, String s, String[] strings) {
        // Not needed for this application
        throw new UnsupportedOperationException("Not Implemented");
    }

    // Updates existing data in the content provider
    @Override
    public int update(Uri uri,ContentValues contentValues, String selection, String[] selection_args)
    {
        int uriType = AppRunningTrackerContract.sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database_handler.getWritableDatabase();

        int rowsUpdated = 0;
        switch (uriType) {
            case AppRunningTrackerContract.INFORMATION:
                rowsUpdated =
                        sqlDB.update(AppRunningTrackerContract.RunInformationTable.TABLE_RUN_INFORMATION, contentValues, selection, selection_args);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

} // end class