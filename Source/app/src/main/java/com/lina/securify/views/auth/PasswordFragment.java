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

import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthRepository.Result;
import com.lina.securify.databinding.FragmentPasswordBinding;
import com.lina.securify.viewmodels.auth.PasswordViewModel;
import com.lina.securify.views.auth.validations.PasswordValidation;

/**
 * Checks if the password is correct and signs in the user.
 */
public class PasswordFragment extends Fragment {

    private FragmentPasswordBinding binding;
    private PasswordViewModel viewModel;
    private PasswordValidation validation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);

        viewModel.getModel().setEmail(
                PasswordFragmentArgs.fromBundle(getArguments()).getExistingUserEmail()
        );

        binding = FragmentPasswordBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        binding.setViewModel(viewModel);
        binding.setWrongPasswordStringId(-1);

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
            viewModel.signIn().observe(this, new Observer<Result>() {
                @Override
                public void onChanged(Result result) {

                    viewModel.toggleLoading(false);

                    switch (result) {

                        case WRONG_PASSWORD:
                            binding.setWrongPasswordStringId(R.string.error_wrong_password);
                            break;

                        case SIGNED_IN:
                            goToPinFragment();
                            break;
                    }

                }
            });
        }

    }

    /**
     * Navigate to PinFragment
     */
    private void goToPinFragment() {

        NavHostFragment
                .findNavController(this)
                .navigate(PasswordFragmentDirections.actionVerifyPin());

    }

}
