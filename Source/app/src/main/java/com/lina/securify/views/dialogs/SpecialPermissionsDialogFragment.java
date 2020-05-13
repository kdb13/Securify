package com.lina.securify.views.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.databinding.DialogSpecialPermissionsBinding;
import com.lina.securify.utils.AccessibilityUtils;
import com.lina.securify.utils.Utils;

public class SpecialPermissionsDialogFragment extends DialogFragment {

    private DialogSpecialPermissionsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogSpecialPermissionsBinding.inflate(inflater, container, false);
        binding.setDialog(this);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.isSpecialAccessGranted(requireContext())) {
            NavHostFragment.findNavController(this).navigateUp();
        } else if (AccessibilityUtils.isEnabled(requireContext())) {
            binding.buttonStartService.setEnabled(false);
            binding.buttonStartService.setText(R.string.started);
        } else if (!Utils.isM() || Utils.canDrawOverlays(requireContext())) {
            binding.buttonGrantAccess.setEnabled(false);
            binding.buttonGrantAccess.setText(R.string.granted);
        }

    }

    /**
     * Starts the activity to start Accessibility services.
     */
    public void goToAccessibilitySettings(View view) {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    /**
     * Starts the activity to allow <b>Display over other apps</b> permission.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    public void goToSystemOverlaySettings(View view) {

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + requireContext().getPackageName()));

        startActivity(intent);

    }
}
