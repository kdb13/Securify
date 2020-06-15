package com.lina.securify.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lina.securify.R;

public class ConfirmationDialogBuilder {

    public static Dialog build(Context context,
                               String title,
                               String message,
                               String positiveButton,
                               OnClickListener listener) {

        return new MaterialAlertDialogBuilder(context)
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton(positiveButton, (dialog, which) -> listener.onPositive())
                .setNegativeButton(R.string.button_cancel, null)
                .create();

    }

    public interface OnClickListener {
        void onPositive();
    }

}
