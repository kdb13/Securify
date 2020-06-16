package com.lina.securify.alerts.showalert;

import android.content.Context;
import android.telephony.SmsMessage;

import com.lina.securify.data.repositories.UsersRepository;
import com.lina.securify.data.contracts.UsersContract;
import com.lina.securify.alerts.Victim;
import com.lina.securify.utils.Utils;

public class AlertSmsProcessor {

    private static final String TAG = "AlertSmsProcessor";

    public static void process(Context context, SmsMessage smsMessage) {

        UsersRepository usersRepository = UsersRepository.getInstance();

        // Check if the phone number matches with the current user's one
        usersRepository.getCurrentUser().get()
                .addOnSuccessListener(documentSnapshot -> {

                    String phone = documentSnapshot.getString(UsersContract.PHONE);
                    String receivedPhone = Utils.trimPhone(smsMessage.getOriginatingAddress());

                    if (phone.equals(receivedPhone)) {

                        // Parse the SMS
                        AlertSmsParser parser = new AlertSmsParser(context);
                        Victim victim = parser.parse(smsMessage.getMessageBody());

                        if (victim != null) {
                            // Notify the user
                            AlertNotification alertNotification = new AlertNotification(context, victim);
                            alertNotification.show();
                        }

                    }

                });


    }

}
