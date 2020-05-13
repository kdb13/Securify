package com.lina.securify.sendalert;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.lina.securify.receivers.AlertReceiver;
import com.lina.securify.contracts.IntentActions;
import com.lina.securify.contracts.RequestCodes;

import java.util.List;

class AlertSmsSender {

    private static final String TAG = AlertSmsSender.class.getSimpleName();

    private String alertSms;
    private SmsManager smsManager;
    private List<String> phones;

    AlertSmsSender(String alertSms, List<String> phones) {
        this.alertSms = alertSms;
        this.phones = phones;
        smsManager = SmsManager.getDefault();
    }

    void send(Context context) {

        Intent intent = new Intent(context, AlertReceiver.class);
        intent.setAction(IntentActions.ACTION_ALERT_SENT);

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(
                context,
                RequestCodes.REQUEST_SMS_SENT,
                new Intent(IntentActions.ACTION_ALERT_SENT),
                0
        );

        for (String phone : phones) {

            smsManager.sendTextMessage(
                    phone, null, alertSms, sentPendingIntent, null);

        }

    }

}
