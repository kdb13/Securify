package com.lina.securify.views.fragments;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.lina.securify.databinding.FragmentHomeBinding;
import com.lina.securify.services.alertservice.AlertService;
import com.lina.securify.utils.AccessibilityUtils;
import com.lina.securify.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private FragmentHomeBinding binding;
    private Snackbar permissionsSnackbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createPermissionsSnackbar();
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPermissions();
    }

    /**
     * Checks the "System Overlay" permission and state of Accessibility Service.
     */
    private void checkPermissions() {

        boolean canDrawOverlays = true;

        // Check for System Overlay permission
        if (Utils.isM())
            canDrawOverlays = Settings.canDrawOverlays(requireContext());

        if (canDrawOverlays && AccessibilityUtils.isEnabled(requireContext())) {
            binding.switchAlertService.setChecked(true);

            if (permissionsSnackbar.isShown())
                permissionsSnackbar.dismiss();
        }
        else {
            binding.switchAlertService.setChecked(false);
            permissionsSnackbar.show();
        }
    }

    /**
     * Creates the Snackbar to ask for permissions.
     */
    private void createPermissionsSnackbar() {

        permissionsSnackbar =
                Snackbar.make(binding.getRoot(), "Please grant permissions!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Grant", view -> {
                        });

    }

}
