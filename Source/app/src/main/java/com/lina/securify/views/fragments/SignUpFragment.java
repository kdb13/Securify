package com.lina.securify.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthRepository;
import com.lina.securify.data.repositories.AuthTaskListener;
import com.lina.securify.databinding.FragmentSignUpBinding;
import com.lina.securify.viewmodels.SignUpViewModel;
import com.lina.securify.views.validations.SignUpValidation;


/**
 * It creates a new user account.
 */
public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private SignUpViewModel viewModel;
    private SignUpValidation validation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this)
                        .getBackStackEntry(R.id.signUpFragment))
                .get(SignUpViewModel.class);

        if (getArguments() != null)
            viewModel.getCredentials().setEmail(
                    SignUpFragmentArgs.fromBundle(getArguments()).getNewUserEmail()
            );

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setFragment(this);

        validation = new SignUpValidation(binding);

        return binding.getRoot();
    }

    /**
     * Called when the Sign Up button is clicked.
     */
    public void onSignUpClick(View view) {

        if (validation.validate()) {

            if (viewModel.getCredentials().getPassword().equals(
                    viewModel.getCredentials().getConfPassword())) {

                viewModel.checkEmailExists(result -> {

                    switch (result) {

                        case AuthTaskListener.EXISTING_EMAIL:
                            binding.inputEmail.setError(
                                    requireContext().getString(R.string.error_existing_email)
                            );

                            break;

                        case AuthTaskListener.NEW_EMAIL:
                            goToPhoneFragment();
                            break;

                        default:
                    }

                    viewModel.isLoading.set(false);

                });

            } else {

                binding.inputConfirmPassword.setError(
                        requireContext().getString(R.string.error_password_mismatch)
                );

            }

        }

    }

    /**
     * Navigate to PhoneFragment.
     */
    private void goToPhoneFragment() {

        NavHostFragment
                .findNavController(this)
                .navigate(SignUpFragmentDirections.actionAddPhone());

    }

}