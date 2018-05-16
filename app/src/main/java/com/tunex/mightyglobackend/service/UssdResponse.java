package com.tunex.mightyglobackend.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.tunex.mightyglobackend.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MIGHTY5 on 4/5/2018.
 */

public class UssdResponse extends AccessibilityService {


    public static String TAG =  UssdResponse.class.getSimpleName();

    private static final int uniqueID = 1000;

    String text;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent");
        Log.i("info:", "onAccessibiltyEvent");
//        String text = event.getText().toString();
         text = event.getText().toString();

        if (event.getClassName().equals("android.app.AlertDialog")) {
            performGlobalAction(GLOBAL_ACTION_BACK);
            Log.d(TAG, text);
            Log.i("info:", text);
            Intent intent = new Intent(" com.example.mighty5.mightydata.action.REFRESH");
            intent.putExtra("message", text);
            // write a broad cast receiver and call sendbroadcast() from here, if you want to parse the message for balance, date

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm:ss a");
            String currentTime = "Current Time : " + mdformat.format(calendar.getTime());




            Log.i("current time: ", currentTime);
            Log.i("info", "UssdResponse" + text);
            //Toast.makeText(this, "Ussd response "+ text, Toast.LENGTH_SHORT).show();

            // processing done here….
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.Receiver.ACTION_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra("result", currentTime);
            //broadcastIntent.putExtra("current time", currentTime);
            sendBroadcast(broadcastIntent);


//            // if (!text.isEmpty()){
//
//                String balance = text;
//        Pattern p = Pattern.compile(":(.*?)G");
//
//        Matcher m = p.matcher(balance);
//
//            if (m.find()){
//
//                Toast.makeText(this, "Your last balance is "+ m.group(1), Toast.LENGTH_SHORT).show();
//
//
//
//
//            }else {
//
//                //Toast.makeText(getApplicationContext(), "Request not successful please try again", Toast.LENGTH_SHORT).show();
//
//               // showNotification();
//
//               // sendEmail();
//
//
//
//            }
//
//
//
////        while (m.find()){
////
////
////
////
////            Log.i("bal :", m.group(1));
////
////            Toast.makeText(this, "Your last balance is "+ m.group(1), Toast.LENGTH_SHORT).show();
////
////
//////            balanceValue = m.group(1);
//////            Log.i("balance info:", m.group(1));
////
////
////        }
//
////                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
////            }else {
////
////                Toast.makeText(getApplicationContext(), "Request not successful please try again", Toast.LENGTH_SHORT).show();
////            }
        }

    }



    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }


}
