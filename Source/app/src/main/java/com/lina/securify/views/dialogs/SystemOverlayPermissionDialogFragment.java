package com.lina.securify.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.lina.securify.R;

public class SystemOverlayPermissionDialogFragment extends DialogFragment {

    private Context context;

    public SystemOverlayPermissionDialogFragment(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(R.string.display_over_other_apps_permission_message);
        builder.setNegativeButton(R.string.button_cancel, null);
        builder.setPositiveButton(R.string.button_grant, (dialog, which) -> {

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + context.getPackageName()));

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }

            dismiss();

        });

        return builder.create();
    }

}
