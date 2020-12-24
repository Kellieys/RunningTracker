package com.example.runningtracker;

import android.net.Uri;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.ContentProvider;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

//The purpose of this class is to handle the queries

public class AppContentProvider extends ContentProvider {

    private AppDatabase database_handler;

    public AppContentProvider() {}

    @Override
    public boolean onCreate()
    {
        database_handler = new AppDatabase(getContext(), null, null, 1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1)
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

        // Return cursor after executing commands
        Cursor cursor = queryBuilder.query(database_handler.getReadableDatabase(), strings, s, strings1, null, null, s1);
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
        switch (uriType){
            case AppRunningTrackerContract.INFORMATION:
                // insert into database
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

    @Override
    public int update(Uri uri,ContentValues contentValues, String selection, String[] selectionArgs)
    {
        int uriType = AppRunningTrackerContract.sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database_handler.getWritableDatabase();

        int rowsUpdated = 0;
        switch (uriType) {
            case AppRunningTrackerContract.INFORMATION:
                rowsUpdated =
                        sqlDB.update(AppRunningTrackerContract.RunInformationTable.TABLE_RUN_INFORMATION, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

} // end class