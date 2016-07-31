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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerService extends Service {

    // String to hold type of social interaction
    private static String encounterType;
    private static boolean isAlive;


    // Arraylist for close proximity devices
    private static HashMap<BTDevice, InteractionTimer> closeProxBTDevices;
    private final IBinder mBinder = new LocalBinder();
    private static Context mContext;

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

    // Class dedicated to checking proximity
    class CheckProximity extends TimerTask {
        public void run() {
            removeCloseProxDevice();
        }
    }

    @Override
    public void onCreate() {
        isAlive = true;
        closeProxBTDevices = new HashMap<>();
        mContext = this.getApplicationContext();

        Timer timer = new Timer();
        timer.schedule(new CheckProximity(), 0, 5000);
    }

    // Adds device if in close proximity to application
    public static void addCloseProxDevice(BTDevice sBtd, long timeStamp) {
        if (closeProxBTDevices.get(sBtd) == null) {
            if (!sBtd.getMACAddress().contentEquals(sBtd.getMACAddress())) {
                InteractionTimer it = new InteractionTimer(timeStamp);
                closeProxBTDevices.put(sBtd, it);
            }
        }
    }

    // Removes device if no longer in immediate zone + checks timer
    public void removeCloseProxDevice() {
        if(BLEScanningService.isAlive() &&  !BLEScanningService.getBTDeviceList().isEmpty()) {
            for (Map.Entry<BTDevice, InteractionTimer> entry : closeProxBTDevices.entrySet()) {
                if (entry.getKey().getProxBand().equals("Far") && entry.getValue().getInteractionLength() > 180000) // 3 minutes - interaction happened?
                {
                    sendNotification(entry);
                    closeProxBTDevices.remove(entry);
                } else {
                    closeProxBTDevices.remove(entry);
                }
            }
        }
    }

    public static int getSize() {
        return closeProxBTDevices.size();
    }

    public void sendNotification(Map.Entry<BTDevice, InteractionTimer> btIT) {

        // The PendingIntent to launch our activity if the user selects this notification
        Intent mIntent = new Intent(mContext, FeedbackActivity.class);

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
        Prompt prompt = new Prompt(btIT.getKey().getName(), pendingIntent, this);
        prompt.sendNotification();

    }

    public static boolean isAlive() {
        return isAlive;
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
