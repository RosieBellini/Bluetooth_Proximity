package com.example.b2026015.bluetooth.rfb.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.b2026015.bluetooth.rfb.activities.DeviceActivity;
import com.example.b2026015.bluetooth.rfb.entities.BTDevice;

import java.util.ArrayList;
import java.util.List;

public class ScanningService extends Service {

    private boolean inRange;

    public ScanningService() {

        ArrayList<BTDevice> btDeviceArrayList = DeviceActivity.getBTDeviceList();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    public void enteredZone(String zone, List<BTDevice> btdevices) {

        inRange = true;
    }

    public void exitedZone(String zone) {

        inRange = false;
    }

//    public void showFeedback(Activity feedbackActivity, BTDevice bld, String title, String message) {
//        Intent notifyIntent = new Intent(this, FeedbackActivity.this);
//        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
//                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.qm)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .build();
//        notification.defaults |= Notification.DEFAULT_SOUND;
//        NotificationManager notificationManager =
//                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, notification);
//    }
}
