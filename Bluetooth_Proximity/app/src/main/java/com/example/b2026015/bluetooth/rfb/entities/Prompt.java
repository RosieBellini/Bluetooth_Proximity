package com.example.b2026015.bluetooth.rfb.entities;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.activities.FeedbackActivity;
import com.example.b2026015.bluetooth.rfb.services.TimerService;

public class Prompt {

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.notification_text;
    private String personName;
    private int QUESTION = R.string.question_mark;
    PendingIntent pendingNotificationIntent;
    private Notification notification;
    private NotificationManager mNM;
    private Context context;

    public Prompt(String person, PendingIntent contentIntent, TimerService service) {

        mNM = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        String interaction = "What was your interaction with ";
        String question = " like?";

        CharSequence status_bar_text = (CharSequence) "Aren't you a social butterfly today?";

        // Set the info for the views that show in the notification panel.
        notification = new NotificationCompat.Builder(service)
                .setSmallIcon(R.drawable.qm)  // the status icon
                .setTicker(status_bar_text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Close2Blu")  // the label of the entry
                .setContentText(interaction + person + question)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.MAGENTA, 3000, 3000)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        // Cancel notification when clicked by the user (not persistent)
        notification.flags = Notification.FLAG_AUTO_CANCEL;

    }

    public void sendNotification() {
        mNM.notify(NOTIFICATION, notification);
    }

}
