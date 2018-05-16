package com.tunex.mightyglobackend.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by hp on 26-Apr-18.
 */

public class DataProvider extends ContentProvider {

    public static final String LOG_TAG = DataProvider.class.getSimpleName();
    private static final int DATAS = 100;
    private static final int GLO_DATA_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_DATAS, DATAS);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_DATAS + "/#", GLO_DATA_ID);
    }


    private DataDbHelper mDbHelper;

    @Override
    public boolean onCreate() {

        mDbHelper = new DataDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case DATAS:
                cursor = database.query(Contract.DataEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case GLO_DATA_ID:
                selection = Contract.DataEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Contract.DataEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DATAS:
                return insertData(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertData(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(Contract.DataEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
