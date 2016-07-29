package com.example.b2026015.bluetooth.rfb.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.activities.FeedbackActivity;
import com.example.b2026015.bluetooth.rfb.entities.BTDevice;
import com.example.b2026015.bluetooth.rfb.entities.Prompt;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.security.Timestamp;
import java.util.ArrayList;

public class TimerService extends Service {

    // String to hold type of social interaction
    private String encounterType;
    // Arraylist for close proximity devices
    private static ArrayList<BTDevice> closeProxBTDevices;
    private final IBinder mBinder = new LocalBinder();

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.notification_text;
    private String personName;
    private int QUESTION = R.string.question_mark;

    public class LocalBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    @Override
    public void onCreate() {

        closeProxBTDevices = new ArrayList<>();

        // The PendingIntent to launch our activity if the user selects this notification
        Intent mIntent = new Intent(TimerService.this, FeedbackActivity.class);

        // Type of encounter (Choice of casual encounter / lab talk / meeting)
        encounterType = "meeting";

        switch (encounterType) {
            case "casual":
                mIntent.putExtra("INTENT_ENCOUNTER_CASUAL", "encounter_casual");
                break;
            case "labtalk":
                mIntent.putExtra("INTENT_ENCOUNTER_LABTALK", "encounter_casual");
                break;
            case "meeting":
                mIntent.putExtra("INTENT_ENCOUNTER_MEETING", "encounter_casual");
                break;
        }

        // Create and send a new prompt
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Prompt prompt = new Prompt(personName, pendingIntent, this);
        prompt.sendNotification();
    }

    public static void addCloseProxDevice(BTDevice sBtd, long timeStamp) {
        for (BTDevice btd : closeProxBTDevices) { //If doesn't already exist
        if(!sBtd.getMACAddress().contentEquals(btd.getMACAddress())) {
                closeProxBTDevices.add(getSize(), btd);
            }
        }
    }

    public static int getSize() {
        return closeProxBTDevices.size();
    }

    public void sendNotification(BTDevice bld) {

    }

    public static void printImmediate() {
        for (BTDevice btds : closeProxBTDevices) {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println(btds.getMACAddress());
            System.out.println(btds.getProxBand());
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.


    /**
     * Show a notification while this service is running.
     */
//    private void showNotification(String person, PendingIntent contentIntent, TimerService serivce) {
//
//        String interaction = "Have you just had an interaction with ";
//        String question = "?";
//
//        CharSequence status_bar_text =
//
//        // Set the info for the views that show in the notification panel.
//        Notification notification = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.qm)  // the status icon
//                .setTicker(status_bar_text)  // the status text
//                .setWhen(System.currentTimeMillis())  // the time stamp
//                .setContentTitle("Close2Blu")  // the label of the entry
//                .setContentText(interaction + person + question)  // the contents of the entry
//                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
//                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
//                .setLights(Color.MAGENTA, 3000, 3000)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .build();
//
//
//        Intent notificationIntent = new Intent(this, FeedbackActivity.class);
//        notificationIntent.putExtra("encounter_casual", "001");
//        notificationIntent.putExtra("selected_person", "NAME OF PERSON"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(),0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        //notification.setLatestEventInfo(getApplicationContext(), "encounter_lab_talk","001", pendingNotificationIntent);
//
//
//        // Send the notification.
//        mNM.notify(NOTIFICATION, notification);
//
//
//    }

}
