package com.tunex.mightyglobackend;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tunex.mightyglobackend.data.Contract;
import com.tunex.mightyglobackend.data.Contract.DataEntry;
import com.tunex.mightyglobackend.data.DataDbHelper;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    private EditText mRecipientNumber, mTimeReceived;
    private Spinner mBundleValueSpinner;
    private Spinner mBundleCostSpinner;
    private Spinner mRequestSourceSpinner;
    private Button mSubmitButton;
    private ImageView mHistoryImg;

    private String mBundleValue = DataEntry.BUNDLE_UNKNOWN;
    private String mBundleCost = DataEntry.BUNDLE_UNKOWN_PRICE;
    private String mRequestSource = DataEntry.REQUEST_SOURCE_UNKNOWN;


    /** Database helper that will provide us access to the database */
    private DataDbHelper mDbHelper;


//    private static final int DATA_LOADER = 0;
//
//    DataCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /** Initialise db helper */
        mDbHelper = new DataDbHelper(this);



        mRecipientNumber = (EditText) findViewById(R.id.recipient_number_edit_text);
        mBundleValueSpinner = (Spinner) findViewById(R.id.bundle_spinner);
        mBundleCostSpinner = (Spinner) findViewById(R.id.bundle_cost_edit_text);
        mRequestSourceSpinner = (Spinner) findViewById(R.id.request_source_spinner);
        mTimeReceived = (EditText) findViewById(R.id.time_received_edit_text);

        mBundleCostSpinner.setEnabled(false);

        // call spinner method
        setupRequestSourceSpinner();
        setupBundleValueSpinner();

        /** change button text and color when fields are not empty using text watcher*/
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String recipientNumber = mRecipientNumber.getText().toString().trim();
                String timeReceived = mTimeReceived.getText().toString().trim();
                

                validateInput(recipientNumber, timeReceived);

            }

            @Override
            public void afterTextChanged(Editable editable) {

//                if (editable.toString().trim().length() > 0){
//////
//            submitButton.setText("send data");
//            submitButton.setBackgroundColor(Color.GREEN);
//
//        }else{
//
//            submitButton.setText("submit");
//            submitButton.setBackgroundColor(Color.WHITE);
//        }
////
//

            }


        };


        // add text change listener to fields
        mRecipientNumber.addTextChangedListener(tw);
        mTimeReceived.addTextChangedListener(tw);



        mSubmitButton = (Button) findViewById(R.id.submit_button);

//        mHistoryImg = (ImageView) findViewById(R.id.view_list_img);
//        mHistoryImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
//                //Toast.makeText(MainActivity.this, "History Button Clicked",Toast.LENGTH_LONG).show();
//            }
//        });

