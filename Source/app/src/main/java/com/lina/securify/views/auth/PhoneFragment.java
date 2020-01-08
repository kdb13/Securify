package com.lina.securify.views.auth;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lina.securify.databinding.FragmentPhoneBinding;
import com.lina.securify.views.auth.validations.PhoneValidation;


/**
 * Verifies the phone no. of the user and adds it to the account.
 */
public class PhoneFragment extends Fragment {

    // TODO: Implement phone verification

    private FragmentPhoneBinding binding;
    private PhoneValidation validation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPhoneBinding.inflate(inflater, container, false);
        binding.setFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validation = new PhoneValidation(binding);
    }

    /**
     * Called when the Verify button is clicked.
     */
    public void onVerifyClick(View view) {

    }
}
