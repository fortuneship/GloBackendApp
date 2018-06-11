package com.tunex.mightyglobackend;

import android.app.Activity;
import android.app.AlarmManager;
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
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.tunex.mightyglobackend.model.PostRequest;
import com.tunex.mightyglobackend.model.PostResponse;
import com.tunex.mightyglobackend.task.SendMail;
import com.tunex.mightyglobackend.utilities.SendNotification;
import com.tunex.mightyglobackend.utilities.SetTime;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

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


    private Receiver mReceiver;

    // result from accessibility service
    String mText;
    String currentTime;

    String mBalance;

    private Handler handler;

    private TextToSpeech textToSpeechSystem;

    private MightyApi mightyApi;


    /** Database helper that will provide us access to the database */
    private DataDbHelper mDbHelper;


//    private static final int DATA_LOADER = 0;
//
//    DataCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


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
        new SetTime(mTimeReceived);

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




//        // call spinner method
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

            }
        });

        // check airtime balance
        mAirtimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkBalance();

                // if balance is below threshold
                // lowBalance();
            }
        });


//        h = new Handler();
//        new Thread(new Runnable() {
//            public void run(){
//                while(true){
//                    try{
//                        h.post(new Runnable(){
//                                   public void run(){
//                                     checkBalance();
//                                   }
//                               });
//                                TimeUnit.MINUTES.sleep(2000);
//                    }
//                    catch(Exception ex){
//                    }
//                }
//            }
//        }).start();

        handler = new Handler();
        handler.postDelayed(runnable, 300000);

        //handler.removeCallbacks(runnable);

        textToSpeechSystem = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {


                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeechSystem.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    // speak("Hello");

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }


            }
        });

        //method call to populate fields from api
        apiRequest();


    }




    /**
     * check balance every 5 minutes
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            checkBalance();
            // if balance is below threshold
            lowBalance();

      /* and here comes the "trick" */
            handler.postDelayed(this, 300000);
        }
    };


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

            //saveToDb();

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


            mightyApi = ApiClient.getApiClient().create(MightyApi.class);

            PostRequest postRequest = new PostRequest();
            postRequest.setStatus("success");
            postRequest.setStatusCode(1);

            Call<PostResponse> postResponseCall =   mightyApi.getTokenAccess(postRequest);

            postResponseCall.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                    int mStatusCode = response.code();

                    PostResponse postResponse = response.body();

                    Log.i("postResponse:", "" + mStatusCode);
                    Log.i("postResponseBody:", "" + postResponse);
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {

                    Log.i("onFailure:", "" + t.getMessage());
                }
            });


            if(intent.getAction().equals(MainActivity.Receiver.ACTION_RESPONSE)){

                // response from ussd stored in mText
                mText = intent.getStringExtra("result");
                currentTime = intent.getStringExtra("current time");

//                String reg =  "Sorry, you are not gifting to valid Globacom user.";

                // matcher and pattern to extract balance value from mText
                String balance = mText;
                Pattern p = Pattern.compile(":(.*?)G");


                Matcher m = p.matcher(balance);

                if (m.find()) {

                    //Toast.makeText(this, "Your last balance is " + m.group(1), Toast.LENGTH_SHORT).show();

                    // Then concatenate "Your last balance is " with the matcher and store in mBalance
                    mBalance = "Your last balance is " + m.group(1);

                    //Saved response received from broadcast to sharedPreferences
                    SharedPreferences sharedPref = getSharedPreferences("com.tunex.mightyglobackend", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("airtimeBalance", mBalance);
                    editor.putString("currentTime", currentTime);
                    editor.apply();

                    mAirtimeBalance.setText(mBalance);
                    mCurrentTime.setText(currentTime);

                    // call low balance method
                    lowBalance();


                }else{

                    mAirtimeBalance.setText(mText);
                    mCurrentTime.setText(currentTime);


                    saveToDb();

                    // send notification when request processing fails
                    SendNotification.notification(MainActivity.this, getResources().getString(R.string.device_notification_title),
                            getResources().getString(R.string.device_notification_message));


                    showDialog();

                    // send email when request failed
                    sendEmail();

//                    Log.i("ussdResult", mText);

                }

            }


        }
    }



    /**
     * Show dialog when request fails
     */
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

    /**
     * Send email when request fails
     */
    private void sendEmail(){

        //String SLACK_URL = "https://hooks.slack.com/services/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";

        //String myJSONStr = 'payload= {"username": "SALE BOT", "icon_url": "example.com/img/icon.jpg", "channel": "#general"}'

        //Getting content for email
        String email = getResources().getString(R.string.email_id);
        String subject = getResources().getString(R.string.email_subject);
        String message =  getResources().getString(R.string.email_message);

        try{

//            //Creating SendMail object
            SendMail sm = new SendMail(this, email, subject, message);

            //Executing sendmail to send email
            sm.execute();


        }catch (Exception e){

            Log.e("SendMail", e.getMessage(), e);
        }


    }

    /**
     * Check if balance is less / equal to threshold
     */
    private void lowBalance() {

        // Your last balance is  N3400.86.

        // create a local variable for benchmark price(i.e price to compare with the price mBalance
        double balanceThreshold = 4000;

        int indexStart = 23;
        int indexEnd = 30;

        try {


            // create variable " mCurrentBalanceSubString" to hold the sub string of mBalance
            String mCurrentBalanceSubString = mBalance.substring(indexStart, indexEnd);

            // convert  " mCurrentBalanceSubString" to double and store in "mmm" because it will throw an error if not converted(Now you've converted both balanceThreshold  and
            // " mCurrentBalanceSubString" to the same data type then you can compare them).
            double mmm = Double.parseDouble(mCurrentBalanceSubString);

            //Do comparison
            if (mmm <= balanceThreshold){

                // Toast.makeText(MainActivity.this, "your balance is low please top up!", Toast.LENGTH_LONG).show();

                Log.i("lowBalance",  "your balance is low please top up!");

                speak(getString(R.string.speech_text));


            }

            Log.i("subString", mCurrentBalanceSubString);


        }catch (Exception e){


            e.printStackTrace();
            Log.i("exception", e.getMessage());
        }

    }

    /**
     * speak method
     * @param text
     */
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeechSystem.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            textToSpeechSystem.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeechSystem != null) {
            textToSpeechSystem.stop();
            textToSpeechSystem.shutdown();
        }
        super.onDestroy();
    }


    /** Save to database */
    private void saveToDb(){

        DataDbHelper.saveToDb(recipientNumber, mBundleValue, mBundleCost, mRequestSource, timeReceived, currentTime, MainActivity.this);

    }

