package com.lina.securify.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentSpecialPermissionsBinding;
import com.lina.securify.utils.AccessibilityUtils;
import com.lina.securify.utils.Utils;

public class SpecialPermissionsFragment extends Fragment {

    private FragmentSpecialPermissionsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSpecialPermissionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonGrantAccess.setOnClickListener(this::navigateToSystemOverlaySettings);

        binding.buttonStartService.setOnClickListener(this::navigateToAccessibilitySettings);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.areSpecialPermissionsGranted(requireContext())) {
            NavHostFragment.findNavController(this).navigateUp();
        } else if (AccessibilityUtils.isEnabled(requireContext())) {
            binding.buttonStartService.setEnabled(false);
            binding.buttonStartService.setText(R.string.started);
        } else if (Utils.canDrawOverlays(requireContext())) {
            binding.buttonGrantAccess.setEnabled(false);
            binding.buttonGrantAccess.setText(R.string.granted);
        }

    }

    /**
     * Starts the activity to start Accessibility services.
     */
    public void navigateToAccessibilitySettings(View view) {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    /**
     * Starts the activity to allow <b>Display over other apps</b> permission.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    public void navigateToSystemOverlaySettings(View view) {

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + requireContext().getPackageName()));

        startActivity(intent);

    }

}
