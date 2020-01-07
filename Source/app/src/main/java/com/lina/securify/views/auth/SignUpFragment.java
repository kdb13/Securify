package com.lina.securify.views.auth;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lina.securify.R;
import com.lina.securify.databinding.FragmentSignUpBinding;
import com.lina.securify.viewmodels.auth.Constants;

import java.util.Objects;


/**
 * It creates a new user account.
 */
public class SignUpFragment extends Fragment {

    // TODO: Check & implement me

    private FragmentSignUpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    /**
     * Called when the Sign Up button is clicked.
     */
    public void onSignUpClick(View view) {

    }

    /**
     * Navigate to MainActivity.
     */
    private void goToMainActivity() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_global_mainActivity);

        getActivity().finish();
    }

    /**
     * Navigate to PhoneFragment.
     */
    private void goToPhoneFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_signUpFragment_to_verifyPhoneFragment);
    }

    /**
     * @return The email retrieved from Bundle
     */
    private String getEmailFromBundle() {

        return Objects.requireNonNull(getArguments())
                .getString(Constants.BUNDLE_EMAIL);

    }

}