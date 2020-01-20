package com.lina.securify.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.lina.securify.intents.Intents;
import com.lina.securify.intents.RequestCodes;
import com.lina.securify.views.dialogs.DialogListener;

public class AlertSender implements DialogListener {

    private Context context;

    public AlertSender(Context context) {
        this.context = context;
    }

    @Override
    public void onPositive() {

        SmsManager smsManager = SmsManager.getDefault();

        Intent sentIntent = new Intent(context, AlertReceiver.class);
        sentIntent.setAction(Intents.ACTION_ALERT_SENT);

        Intent deliveredIntent = new Intent(context, AlertReceiver.class);
        deliveredIntent.setAction(Intents.ACTION_ALERT_DELIVERED);

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(
                context,
                RequestCodes.REQUEST_SMS_SENT,
                sentIntent,
                0
        );

        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(
                context,
                RequestCodes.REQUEST_SMS_SENT,
                deliveredIntent,
                0
        );

        smsManager.sendTextMessage(
                "8000046911",
                null,
                "Hello World",
                sentPendingIntent,
                deliveredPendingIntent
        );
    }
}
