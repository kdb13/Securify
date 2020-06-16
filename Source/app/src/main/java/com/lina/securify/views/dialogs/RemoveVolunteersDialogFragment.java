package com.lina.securify.views.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lina.securify.R;
import com.lina.securify.data.repositories.VolunteersRepository;

public class RemoveVolunteersDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(requireActivity())
                .setMessage(R.string.msg_remove_volunteers)
                .setPositiveButton(R.string.button_remove, (dialog, which) -> {
                    VolunteersRepository.getInstance().removeAll();
                })
                .setNegativeButton(R.string.button_cancel, null)
                .create();
    }

}
