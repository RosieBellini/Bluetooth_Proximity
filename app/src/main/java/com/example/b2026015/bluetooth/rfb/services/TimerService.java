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
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.activities.FeedbackActivity;
import com.example.b2026015.bluetooth.rfb.entities.BTDevice;
import com.example.b2026015.bluetooth.rfb.entities.InteractionTimer;
import com.example.b2026015.bluetooth.rfb.entities.Prompt;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerService extends Service {

    // String to hold type of social interaction
    private String encounterType;

    // Arraylist for close proximity devices
    private static HashMap<BTDevice, InteractionTimer> closeProxBTDevices;
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
        closeProxBTDevices = new HashMap<>();

        //Runnable dedicated to continuously check proximity in order to reorder devices according to proximity
        Runnable checkerRunnable = new Runnable() {
            public void run() {
                checkCloseProximity();
            }
        };

        // Schedule reordering every second
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(checkerRunnable, 0, 1, TimeUnit.SECONDS);
    }

    public void checkCloseProximity() {
        if(DeviceActivity.hasStarted() && DeviceActivity.getBTDeviceList().get(0) != null) { // If activity has started + device list isn't empty
            for (BTDevice btd : DeviceActivity.getBTDeviceList()) {
                if (btd.getProxBand().equals("Immediate")) {
                    addCloseProxDevice(btd, System.currentTimeMillis());
                }
            }
        }
    }

    public static void addCloseProxDevice(BTDevice sBtd, long timeStamp) {
        if (closeProxBTDevices.get(sBtd) == null) {
            if (!sBtd.getMACAddress().contentEquals(sBtd.getMACAddress())) {
                InteractionTimer it = new InteractionTimer(timeStamp);
                closeProxBTDevices.put(sBtd, it);
            }
        }
    }

    public static void removeCloseProxDevice() {
        Iterator ite = closeProxBTDevices.entrySet().iterator();
        for (Map.Entry<BTDevice, InteractionTimer> entry : closeProxBTDevices.entrySet()) {
            if(entry.getValue().getInteractionLength() < 180000) // 3 minutes
            {
                closeProxBTDevices.remove(entry);
            }
        }
    }


    public static int getSize() {
        return closeProxBTDevices.size();
    }

    public void sendNotification(BTDevice bld) {

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


}
