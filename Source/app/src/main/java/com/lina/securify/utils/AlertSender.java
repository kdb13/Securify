package com.lina.securify.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Source;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Alert;
import com.lina.securify.data.models.NewUser;
import com.lina.securify.receivers.AlertReceiver;
import com.lina.securify.utils.constants.IntentActions;
import com.lina.securify.utils.constants.MetaUser;
import com.lina.securify.utils.constants.MetaVolunteer;
import com.lina.securify.utils.constants.RequestCodes;

import java.util.ArrayList;
import java.util.List;

public class AlertSender {

    private static final String TAG = AlertSender.class.getSimpleName();

    private FirestoreRepository repository = FirestoreRepository.getInstance();
    private SmsManager smsManager = SmsManager.getDefault();

    private Context context;
    private AlertSmsBuilder builder;

    public AlertSender(Context context) {
        this.context = context;
        builder = new AlertSmsBuilder(context);
    }

    public void send() {

        // Fetch the information necessary to build the Alert SMS
        fetchData();

    }

    /**
     * It fetches the information necessary to build the SMS and then sends the SMS by
     * calling the <code>sendSms</code> method.
     */
    private void fetchData() {

        repository
                .getVolunteers()
                .get(Source.CACHE)
                .addOnSuccessListener((querySnapshot -> {

                    // #1 Get the phone numbers of victim's volunteers
                    final List<String> phones = new ArrayList<>();

                    for (DocumentSnapshot documentSnapshot : querySnapshot) {
                        phones.add(documentSnapshot.getString(MetaVolunteer.PHONE));
                    }

                    repository
                            .getCurrentUserDocument()
                            .get(Source.CACHE)
                            .addOnSuccessListener((documentSnapshot -> {

                                // #2 Get the victim's information and build the Alert
                                NewUser user = documentSnapshot.toObject(NewUser.class);

                                if (user != null) {
                                    Alert alert = new Alert(
                                            NewUser.fullName(user.getFirstName(), user.getLastName()),
                                            user.getPhone(),
                                            "0.0:0.0"
                                    );

                                    // #3 Build the Alert SMS and send it
                                    sendSms(builder.buildSms(alert), phones);

                                } else {
                                    Log.e(TAG, "A null User returned!");
                                }

                            }))
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error fetching current user's document", e);
                            });

                }))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching volunteers!", e);
                });

    }

    /**
     * It actually sends the SMS to specified phone numbers.
     * @param sms The SMS to send
     * @param phones The phone numbers to which the message will be sent
     */
    private void sendSms(String sms, List<String> phones) {

        // Create intents for receiving the SMS status
        Intent sentIntent = new Intent(context, AlertReceiver.class);
        sentIntent.setAction(IntentActions.ACTION_ALERT_SENT);

        Intent deliveredIntent = new Intent(context, AlertReceiver.class);
        deliveredIntent.setAction(IntentActions.ACTION_ALERT_DELIVERED);

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

        // Send the SMS
        for (String phone : phones) {

            // TODO: Handle the multi-SIM case
            smsManager.sendTextMessage(
                    phone, null, sms, sentPendingIntent, deliveredPendingIntent);
        }

    }
}
