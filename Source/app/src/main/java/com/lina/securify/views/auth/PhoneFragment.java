package com.lina.securify.views.auth;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lina.securify.databinding.FragmentVerifyPhoneBinding;


/**
 * Verifies the phone no. of the user and adds it to the account.
 */
public class PhoneFragment extends Fragment {

    private FragmentVerifyPhoneBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVerifyPhoneBinding.inflate(inflater, container, false);
        binding.setFragment(this);

        return binding.getRoot();
    }

    /**
     * Called when the Verify button is clicked.
     */
    public void onVerifyClick(View view) {

    }
}
