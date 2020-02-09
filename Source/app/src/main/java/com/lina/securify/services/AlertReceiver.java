package com.lina.securify.services;

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
import com.lina.securify.utils.constants.IntentActions;
import com.lina.securify.utils.Utils;
import com.lina.securify.views.dialogs.HelpAlertDialogBuilder;
import com.lina.securify.views.dialogs.ReceiveAlertDialog;

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";
    private FirestoreRepository repository;

    public AlertReceiver() {
        repository = FirestoreRepository.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {

            Log.d(TAG, "onReceive: " + intent.getAction());

            switch (intent.getAction()) {

                case IntentActions.ACTION_ALERT_SENT:
                    Log.d(TAG, "Alert sent!");
                    break;

                case IntentActions.ACTION_ALERT_DELIVERED:
                    Log.d(TAG, "Alert delivered!");
                    Utils.showToast(context, "Alert sent successfully!");
                    break;

                case Telephony.Sms.Intents.SMS_RECEIVED_ACTION:
                    showAlertDialog(context, intent);
                    break;
            }

        }

    }

    private void showAlertDialog(final Context context, Intent intent) {

        Log.d(TAG, "SMS Received!");

        context.setTheme(R.style.AppTheme);

        final SmsMessage smsMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent)[0];

        repository
                .userExistsWithPhone(smsMessage.getOriginatingAddress())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        // Check if the victim is valid
                        if (!queryDocumentSnapshots.isEmpty()) {

                            // Check if the message is an alert
                            if (Utils.isAnAlert(smsMessage.getMessageBody())) {

                                new ReceiveAlertDialog(context);

                            }

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error fetching user phones!", e);
                    }
                });

    }

}
