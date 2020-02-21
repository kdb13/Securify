package com.lina.securify.views.dialogs;

import android.app.Dialog;
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
import com.lina.securify.receivers.AlertReceiver;
import com.lina.securify.utils.AlertSender;
import com.lina.securify.utils.AlertSmsBuilder;
import com.lina.securify.utils.constants.IntentActions;
import com.lina.securify.utils.constants.MetaUser;
import com.lina.securify.utils.constants.MetaVolunteer;
import com.lina.securify.utils.constants.RequestCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a dialog that prompts the user whether to send the alert or not.
 * When the user presses the <b>Send</b> button, the alert will be sent to all the volunteers in form of
 * SMS.
 */
public class SendAlertDialog {

    private static final String TAG = SendAlertDialog.class.getSimpleName();

    private AlertSender alertSender;

    public SendAlertDialog(Context context) {

        alertSender = new AlertSender(context);

        // Show the dialog
        Dialog dialog = AlertDialogsBuilder.buildSendAlert(context, () -> alertSender.send());
        dialog.show();

    }

}
