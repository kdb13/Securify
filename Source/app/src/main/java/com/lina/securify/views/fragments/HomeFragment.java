package com.lina.securify.views.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.alerts.sendalert.AlertSender;
import com.lina.securify.databinding.FragmentHomeBinding;
import com.lina.securify.utils.Utils;
import com.lina.securify.views.dialogs.ConfirmationDialogBuilder;
import com.lina.securify.views.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS = 1;

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;

    private AlertSender alertSender;

    private Dialog sendAlertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        alertSender = new AlertSender(requireContext().getApplicationContext());


        sendAlertDialog = ConfirmationDialogBuilder.build(requireContext(),
                getString(R.string.send_alert_title),
                getString(R.string.send_alert_msg),
                getString(R.string.button_alert),
                () -> alertSender.send());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fabSendAlert.setOnClickListener(this::navigateToSendAlertDialog);

        binding.switchAlertService.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (buttonView.isPressed()) {

                if (!Utils.areSpecialPermissionsGranted(requireContext())) {
                    navigateToSpecialPermissions();
                    viewModel.setInstantAlertState(false);
                } else {
                    viewModel.setInstantAlertState(isChecked);
                }
            }

        });

        viewModel.getInstantAlertState().observe(getViewLifecycleOwner(),
                isOn -> binding.switchAlertService.setChecked(isOn));

    }

    @Override
    public void onStart() {
        super.onStart();

        if (viewModel.isAppFresh()) {

            if (Utils.isSDK(Build.VERSION_CODES.M)) {

                Context context = requireContext();

                List<String> permissions = new ArrayList<>();
                permissions.add(Manifest.permission.RECEIVE_SMS);
                permissions.add(Manifest.permission.SEND_SMS);
                permissions.add(Manifest.permission.READ_SMS);
                permissions.add(Manifest.permission.CALL_PHONE);
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

                if (Utils.isSDK(Build.VERSION_CODES.Q)) {
                    permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                }

                if (!Utils.arePermissionsGranted(context, permissions)) {
                    requestPermissions((String[]) permissions.toArray(), REQUEST_PERMISSIONS);
                }

                viewModel.makeAppStale();

            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!Utils.areSpecialPermissionsGranted(requireContext())) {
            viewModel.setInstantAlertState(false);
        }

    }

    private void navigateToSendAlertDialog(View view) {
        sendAlertDialog.show();
    }

    private void navigateToSpecialPermissions() {
        NavHostFragment
                .findNavController(this)
                .navigate(HomeFragmentDirections.actionGrantSpecialPermissions());
    }

}
