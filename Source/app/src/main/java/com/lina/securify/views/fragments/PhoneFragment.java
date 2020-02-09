package com.lina.securify.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthRepository;
import com.lina.securify.databinding.FragmentPhoneBinding;
import com.lina.securify.viewmodels.PhoneViewModel;
import com.lina.securify.views.validations.PhoneValidation;


/**
 * Verifies the phone no. of the user and adds it to the account.
 */
public class PhoneFragment extends Fragment {

    private String verificationId;

    private FragmentPhoneBinding binding;
    private PhoneValidation validation;
    private PhoneViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(PhoneViewModel.class);

        binding = FragmentPhoneBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setFragment(this);
        binding.setCodeUiVisibility(false);
        binding.setInvalidCodeStringId(-1);
        binding.setPhoneButtonTextId(R.string.button_send_code);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validation = new PhoneValidation(binding);
    }

    public void onButtonClick(View view) {

        viewModel.toggleLoading(true);

        // Send the code
        if (verificationId == null) {

            if (validation.validate()) {
                sendVerificationCode();
            }

        } else {

            if (validation.validateSmsCode()) {
                verifySmsCode();
            }

        }


    }

    private void verifySmsCode() {

        // Verify the received code
        viewModel.verifySmsCode(verificationId).observe(this, new Observer<AuthRepository.Result>() {
            @Override
            public void onChanged(AuthRepository.Result result) {

                viewModel.toggleLoading(false);

                switch (result) {

                    case PHONE_VERIFIED:
                        goToPinFragment();
                        break;

                    case INVALID_SMS_CODE:
                        binding.setInvalidCodeStringId(R.string.error_invalid_sms_code);
                        break;

                }

            }
        });

    }

    private void sendVerificationCode() {

        viewModel.sendVerificationCode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                viewModel.toggleLoading(false);

                if (s != null) {
                    verificationId = s;

                    binding.setCodeUiVisibility(true);
                    binding.setPhoneButtonTextId(R.string.button_verify);
                }

            }
        });

    }

    /**
     * Navigate to PinFragment.
     */
    private void goToPinFragment() {
        NavHostFragment
                .findNavController(this)
                .navigate(PhoneFragmentDirections.actionVerifyPin());
    }
}
