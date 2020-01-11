package com.lina.securify.views.auth;

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

import com.lina.securify.data.repositories.AuthRepository.Result;
import com.lina.securify.databinding.FragmentEmailBinding;
import com.lina.securify.viewmodels.auth.EmailViewModel;
import com.lina.securify.views.auth.validations.EmailValidation;

/**
 * It checks if the email is associated with an existing user or not.
 */
public class EmailFragment extends Fragment {

    private FragmentEmailBinding binding;
    private EmailValidation validation;
    private EmailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(EmailViewModel.class);

        binding = FragmentEmailBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        validation = new EmailValidation(binding);
    }

    /**
     * Called when the continue button is clicked
     */
    public void onContinueClick(View view) {

        // Check if the input email is valid
        if (validation.validate()) {

            // Check if email exists
            viewModel.toggleLoading(true);
            viewModel.checkEmailExists().observe(this, new Observer<Result>() {
                @Override
                public void onChanged(Result result) {

                    viewModel.toggleLoading(false);

                    switch (result) {

                        case EXISTING_EMAIL:
                            goToPasswordFragment();
                            break;

                        case NEW_EMAIL:
                            goToSignUpFragment();
                            break;

                        default:

                    }

                }
            });
        }

    }

    /**
     * Navigate to PasswordFragment
     */
    private void goToPasswordFragment() {

        NavHostFragment
                .findNavController(this)
                .navigate(
                        EmailFragmentDirections.actionVerifyPassword(viewModel.getModel().getEmail())
                );

    }

    /**
     * Navigate to SignUpFragment
     */
    private void goToSignUpFragment() {

        NavHostFragment
                .findNavController(this)
                .navigate(
                        EmailFragmentDirections.actionNewUser(viewModel.getModel().getEmail())
                );

    }

}
