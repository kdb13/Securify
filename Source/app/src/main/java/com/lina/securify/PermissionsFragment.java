package com.lina.securify;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lina.securify.databinding.FragmentPermissionsBinding;
import com.lina.securify.utils.Utils;
import com.lina.securify.utils.constants.Constants;
import com.lina.securify.utils.constants.IntentActions;

public class PermissionsFragment extends Fragment {

    private FragmentPermissionsBinding binding;
    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null &&
                    intent.getAction().equals(IntentActions.ACTION_ACCESSIBILITY_SERVICE_CHANGED)) {

                binding.checkAccessibility.setChecked(intent.getBooleanExtra(
                        Constants.EXTRA_ACCESSIBILITY_SERVICE_STATE, false
                ));

            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireContext().registerReceiver(serviceReceiver, new IntentFilter(
                IntentActions.ACTION_ACCESSIBILITY_SERVICE_CHANGED
        ));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPermissionsBinding.inflate(inflater, container, false);
        binding.setShowSystemOverlayPermission(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
        binding.setFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Settings.canDrawOverlays(requireContext()))
                binding.checkSystemOverlay.setChecked(true);
            else
                binding.checkSystemOverlay.setChecked(false);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        requireContext().unregisterReceiver(serviceReceiver);
    }

    public void onCheckboxClick(View view) {

        switch (view.getId()) {

            case R.id.check_accessibility:
                goToAccessibilitySettings();
                break;

            case R.id.check_system_overlay:
                goToSystemOverlaySettings();
                break;

            default:

        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void goToSystemOverlaySettings() {

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + requireContext().getPackageName()));

        startActivity(intent);

    }

    private void goToAccessibilitySettings() {

        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

    }
}
