package com.lina.securify.views.dialogs;

import android.app.Dialog;
import android.content.Context;

import com.lina.securify.utils.alert.AlertSender;

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
