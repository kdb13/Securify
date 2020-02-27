package com.lina.securify.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.util.Log;

import com.lina.securify.R;
import com.lina.securify.utils.alert.AlertSmsProcessor;
import com.lina.securify.utils.alert.AlertVibrator;
import com.lina.securify.utils.constants.IntentActions;
import com.lina.securify.views.dialogs.ReceiveAlertDialog;

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {

            switch (intent.getAction()) {

                case IntentActions.ACTION_ALERT_DELIVERED:
                    break;

                case Telephony.Sms.Intents.SMS_RECEIVED_ACTION:
                    new AlertSmsProcessor(context).process(
                            Telephony.Sms.Intents.getMessagesFromIntent(intent)[0],
                            alert -> {
                                context.setTheme(R.style.AppTheme);

                                // Show the dialog
                                new ReceiveAlertDialog(context, alert);

                                // Provide haptic feedback
                                new AlertVibrator(context).vibrate();

                            });

                    break;

            }

        }

    }
}