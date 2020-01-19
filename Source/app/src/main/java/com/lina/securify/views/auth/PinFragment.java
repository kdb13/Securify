package com.lina.securify.views.auth;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthRepository;
import com.lina.securify.databinding.FragmentPinBinding;
import com.lina.securify.viewmodels.auth.PinViewModel;
import com.lina.securify.views.MainActivity;
import com.lina.securify.views.auth.validations.PinValidation;


/**
 * It serves as a security portal towards the main application.
 */
public class PinFragment extends Fragment {

    private boolean addNewPin;

    private FragmentPinBinding binding;
    private PinViewModel viewModel;
    private PinValidation validation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addNewPin = PinFragmentArgs.fromBundle(getArguments()).getIsNewPin();

        viewModel = ViewModelProviders.of(this).get(PinViewModel.class);

        binding = FragmentPinBinding.inflate(inflater, container, false);
        binding.setWrongPinStringId(-1);
        binding.setFragment(this);
        binding.setViewModel(viewModel);

        setLayout();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validation = new PinValidation(binding);
    }

    public void onOkClick(View view) {

        if (addNewPin) {

            if (validation.validateNewPin() && viewModel.getModel().doPinsMatch()) {

                viewModel.toggleLoading(true);
                viewModel.addNewPin().observe(this, new Observer<AuthRepository.Result>() {
                    @Override
                    public void onChanged(AuthRepository.Result result) {

                        viewModel.toggleLoading(false);

                        switch (result) {
                            case NEW_PIN_ADDED:
                                goToPinFragmentForEntry();
                                break;

                            case UNKNOWN_ERROR:
                                break;
                        }

                    }
                });
            }

        } else {

            if (validation.validatePin()) {

                viewModel.toggleLoading(true);
                viewModel.verifyPin().observe(this, new Observer<AuthRepository.Result>() {
                    @Override
                    public void onChanged(AuthRepository.Result result) {

                        viewModel.toggleLoading(false);

                        switch (result) {
                            case PIN_VERIFIED:
                                goToMainActivity();
                                break;

                            case INVALID_PIN:
                                binding.setWrongPinStringId(R.string.error_wrong_pin);
                                break;
                        }

                    }
                });
            }

        }

    }

    private void setLayout() {
        if (addNewPin) {
            binding.setPinTitleStringId(R.string.set_up_new_pin);
            binding.setReenterPinVisibility(true);
        }
        else {
            binding.setPinTitleStringId(R.string.enter_pin);
            binding.setReenterPinVisibility(false);
        }
    }

    /**
     * Navigate to PinFragment to enter the main app.
     */
    private void goToPinFragmentForEntry() {
        NavHostFragment
                .findNavController(this)
                .navigate(PinFragmentDirections.actionVerifyPin());
    }

    /**
     * Navigate to the MainActivity of app
     */
    private void goToMainActivity() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

        getActivity().finish();
    }
}
