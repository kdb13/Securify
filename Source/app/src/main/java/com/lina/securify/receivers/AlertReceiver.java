package com.lina.securify.receivers;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lina.securify.AlertActivity;
import com.lina.securify.R;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Alert;
import com.lina.securify.sendalert.AlertSmsParser;
import com.lina.securify.utils.Utils;
import com.lina.securify.sendalert.AlertVibrator;
import com.lina.securify.views.dialogs.AlertInfoDialog;

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {

            switch (intent.getAction()) {

                case Telephony.Sms.Intents.SMS_RECEIVED_ACTION:

                    SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

                    if (messages.length == 1)
                        processSms(messages[0], context);
                    else
                        Log.w(TAG, "More than 1 SmsMessage objects found!");

                    break;

            }

        }

    }

    private void processSms(SmsMessage message, Context context) {
        Log.d(TAG, "Originating number: " + message.getOriginatingAddress());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Securify")
                .setSmallIcon(R.drawable.ic_contacts)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context).notify(12, builder.build());

        // Check if the message is from a valid user
        FirestoreRepository.getInstance()
                .userExistsWithPhone(Utils.trimPhone(message.getOriginatingAddress()))
                .get()
                .addOnSuccessListener(documentSnapshots -> {

                    if (!documentSnapshots.isEmpty()) {

                        Log.i(TAG, "Originating number is valid.");

                        Alert alert = new AlertSmsParser(context)
                                .parse(message.getMessageBody());

                        if (alert != null) {

                            Log.i(TAG, "SMS is valid.");

                            new AlertInfoDialog(context, alert);
                            new AlertVibrator(context).vibrate();

                        }
                    }

                });

    }
}