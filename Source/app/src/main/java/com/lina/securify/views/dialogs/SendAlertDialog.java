package com.lina.securify.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

import com.lina.securify.services.ButtonService;

public class SendAlertDialog {

    public static Dialog build(final Context context, final DialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Send an alert")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPositive();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);

        return dialog;
    }

}