//        mCursorAdapter = new DataCursorAdapter(this, null);
//
//        getLoaderManager().initLoader(DATA_LOADER, null, this);


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submit();
            }
        });






    }

    /** Check if fields are not empty */
    private boolean validateInput(String recipientNumber, String timeReceived){

        /** if fields are not empty change button text and color */
        if (!recipientNumber.isEmpty() && !timeReceived.isEmpty()){

            mSubmitButton.setBackgroundColor(Color.RED);
            mSubmitButton.setText("send data");

        }else{


//            submitButton.setBackgroundColor(Color.BLUE);
//            submitButton.setText("check balance");
        }

        return false;
    }



    /**
     * Setup the dropdown spinner that allows the user to select bundle value.
     */
    private void setupBundleValueSpinner() {



        ArrayAdapter requestSourceAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_bundle_value, android.R.layout.simple_spinner_item);
        requestSourceAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        ArrayAdapter bundleCostAdapter = ArrayAdapter.createFromResource(this, R.array.array_bundle_price, android.R.layout.simple_spinner_item);
        bundleCostAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBundleValueSpinner.setAdapter(requestSourceAdapter);
        mBundleCostSpinner.setAdapter(bundleCostAdapter);

        mBundleValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mBundleCostSpinner.setSelection(position);
                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {

                    if (selection.equals(getString(R.string.one_gig))) {
                        mBundleValue = DataEntry.ONE_GIG;
                        mBundleCost = DataEntry.ONE_GIG_PRICE;

                    }else if (selection.equals(getString(R.string.two_gig))){
                        mBundleValue = DataEntry.TWO_GIG;
                        mBundleCost = DataEntry.TWO_GIG_PRICE;

                    }else if (selection.equals(getString(R.string.four_point_five))){

                        mBundleValue = DataEntry.FOUR_FIVE_GIG;
                        mBundleCost = DataEntry.FOUR_FIVE_GIG_PRICE;

                    }else if (selection.equals(R.string.seven_point_two)){

                        mBundleValue = DataEntry.SEVEN_TWO_GIG;
                        mBundleCost = DataEntry.SEVEN_TWO_GIG_PRICE;

                    }else if (selection.equals(R.string.twelve_point_five)){

                        mBundleValue = DataEntry.TWELVE_FIVE_GIG;
                        mBundleCost = DataEntry.TWELVE_FIVE_GIG_PRICE;

                    }else if (selection.equals(R.string.fifteen_point_six)){

                        mBundleValue = DataEntry.FIFTEEN_SIX_GIG;
                        mBundleCost = DataEntry.FIFTEEN_SIX_GIG_PRICE;

                    }else if (selection.equals(R.string.twenty_five)){

                        mBundleValue = DataEntry.TWENTY_FIVE_GIG;
                        mBundleCost = DataEntry.TWENTY_FIVE_GIG_PRICE;

                    }else if (selection.equals(R.string.twelve_point_five_mb)){

                        mBundleValue = DataEntry.TWELVE_FIVE_MB;
                        mBundleCost = DataEntry.TWELVE_FIVE_MB_PRICE;

                    }else {

                        mBundleValue = DataEntry.BUNDLE_UNKNOWN;
                        mBundleCost = DataEntry.BUNDLE_UNKOWN_PRICE;
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mBundleValue = DataEntry.BUNDLE_UNKNOWN;
                mBundleCost = DataEntry.BUNDLE_UNKOWN_PRICE;
            }
        });


    }




    /**
     * Setup the dropdown spinner that allows the user to select data source.
     */
    private void setupRequestSourceSpinner() {

        ArrayAdapter requestSourceAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_request_source, android.R.layout.simple_spinner_item);

        requestSourceAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mRequestSourceSpinner.setAdapter(requestSourceAdapter);

        mRequestSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {

                    if (selection.equals(getString(R.string.source_airtime))) {
                        mRequestSource = DataEntry.REQUEST_SOURCE_AIRTIME;


                    }else if (selection.equals(getString(R.string.source_cash))){

                        mRequestSource = DataEntry.REQUEST_SOURCE_CASH;

                    }else if (selection.equals(getString(R.string.source_agent))){

                        mRequestSource = DataEntry.REQUEST_SOURCE_AGENT;

                    }else if (selection.equals(R.string.source_sales_rep)){

                        mRequestSource = DataEntry.REQUEST_SOURCE_SALES_REP;

                    }else if (selection.equals(R.string.source_web)){

                        mRequestSource = DataEntry.REQUEST_SOURCE_WEB;

                    }else if (selection.equals(R.string.source_api)){

                        mRequestSource = DataEntry.REQUEST_SOURCE_API;

                    }else {

                        mRequestSource = DataEntry.REQUEST_SOURCE_UNKNOWN;
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mRequestSource = DataEntry.REQUEST_SOURCE_UNKNOWN;
            }
        });
    }


    private void submit(){


        String recipientNumber = mRecipientNumber.getText().toString().trim();
       // bundleCost = mBundleCost.getText().toString().trim();
        String timeReceived = mTimeReceived.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(DataEntry.COLUMN_RECIPIENT_NUMBER, recipientNumber);
        values.put(DataEntry.COLUMN_BUNDLE_VALUE, mBundleValue);
        values.put(DataEntry.COLUMN_BUNDLE_COST, mBundleCost);
        values.put(DataEntry.COLUMN_REQUEST_SOURCE, mRequestSource);
        values.put(DataEntry.COLUMN_TIME_RECEIVED, timeReceived);


        Uri uri = getContentResolver().insert(DataEntry.CONTENT_URI, values);

        if (uri == null) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            Log.i("info:", "error saving to db");
        } else {
            Toast.makeText(this, getString(R.string.form_save), Toast.LENGTH_SHORT).show();

            Log.i("info:", "save to db is successful");
        }





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                //insertPet();
                startActivity(new Intent(this, HistoryActivity.class));

                return true;
            case R.id.action_delete_all_entries:
                //deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle args) {
//
//        String[] projection = {
//                DataEntry._ID,
//                DataEntry.COLUMN_RECIPIENT_NUMBER,
//                DataEntry.COLUMN_BUNDLE_VALUE,
//                DataEntry.COLUMN_BUNDLE_COST};
//
//        return new CursorLoader(this,
//                DataEntry.CONTENT_URI,
//                projection,
//                null,
//                null,
//                null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//
//        mCursorAdapter.swapCursor(data);
//
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//        mCursorAdapter.swapCursor(null);
//
//    }
}
