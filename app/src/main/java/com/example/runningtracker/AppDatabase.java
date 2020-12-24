package com.example.runningtracker;

import android.net.Uri;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {

    private ContentResolver content_resolver;

    // constructor
    public AppDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, AppRunningTrackerContract.DATABASE_NAME, factory, AppRunningTrackerContract.DATABASE_VERSION);
        content_resolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


} // end class
