package com.lina.securify.alerts.showalert;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lina.securify.views.activities.AlertActivity;
import com.lina.securify.R;
import com.lina.securify.alerts.Victim;

public class AlertNotification {

    public static final String ALERT_CHANNEL_ID = "com.lina.securify.ALERT_CHANNEL";

    private Context context;

    private Victim victim;

    public AlertNotification(Context context, Victim victim) {
        this.context = context;
        this.victim = victim;
    }

    public void show() {

        // Content Title
        String contentTitle = context.getString(R.string.alert_help_message);

        // Content Text
        String contentText = context.getString(R.string.alert_person_and_phone,
                victim.getName(), victim.getPhone());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(fullScreenIntent(), true)
                .setAutoCancel(true)
                .addAction(actionCall());

        if (victim.getLocation() != null) {
            builder.addAction(actionLocate());
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());

    }

    private NotificationCompat.Action actionCall() {

        Intent intent = AlertActivity.callIntent(victim.getPhone());

        return new NotificationCompat.Action(null,
                context.getString(R.string.action_call),
                getPendingIntentForActivity(intent));

    }

    private NotificationCompat.Action actionLocate() {

        Intent intent = AlertActivity.locateIntent(victim.getLocation());

        return new NotificationCompat.Action(null,
                context.getString(R.string.action_locate),
                getPendingIntentForActivity(intent));

    }

    private PendingIntent fullScreenIntent() {
        Intent intent = new Intent(context, AlertActivity.class);

        // Pass the alert information with Intent
        intent.putExtra(AlertActivity.EXTRA_ALERT_PERSON, victim.getName());
        intent.putExtra(AlertActivity.EXTRA_ALERT_PERSON_PHONE, victim.getPhone());
        intent.putExtra(AlertActivity.EXTRA_ALERT_LOCATION, victim.getLocation());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private PendingIntent getPendingIntentForActivity(Intent intent) {
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

}