//    private void saveToDb(){
//
//
//        ContentValues values = new ContentValues();
//        values.put(DataEntry.COLUMN_RECIPIENT_NUMBER, recipientNumber);
//        values.put(DataEntry.COLUMN_BUNDLE_VALUE, mBundleValue);
//        values.put(DataEntry.COLUMN_BUNDLE_COST, mBundleCost);
//        values.put(DataEntry.COLUMN_REQUEST_SOURCE, mRequestSource);
//        values.put(DataEntry.COLUMN_TIME_RECEIVED, timeReceived);
//        values.put(DataEntry.COLUMN_TIME_DONE, currentTime);
//
//
//        Uri uri = getContentResolver().insert(DataEntry.CONTENT_URI, values);
//
//        if (uri == null) {
//            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
//            Log.i("info:", "error saving to db");
//        } else {
//            Toast.makeText(this, getString(R.string.form_save), Toast.LENGTH_SHORT).show();
//
//            Log.i("info:", "save to db is successful");
//        }
//
//
//
//    }


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
            case R.id.action_api_request:
                //deleteAllPets();

                startActivity(new Intent(this, MightyApiRequestActivity.class));

                return true;

            case R.id.action_api_notification:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Retrieve value of item clicked from api request list */
    private void apiRequest(){

        /** populate values from api into field */
        mRequestSourceSpinner.setSelection(6);

        mRecipientNumber.setText(getIntent().getStringExtra("mPhoneNumber"));

        String apiBundleValue = getIntent().getStringExtra("mQuantity");


        if (apiBundleValue != null && apiBundleValue.equals(getString(R.string.one_gig))) {
            mBundleValueSpinner.setSelection(1);
            mBundleCost = DataEntry.ONE_GIG_PRICE;

        }else if (apiBundleValue != null && apiBundleValue.equals(getString(R.string.two_gig))){
            mBundleValueSpinner.setSelection(2);
            mBundleCost = DataEntry.TWO_GIG_PRICE;

        }else if (apiBundleValue != null && apiBundleValue.equals(getString(R.string.four_point_five))){

            mBundleValueSpinner.setSelection(3);
            mBundleCost = DataEntry.FOUR_FIVE_GIG_PRICE;

        }else if (apiBundleValue != null && apiBundleValue.equals(R.string.seven_point_two)){

            mBundleValueSpinner.setSelection(4);
            mBundleCost = DataEntry.SEVEN_TWO_GIG_PRICE;

        }else if (apiBundleValue != null && apiBundleValue.equals(R.string.twelve_point_five)){

            mBundleValueSpinner.setSelection(5);
            mBundleCost = DataEntry.TWELVE_FIVE_GIG_PRICE;

        }else if (apiBundleValue != null && apiBundleValue.equals(R.string.fifteen_point_six)){

            mBundleValueSpinner.setSelection(6);
            mBundleCost = DataEntry.FIFTEEN_SIX_GIG_PRICE;

        }else if (apiBundleValue != null && apiBundleValue.equals(R.string.twenty_five)){

            mBundleValueSpinner.setSelection(7);
            mBundleCost = DataEntry.TWENTY_FIVE_GIG_PRICE;

        }else if (apiBundleValue != null && apiBundleValue.equals(R.string.twelve_point_five_mb)){

            mBundleValueSpinner.setSelection(8);
            mBundleCost = DataEntry.TWELVE_FIVE_MB_PRICE;

        }else {

            mBundleValueSpinner.setSelection(0);
            mBundleCost = DataEntry.BUNDLE_UNKOWN_PRICE;
            mRequestSourceSpinner.setSelection(0);
        }


    }

    public void remoteNotificatioin(){

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);

        String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
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

