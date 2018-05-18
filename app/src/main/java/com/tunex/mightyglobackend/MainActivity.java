package com.tunex.mightyglobackend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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
import com.tunex.mightyglobackend.task.SendMail;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity  {

    private EditText mRecipientNumber, mTimeReceived;
    private Spinner mBundleValueSpinner;
    private Spinner mBundleCostSpinner;
    private Spinner mRequestSourceSpinner;
    private Button mSubmitButton;
    private Button mAirtimeButton;
    private ImageView mHistoryImg;

    private String mBundleValue = DataEntry.BUNDLE_UNKNOWN;
    private String mBundleCost = DataEntry.BUNDLE_UNKOWN_PRICE;
    private String mRequestSource = DataEntry.REQUEST_SOURCE_UNKNOWN;

    String recipientNumber;
    String timeReceived;

    private TextView mAirtimeBalance;
    private TextView mCurrentTime;


    //Notification id
    private static final int uniqueID = 1000;


    private Receiver mReceiver;

    // result from accessibility service
    String mText;
    String currentTime;

    Handler h;

    /** Database helper that will provide us access to the database */
    private DataDbHelper mDbHelper;


//    private static final int DATA_LOADER = 0;
//
//    DataCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(Receiver.ACTION_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new Receiver();
        registerReceiver(mReceiver, filter);

        /** Initialise db helper */
        mDbHelper = new DataDbHelper(this);



        mRecipientNumber = (EditText) findViewById(R.id.recipient_number_edit_text);
        mBundleValueSpinner = (Spinner) findViewById(R.id.bundle_spinner);
        mBundleCostSpinner = (Spinner) findViewById(R.id.bundle_cost_edit_text);
        mRequestSourceSpinner = (Spinner) findViewById(R.id.request_source_spinner);
        mTimeReceived = (EditText) findViewById(R.id.time_received_edit_text);

        // set time from editText
       // new SetTime(mTimeReceived);

//        textView.setTextColor(ContextCompat.getColor(this,R.color.TextColor));

        mBundleCostSpinner.setEnabled(false);

        mAirtimeBalance = (TextView) findViewById(R.id.airtime_label);
        mCurrentTime = (TextView) findViewById(R.id.current_time);

        /**
         * Save current airtime balance and current time received from onReceiver
         */
        SharedPreferences sharedPref = getSharedPreferences("com.tunex.mightyglobackend", Context.MODE_PRIVATE);

        String CURRENT_BALANCE = sharedPref.getString("airtimeBalance","");
        String CURRENT_TIME = sharedPref.getString("currentTime","");
        mAirtimeBalance.setText(CURRENT_BALANCE);
        mCurrentTime.setText(CURRENT_TIME);




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

                 recipientNumber = mRecipientNumber.getText().toString().trim();
                 timeReceived = mTimeReceived.getText().toString().trim();
                

                validateInput(recipientNumber, timeReceived);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        };


        // add text change listener to fields
        mRecipientNumber.addTextChangedListener(tw);
        mTimeReceived.addTextChangedListener(tw);



        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mAirtimeButton = (Button) findViewById(R.id.airtime_button);

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
               // sendGloData();
            }
        });

        // check airtime balance
        mAirtimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // checkBalance();
            }
        });


        h = new Handler();
        new Thread(new Runnable() {
            public void run(){
                while(true){
                    try{
                        h.post(new Runnable(){
                                   public void run(){
                                     checkBalance();
                                   }
                               });
                                TimeUnit.MINUTES.sleep(2000);
                    }
                    catch(Exception ex){
                    }
                }
            }
        }).start();


    }

    /**
     * Method to check airtime balance
     */
    private void checkBalance() {


        String ussdCode = "%23" + DataEntry.GLO_CHECK_AIRTIME + Uri.encode("#");
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));

    }


    /**
     * Setup the dropdown spinner that allows the user to select bundle value and corresponding bundle cost.
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

    /** Check if fields are not empty */
    private boolean validateInput(String recipientNumber, String timeReceived){

        /** if fields are not empty change button text and color */
        if (!recipientNumber.isEmpty() && !timeReceived.isEmpty()){

            mSubmitButton.setText(getString(R.string.hint_button_text));

        }else{

            //return button to initial state
            //mSubmitButton.setBackgroundResource(android.R.drawable.btn_default);
            mSubmitButton.setText(getString(R.string.action_submit));

//            submitButton.setBackgroundColor(Color.BLUE);
//            submitButton.setText("check balance");
        }

        return false;
    }

    /** Send glo data to recipient method */
    private void sendGloData() {



        if (mBundleValue.equals(getString(R.string.one_gig))){

            String ussdCode = DataEntry.CODE_ONE_GIG + recipientNumber + Uri.encode("#");
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));

        }else if (mBundleValue.equals(getString(R.string.two_gig))){

            String ussdCode = DataEntry.CODE_TWO_GIG + recipientNumber + Uri.encode("#");
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));

        }else if (mBundleValue.equals(getString(R.string.four_point_five))){

            String ussdCode = DataEntry.CODE_FOUR_FIVE_GIG + recipientNumber + Uri.encode("#");
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));


        }else if (mBundleValue.equals(getString(R.string.seven_point_two))){

            String ussdCode = DataEntry.CODE_SEVEN_TWO_GIG + recipientNumber + Uri.encode("#");
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));


        }else if (mBundleValue.equals(getString(R.string.twelve_point_five))){

            String ussdCode = DataEntry.CODE_TWELVE_FIVE_GIG + recipientNumber + Uri.encode("#");
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));


        }else if (mBundleValue.equals(getString(R.string.fifteen_point_six))){

            String ussdCode = DataEntry.CODE_FIFTEEN_SIX_GIG + recipientNumber + Uri.encode("#");
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));

        }else if (mBundleValue.equals(getString(R.string.twenty_five))){

            String ussdCode = DataEntry.CODE_TWENTY_FIVE_GIG + recipientNumber + Uri.encode("#");
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));

        }else{

            String ussdCode = DataEntry.CODE_TWELVE_FIVE_MB + recipientNumber + Uri.encode("#");
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));

        }


    }


    /** submit and save to database */
    private void submit(){

        /** if button text is "send data" send glo data to recipient number and save to database */
        if (mSubmitButton.getText().equals(getString(R.string.hint_button_text))) {

            sendGloData();

        }else{

            Toast.makeText(this, "Fields are empty", Toast.LENGTH_LONG).show();

        }



//        String recipientNumber = mRecipientNumber.getText().toString().trim();
//        String timeReceived = mTimeReceived.getText().toString().trim();

    }


    /** Receive response from accessibility service and display in activity */
    public class Receiver extends BroadcastReceiver {
        public static final String ACTION_RESPONSE = "com.example.mighty5.mightydata.android.intent.action.CALL";

        @Override
        public void onReceive(Context context, Intent intent) {
            

            if(intent.getAction().equals(MainActivity.Receiver.ACTION_RESPONSE)){

                mText = intent.getStringExtra("result");
                currentTime = intent.getStringExtra("current time");


                String balance = mText;
                Pattern p = Pattern.compile(":(.*?)G");

                Matcher m = p.matcher(balance);

                if (m.find()) {

                    //Toast.makeText(this, "Your last balance is " + m.group(1), Toast.LENGTH_SHORT).show();

                    String mBalance = "Your last balance is " + m.group(1);

                    //Saved response received from broadcast
                    SharedPreferences sharedPref = getSharedPreferences("com.tunex.mightyglobackend", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("airtimeBalance", mBalance);
                    editor.putString("currentTime", currentTime);
                    editor.apply();

                    mAirtimeBalance.setText(mBalance);
                    mCurrentTime.setText(currentTime);

                }else{

                    mAirtimeBalance.setText(mText);
                    mCurrentTime.setText(currentTime);


                    saveToDb();

                    showNotification();

                    showDialog();

                    sendEmail();
                }

            }



//            Log.i("ussdResult", mText);
//
//
////            String reg =  "Sorry, you are not gifting to valid Globacom user.";
//
//            String balance = mText;
//
//            Pattern p = Pattern.compile(":(.*?)G");
//
//
//            Matcher m = p.matcher(balance);
//
//            if (m.find()){
//
//
//               // status = "success";
//
//
//                //Toast.makeText(SendDataActivity.this, "Your last balance is "+ m.group(1), Toast.LENGTH_SHORT).show();
//
//            }else {
//
//                //Toast.makeText(getApplicationContext(), "Request not successful please try again", Toast.LENGTH_SHORT).show();
//
//                //status = "failed";
//                showNotification();

//                showDialog();

//                sendEmail();
//
//
//            }
//
//
        }
    }

    private void showDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder.setMessage("Sending data failed,Please try again")

                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private void sendEmail(){

        //String SLACK_URL = "https://hooks.slack.com/services/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";

        //String myJSONStr = 'payload= {"username": "SALE BOT", "icon_url": "example.com/img/icon.jpg", "channel": "#general"}'





        //Getting content for email
        String email = "tunde8983@gmail.com";
        String subject = "glo data sent failed";
        String message = " fail";

        try{

//            //Creating SendMail object
            SendMail sm = new SendMail(this, email, subject, message);

            //Executing sendmail to send email
            sm.execute();


        }catch (Exception e){

            Log.e("SendMail", e.getMessage(), e);
        }


    }




    private void showNotification() {

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        // intent triggered, you can add other intent for other actions
        Intent mIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, mIntent, 0);


        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

                .setContentTitle("New Post!")
                .setContentText(mText)
                .setSmallIcon(R.drawable.textview_border)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .setWhen(System.currentTimeMillis())
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(uniqueID, mNotification);

        // If you want to hide the notification after it was selected, do the code below
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }




    private void saveToDb(){


        ContentValues values = new ContentValues();
        values.put(DataEntry.COLUMN_RECIPIENT_NUMBER, recipientNumber);
        values.put(DataEntry.COLUMN_BUNDLE_VALUE, mBundleValue);
        values.put(DataEntry.COLUMN_BUNDLE_COST, mBundleCost);
        values.put(DataEntry.COLUMN_REQUEST_SOURCE, mRequestSource);
        values.put(DataEntry.COLUMN_TIME_RECEIVED, timeReceived);
        values.put(DataEntry.COLUMN_TIME_DONE, currentTime);


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
