package com.lina.securify.utils.alert;

import android.content.Context;
import android.telephony.SmsMessage;
import android.util.Log;

import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Alert;
import com.lina.securify.utils.Utils;

public class AlertSmsProcessor {

    private static final String TAG = AlertSmsProcessor.class.getSimpleName();

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
                .userExistsWithPhone(Utils.trimPhone(smsMessage.getOriginatingAddress()))
                .get()
                .addOnSuccessListener((querySnapshot -> {

                    // Check if the SMS's mobile number belongs to a valid user
                    if (!querySnapshot.isEmpty()) {

                        Alert alert = builder.parseAlertFromSms(smsMessage.getMessageBody());

                        if (alert != null)
                            processResult.onResult(alert);
                    }

                }));

    }

    public interface ProcessResult {
        void onResult(Alert alert);
    }
}
