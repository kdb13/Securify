package com.lina.securify.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lina.securify.R;
import com.lina.securify.databinding.DialogReceiveAlertBinding;

public class ReceiveAlertDialog {

    private Dialog dialog;

    public ReceiveAlertDialog(Context context) {

        DialogReceiveAlertBinding binding =
                DialogReceiveAlertBinding.inflate(LayoutInflater.from(context));

        dialog = HelpAlertDialogBuilder.buildReceiveAlert(context, binding.getRoot());
        dialog.show();

        binding.setDialog(this);
    }

    public void onButtonClick(View view) {

        switch (view.getId()) {

            case R.id.button_call:
                break;

            case R.id.button_locate:
                break;

            case R.id.button_reply:
                break;

            default:

        }

        dialog.dismiss();

    }
}
