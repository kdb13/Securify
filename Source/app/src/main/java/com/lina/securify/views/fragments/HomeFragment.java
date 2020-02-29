package com.lina.securify.views.fragments;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentHomeBinding;
import com.lina.securify.utils.constants.Constants;
import com.lina.securify.utils.constants.IntentActions;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toggleAlertService(null, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setFragment(this);

        return binding.getRoot();
    }

    public void toggleAlertService(CompoundButton button, boolean isChecked) {

        // TODO: Get the current state of AccessibilityService

        if (!canDrawOverlays()) {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_askForPermissions);

            return;
        }

        // Notify the AlertService about its new state
        Intent intent = new Intent(IntentActions.ACTION_ALERT_SERVICE_STATE_CHANGED);
        intent.putExtra(Constants.EXTRA_ALERT_SERVICE_STATE, isChecked);

        requireContext().sendBroadcast(intent);

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean canDrawOverlays() {
        return Settings.canDrawOverlays(requireContext());
    }
}
