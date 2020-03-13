package com.lina.securify.views.dialogs;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lina.securify.R;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.sendalert.AlertBuilder;
import com.lina.securify.sendalert.AlertSmsBuilder;
import com.lina.securify.sendalert.AlertSmsSender;
import com.lina.securify.utils.Utils;
import com.lina.securify.utils.constants.MetaVolunteer;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps the dialog which sends the alert.
 */
public class SendAlertDialog {

    private static final String TAG = SendAlertDialog.class.getSimpleName();

    private AlertDialog dialog;

    public SendAlertDialog(Context context) {

        dialog = new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.send_alert)
                .setNegativeButton(R.string.button_cancel, null)
                .setPositiveButton(R.string.button_send, (dialog, which) -> sendAlert())
                .create();

        Utils.setWindowType(dialog);

        dialog.show();
    }

    private void sendAlert() {

        FirestoreRepository repository = FirestoreRepository.getInstance();

        repository.getVolunteers().get()
                .addOnSuccessListener(documentSnapshots -> {

                    if (!documentSnapshots.isEmpty()) {

                        // Get the volunteers
                        List<String> phones = new ArrayList<>();

                        for (DocumentSnapshot document : documentSnapshots)
                            phones.add(document.getString(MetaVolunteer.PHONE));

                        new AlertBuilder(dialog.getContext()).build(alert -> {

                            if (alert != null) {

                                // Build the sms
                                String sms = new AlertSmsBuilder(dialog.getContext(), alert).build();

                                Log.i(TAG, "SMS: " + sms);

                                // Send the sms
                                new AlertSmsSender(sms, phones).send(dialog.getContext());

                            }
                        });

                    }

                });

    }

}
