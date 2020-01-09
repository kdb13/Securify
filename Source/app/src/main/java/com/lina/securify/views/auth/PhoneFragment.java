package com.lina.securify.views.auth;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthRepository;
import com.lina.securify.databinding.FragmentPhoneBinding;
import com.lina.securify.viewmodels.auth.PhoneViewModel;
import com.lina.securify.views.auth.validations.PhoneValidation;


/**
 * Verifies the phone no. of the user and adds it to the account.
 */
public class PhoneFragment extends Fragment {

    // TODO: Implement phone verification

    private boolean addNewUser;
    private String verificationId;

    private FragmentPhoneBinding binding;
    private PhoneValidation validation;
    private PhoneViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(PhoneViewModel.class);
        addNewUser = PhoneFragmentArgs.fromBundle(getArguments()).getAddPhoneNo();

        if (!addNewUser)
            viewModel.getModel().setPhoneNo(getCurrentPhoneNo());

        binding = FragmentPhoneBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validation = new PhoneValidation(binding);
    }

    public void onButtonClick(View view) {


        if (verificationId == null) {

            if (validation.validate()) {
                viewModel.toggleLoading(true);

                viewModel.sendVerificationCode().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        viewModel.toggleLoading(false);

                        if (s != null) {
                            verificationId = s;
                            viewModel.buttonTextId.set(R.string.button_verify);
                            viewModel.smsUiVisibility.set(true);
                        } else {
                            // TODO: Handle if code not sent successfully
                        }

                    }
                });

            }

        } else {

            if (validation.validateSmsCode()) {

                viewModel.toggleLoading(true);

                verifySmsCode();

            }

        }


    }

    private void verifySmsCode() {

        viewModel.invalidCodeErrorId.set(-1);

        viewModel.verifySmsCode(verificationId).observe(this, new Observer<AuthRepository.Result>() {
            @Override
            public void onChanged(AuthRepository.Result result) {

                viewModel.toggleLoading(false);

                switch (result) {

                    case PHONE_ADDED:
                        Toast.makeText(getContext(), "Phone added successfully!", Toast.LENGTH_SHORT).show();

                        break;

                    case INVALID_SMS_CODE:
                        viewModel.invalidCodeErrorId.set(R.string.error_invalid_sms_code);

                        break;
                }

            }
        });

    }

    private String getCurrentPhoneNo() {
        return FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .getPhoneNumber();
    }
}
