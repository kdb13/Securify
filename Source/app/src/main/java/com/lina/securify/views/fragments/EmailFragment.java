package com.lina.securify.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthTaskListener;
import com.lina.securify.databinding.FragmentEmailBinding;
import com.lina.securify.views.viewmodels.LoginViewModel;
import com.lina.securify.views.validations.EmailValidation;

/**
 * It checks if the email is associated with an existing user or not.
 */
public class EmailFragment extends Fragment {

    private EmailValidation validation;
    private LoginViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this)
                        .getBackStackEntry(R.id.emailFragment))
                .get(LoginViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentEmailBinding binding = FragmentEmailBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        binding.setViewModel(viewModel);

        validation = new EmailValidation(binding);

        return binding.getRoot();
    }

    /**
     * Called when the continue button is clicked
     */
    public void onContinueClick(View view) {

        // Check if the input email is valid
        if (validation.validate()) {

            // Check if email exists
            viewModel.checkEmailExists(result -> {

                switch(result) {

                    case AuthTaskListener.NEW_EMAIL:
                        goToSignUpFragment();
                        break;

                    case AuthTaskListener.EXISTING_EMAIL:
                        goToPasswordFragment();
                        break;

                    default:
                        break;
                }

                viewModel.isLoading.set(false);

            });


        }

    }

    /**
     * Navigate to PasswordFragment
     */
    private void goToPasswordFragment() {

        NavHostFragment.findNavController(this)
                .navigate(EmailFragmentDirections.actionLogin());

    }

    /**
     * Navigate to SignUpFragment
     */
    private void goToSignUpFragment() {

        NavHostFragment.findNavController(this)
                .navigate(EmailFragmentDirections
                        .actionSignUp(viewModel.getCredentials().getEmail())
                );

    }

}
