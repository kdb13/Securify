package com.lina.securify.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.provider.Telephony;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.lina.securify.R;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.utils.AlertSmsProcessor;
import com.lina.securify.utils.AlertVibrator;
import com.lina.securify.utils.constants.IntentActions;
import com.lina.securify.utils.Utils;
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