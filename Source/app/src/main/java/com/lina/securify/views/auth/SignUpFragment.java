package com.lina.securify.views.auth;


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
import com.lina.securify.databinding.FragmentSignUpBinding;
import com.lina.securify.viewmodels.auth.Constants;
import com.lina.securify.viewmodels.auth.SignUpViewModel;
import com.lina.securify.views.auth.validations.SignUpValidation;

import java.util.Objects;


/**
 * It creates a new user account.
 */
public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private SignUpViewModel viewModel;
    private SignUpValidation validation;

    private Observer<AuthRepository.Result> emailExistsObserver = new Observer<AuthRepository.Result>() {
        @Override
        public void onChanged(AuthRepository.Result result) {

            viewModel.toggleLoading(false);

            switch (result) {

                case EXISTING_EMAIL:
                    viewModel.emailExistsErrorID.set(R.string.error_existing_email);
                    break;

                case NEW_EMAIL:
                    signUp();
                    break;
                    
            }
        }
    };

    private Observer<AuthRepository.Result> signUpObserver = new Observer<AuthRepository.Result>() {
        @Override
        public void onChanged(AuthRepository.Result result) {
            viewModel.toggleLoading(false);

            switch (result) {

                case SIGNED_UP:
                    goToPhoneFragment();
                    break;

                case UNKNOWN_ERROR:
                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        viewModel.getNewUser().setEmail(getEmailFromBundle());

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setFragment(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the validation view
        validation = new SignUpValidation(binding);
    }

    /**
     * Called when the Sign Up button is clicked.
     */
    public void onSignUpClick(View view) {

        if (validation.validate() && viewModel.checkIfPasswordsMatch()) {
            viewModel.toggleLoading(true);
            viewModel.checkEmailExists().observe(this, emailExistsObserver);
        }

    }

    private void signUp() {
        viewModel.toggleLoading(true);

        viewModel.signUp().observe(this, signUpObserver);
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