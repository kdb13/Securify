package com.lina.securify.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Alert;
import com.lina.securify.data.models.NewUser;
import com.lina.securify.utils.Utils;
import com.lina.securify.utils.constants.IntentActions;
import com.lina.securify.utils.constants.MetaUser;
import com.lina.securify.utils.constants.MetaVolunteer;
import com.lina.securify.utils.constants.RequestCodes;
import com.lina.securify.views.dialogs.DialogListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the logic behind sending the alert.
 */
public class AlertSender implements DialogListener {

    private static final String TAG = AlertSender.class.getSimpleName();

    private Context context;
    private SmsManager smsManager;
    private FirestoreRepository repository;

    public AlertSender(Context context) {
        this.context = context;
        smsManager = SmsManager.getDefault();
        repository = FirestoreRepository.getInstance();
    }

    @Override
    public void onPositive() {

        // TODO: Send the alert to volunteers
        repository
                .getVolunteers()
                .get(Source.CACHE)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        // Fetch the phone numbers
                        List<String> phones = new ArrayList<>();

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            phones.add(snapshot.getString(MetaVolunteer.PHONE));
                            Log.d(TAG, snapshot.getString(MetaVolunteer.PHONE));
                        }

                        // Send the alert to those numbers
                        sendAlert(phones);
                    }


                });

    }

    private void sendAlert(final List<String> phones) {

        repository
                .getCurrentUserDocument()
                .get(Source.CACHE)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Alert alert = new Alert(
                                NewUser.fullName(
                                        documentSnapshot.getString(MetaUser.FIRST_NAME),
                                        documentSnapshot.getString(MetaUser.LAST_NAME)),
                                documentSnapshot.getString(MetaUser.PHONE),
                                "0:0", "", ""
                        );

                        sendAlertSms(alert, phones);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });

    }

    private void sendAlertSms(Alert alert, List<String> phones) {

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

        for (String phone : phones) {

            smsManager.sendTextMessage(
                    phone,
                    null,
                    Utils.makeAlertMessage(context, alert),
                    sentPendingIntent,
                    deliveredPendingIntent
            );

        }

    }

}
