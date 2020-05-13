package com.lina.securify.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.databinding.FragmentHomeBinding;
import com.lina.securify.services.AlertService;
import com.lina.securify.utils.AccessibilityUtils;
import com.lina.securify.utils.Utils;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.switchAlertService.setOnClickListener(this::toggleAlertService);

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();

        // If any of the special permissions is missing, stop the Alert Service
        if (!AccessibilityUtils.isEnabled(requireContext()) ||
                (Utils.isM() && !Utils.canDrawOverlays(requireContext())) ) {
            binding.switchAlertService.setChecked(false);
            AlertService.isListening = false;
        }

    }

    /**
     * Used to toggle the state of Alert Service when the switch is clicked.
     */
    private void toggleAlertService(View view) {

        if (binding.switchAlertService.isChecked()) {

            if (!Utils.isSpecialAccessGranted(requireContext())) {

                // Show the dialog to grant these special access permissions
                NavHostFragment
                        .findNavController(this)
                        .navigate(HomeFragmentDirections.showSpecialPermissionsDialog());

                binding.switchAlertService.setChecked(false);

            } else
                AlertService.isListening = true;

        } else
            AlertService.isListening = false;


    }

}
