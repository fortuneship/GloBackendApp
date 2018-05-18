package com.tunex.mightyglobackend;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.tunex.mightyglobackend.data.Contract.DataEntry;

public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DATA_LOADER = 0;

    DataCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ListView listView = (ListView) findViewById(R.id.listView);

//        mCursorAdapter = new DataCursorAdapter(this, null);
//
//        listView.setAdapter(mCursorAdapter);

        mCursorAdapter = new DataCursorAdapter(this, null);

        listView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(DATA_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle args) {


        String[] projection = {
                DataEntry._ID,
                DataEntry.COLUMN_RECIPIENT_NUMBER,
                DataEntry.COLUMN_BUNDLE_VALUE,
                DataEntry.COLUMN_BUNDLE_COST,
                DataEntry.COLUMN_REQUEST_SOURCE,
                DataEntry.COLUMN_TIME_RECEIVED,
                DataEntry.COLUMN_TIME_DONE};

        return new CursorLoader(this,
                DataEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);


    }
}
