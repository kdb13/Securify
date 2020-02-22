package com.lina.securify.utils.alert;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Source;
import com.lina.securify.R;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Alert;
import com.lina.securify.data.models.NewUser;
import com.lina.securify.receivers.AlertReceiver;
import com.lina.securify.utils.constants.IntentActions;
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
    private FusedLocationProviderClient fusedLocationClient;

    public AlertSender(Context context) {
        this.context = context;
        builder = new AlertSmsBuilder(context);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void send() {

        // Fetch the information necessary to build the Alert SMS
        fetchPhones();

    }

    private void fetchPhones() {

        repository
                .getVolunteers()
                .get(Source.CACHE)
                .addOnSuccessListener(querySnapshot -> {

                    // #1 Get the phone numbers of victim's volunteers
                    final List<String> phones = new ArrayList<>();

                    for (DocumentSnapshot documentSnapshot : querySnapshot) {
                        phones.add(documentSnapshot.getString(MetaVolunteer.PHONE));
                    }

                    fetchUserInfo(phones);

                });

    }

    private void fetchUserInfo(List<String> phones) {

            repository
                    .getCurrentUserDocument()
                    .get(Source.CACHE)
                    .addOnSuccessListener(documentSnapshot -> {

                        // #2 Get the victim's information
                        NewUser user = documentSnapshot.toObject(NewUser.class);

                        if (user != null) {

                            Alert alert = new Alert(
                                    NewUser.fullName(user.getFirstName(), user.getLastName()),
                                    user.getPhone(),
                                    ""
                            );

                            Log.d(TAG, "Name: " + NewUser.fullName(user.getFirstName(), user.getLastName()));

                            // #3 Get the victim's location
                            fetchLocationAndSend(phones, alert);
                        }

                    })
                    .addOnFailureListener(e ->
                            Log.e(TAG, "Error fetching current user's document", e));

    }

    private void fetchLocationAndSend(List<String> phones, Alert alert) {

        fusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        alert.setLocation(
                                location.getLatitude() + "," + location.getLongitude()
                        );

                    } else {

                        alert.setLocation(context.getString(R.string.no_location));

                    }

                    // #4 Send the SMS
                    sendSms(builder.buildSms(alert), phones);

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
