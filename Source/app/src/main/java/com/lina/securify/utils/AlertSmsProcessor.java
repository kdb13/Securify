package com.lina.securify.utils;

import android.content.Context;
import android.telephony.SmsMessage;

import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Alert;

public class AlertSmsProcessor {

    private FirestoreRepository repository = FirestoreRepository.getInstance();
    private AlertSmsBuilder builder;

    public AlertSmsProcessor(Context context) {
        builder = new AlertSmsBuilder(context);
    }

    /**
     * Processes the SMS and builds an Alert object if the SMS is a valid alert
     * @param smsMessage The SMS to process
     * @param processResult The object where result will be passed
     */
    public void process(SmsMessage smsMessage, ProcessResult processResult) {

        repository
                .userExistsWithPhone(smsMessage.getOriginatingAddress())
                .get()
                .addOnSuccessListener((querySnapshot -> {

                    // Check if the SMS's mobile number belongs to a valid user
                    if (!querySnapshot.isEmpty()) {

                        processResult.onResult(builder.parseAlertFromSms(
                                smsMessage.getMessageBody()
                        ));

                    } else
                        processResult.onResult(null);

                }));

    }

    public interface ProcessResult {
        void onResult(Alert alert);
    }
}
