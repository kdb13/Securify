package com.lina.securify.views.dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lina.securify.R;
import com.lina.securify.data.models.Alert;
import com.lina.securify.databinding.DialogAlertInfoBinding;
import com.lina.securify.utils.Utils;

public class AlertInfoDialog {

    private static final String TAG = AlertInfoDialog.class.getSimpleName();

    private Dialog dialog;
    private DialogAlertInfoBinding binding;

    public AlertInfoDialog(Context context, Alert alert) {

        context.setTheme(R.style.AppTheme);

        binding = DialogAlertInfoBinding.inflate(LayoutInflater.from(context));

        dialog = new MaterialAlertDialogBuilder(context)
                .setView(binding.getRoot())
                .setCancelable(false)
                .create();

        Utils.setWindowToOverlay(dialog);

        dialog.show();

        binding.setAlert(alert);
        binding.setIsLocateVisible(alert.getLocation().equals(context.getString(R.string.alert_no_location))
                ? View.GONE : View.VISIBLE);
        binding.setDialog(this);

    }

    public void onButtonClick(View view) {

        switch (view.getId()) {

            case R.id.button_call:
                call(binding.getAlert().getVictimPhone());
                break;

            case R.id.button_locate:
                locate(binding.getAlert().getLocation());
                break;

            default:

        }

        dialog.dismiss();

    }

    private void call(String phone) {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));

        if (ContextCompat.checkSelfPermission(
                dialog.getContext(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            dialog.getContext().startActivity(intent);

        }
    }

    private void locate(String location) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q=" + location));
        intent.setPackage("com.google.android.apps.maps");

        dialog.getContext().startActivity(intent);
    }
}
