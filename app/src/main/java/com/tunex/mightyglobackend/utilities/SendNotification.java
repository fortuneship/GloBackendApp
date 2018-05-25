package com.tunex.mightyglobackend.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.tunex.mightyglobackend.MainActivity;
import com.tunex.mightyglobackend.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by MIGHTY5 on 5/21/2018.
 */

public class SendNotification {

    private static final int uniqueID = 100;


    public static void notification(Context ctx, String mText){

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        // intent triggered, you can add other intent for other actions
        Intent mIntent = new Intent(ctx, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, mIntent, 0);


        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(ctx)

                .setContentTitle("New Post!")
                .setContentText(mText)
                .setSmallIcon(R.drawable.textview_border)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .setWhen(System.currentTimeMillis())
                .build();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(uniqueID, mNotification);

        // If you want to hide the notification after it was selected, do the code below
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);

    }
}
