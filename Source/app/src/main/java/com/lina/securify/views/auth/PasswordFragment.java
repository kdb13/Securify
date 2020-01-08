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

import com.lina.securify.data.repositories.AuthRepository.Result;
import com.lina.securify.R;
import com.lina.securify.databinding.FragmentPasswordBinding;
import com.lina.securify.viewmodels.auth.Constants;
import com.lina.securify.viewmodels.auth.PasswordViewModel;
import com.lina.securify.views.auth.validations.PasswordValidation;

import java.util.Objects;

/**
 * Checks if the password is correct and signs in the user.
 */
public class PasswordFragment extends Fragment implements Observer<Result> {

    private FragmentPasswordBinding binding;
    private PasswordViewModel viewModel;
    private PasswordValidation validation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);

        viewModel.getModel().setEmail(
                PasswordFragmentArgs.fromBundle(getArguments()).getEmail()
        );

        binding = FragmentPasswordBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the validation views
        validation = new PasswordValidation(binding);
    }

    /**
     * Called when the Sign In button is clicked
     */
    public void onSignInClick(View view) {

        if (validation.validate()) {
            viewModel.toggleLoading(true);
            viewModel.signIn().observe(this, this);
        }

    }

    /**
     * Called when the AuthRepository returns a auth result
     * @param result The changed auth result
     */
    @Override
    public void onChanged(Result result) {

        viewModel.toggleLoading(false);

        switch (result) {

            case WRONG_PASSWORD:
                viewModel.wrongPasswordErrorID.set(R.string.error_wrong_password);
                break;

            case SIGNED_IN:
                goToPhoneFragment();
                break;
        }

    }

    /**
     * Navigate to PhoneFragment
     */
    private void goToPhoneFragment() {

        NavHostFragment
                .findNavController(this)
                .navigate(PasswordFragmentDirections.actionVerifyPhone(false));

    }

}
