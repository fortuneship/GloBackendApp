package com.tunex.mightyglobackend.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tunex.mightyglobackend.data.Contract.DataEntry;

/**
 * Created by hp on 26-Apr-18.
 */

public class DataDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "glo_backend.db";
    private static final int DATABASE_VERSION = 1;

    public DataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + DataEntry.TABLE_NAME + "("
                + DataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataEntry.COLUMN_RECIPIENT_NUMBER + " TEXT NOT NULL, "
                + DataEntry.COLUMN_BUNDLE_VALUE + " TEXT, "
                + DataEntry.COLUMN_BUNDLE_COST + " TEXT, "
                + DataEntry.COLUMN_REQUEST_SOURCE + " TEXT, "
                + DataEntry.COLUMN_TIME_RECEIVED + " TEXT "
                + DataEntry.COLUMN_STATUS_ + " NUMERIC , "
                + DataEntry.COLUMN_TIME_DONE + " NUMERIC " + ");";
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME);

    }

}
