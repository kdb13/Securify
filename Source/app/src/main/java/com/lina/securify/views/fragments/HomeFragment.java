package com.lina.securify.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.lina.securify.R;
import com.lina.securify.databinding.FragmentHomeBinding;
import com.lina.securify.utils.AccessibilityUtils;
import com.lina.securify.utils.Utils;
import com.lina.securify.utils.constants.Constants;
import com.lina.securify.utils.constants.IntentActions;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private FragmentHomeBinding binding;
    private Snackbar permissionsSnackbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setFragment(this);

        binding.switchAlertService.setOnCheckedChangeListener((button, isChecked) ->
                sendAlertServiceBroadcast(isChecked));

        sendAlertServiceBroadcast(binding.switchAlertService.isChecked());

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

        // Check for permissions
        if (Utils.canDrawOverlays(requireContext()) &&
                AccessibilityUtils.isEnabled(requireContext())) {

            if (permissionsSnackbar.isShown())
                permissionsSnackbar.dismiss();
            binding.switchAlertService.setEnabled(true);
        } else {
            binding.switchAlertService.setEnabled(false);
            binding.switchAlertService.setChecked(false);
            permissionsSnackbar.show();
        }
    }

    private void createPermissionsSnackbar() {

        permissionsSnackbar =
                Snackbar.make(binding.getRoot(), R.string.permissions_request, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.action_grant, view -> {

                            NavHostFragment.findNavController(this)
                                    .navigate(R.id.action_askForPermissions);

                        });

    }

    private void sendAlertServiceBroadcast(boolean isEnabled) {

        Intent intent = new Intent(IntentActions.ACTION_ALERT_SERVICE_STATE_CHANGED);
        intent.putExtra(Constants.EXTRA_ALERT_SERVICE_STATE, isEnabled);

        requireContext().sendBroadcast(intent);
    }

}
