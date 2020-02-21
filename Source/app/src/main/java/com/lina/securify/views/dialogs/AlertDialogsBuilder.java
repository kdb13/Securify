package com.lina.securify.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lina.securify.R;

import java.util.Objects;

/**
 * It creates the dialog for sending & receiving the alert.
 */
public class AlertDialogsBuilder {

    public static Dialog buildSendAlert(final Context context, final DialogListener listener) {

        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.send_alert)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPositive();
                    }
                })
                .create();

        return setWindowType(dialog);
    }

    public static Dialog buildReceiveAlert(final Context context, final View root) {

        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setView(root)
                .create();

        return setWindowType(dialog);

    }

    private static Dialog setWindowType(Dialog dialog) {
        // If Oreo or above, set the window type to to overlay
        if (Build.VERSION.SDK_INT >= 26)
            Objects.requireNonNull(dialog.getWindow())
                    .setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else
            Objects.requireNonNull(dialog.getWindow())
                    .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        return dialog;
    }

}