package com.lina.securify.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.lina.securify.data.repositories.AlertRepository;
import com.lina.securify.intents.Intents;
import com.lina.securify.intents.RequestCodes;
import com.lina.securify.views.dialogs.DialogListener;

public class AlertSender implements DialogListener {

    private Context context;
    private SmsManager smsManager;
    private AlertRepository alertRepository;

    public AlertSender(Context context) {
        this.context = context;
        smsManager = SmsManager.getDefault();
        alertRepository = AlertRepository.getInstance();
    }

    @Override
    public void onPositive() {

        // TODO: Send the alert to volunteers

    }

    private void sendMessage() {

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
                null,
                null,
                "Hello World",
                sentPendingIntent,
                deliveredPendingIntent
        );

    }
}
