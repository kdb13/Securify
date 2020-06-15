package com.lina.securify.alerts.showalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.lina.securify.alerts.showalert.AlertSmsProcessor;

public class AlertSmsReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null &&
                intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            if (messages.length == 1) {
                AlertSmsProcessor.process(context, messages[0]);
            }

        }

    }

}