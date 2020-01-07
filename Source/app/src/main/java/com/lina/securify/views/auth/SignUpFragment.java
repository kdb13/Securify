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
 * This fragment displays the UI to create a new account.
 */
public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onSignUpClick(View view) {

    }

    private void goToMainActivity() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_global_mainActivity);

        getActivity().finish();
    }

    private void goToVerifyPhoneFragment() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_signUpFragment_to_verifyPhoneFragment);
    }

    private String getEmailFromBundle() {

        return Objects.requireNonNull(getArguments())
                .getString(Constants.BUNDLE_EMAIL);

    }

}