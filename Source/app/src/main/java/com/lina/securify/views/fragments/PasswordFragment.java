package com.lina.securify.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthTaskListener;
import com.lina.securify.databinding.FragmentPasswordBinding;
import com.lina.securify.viewmodels.LoginViewModel;
import com.lina.securify.views.validations.PasswordValidation;

/**
 * Checks if the password is correct and signs in the user.
 */
public class PasswordFragment extends Fragment {

    private static final String TAG = PasswordFragment.class.getSimpleName();

    private FragmentPasswordBinding binding;
    private PasswordValidation validation;
    private LoginViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this)
                        .getBackStackEntry(R.id.emailFragment))
                .get(LoginViewModel.class);

        Log.d(TAG, "Email: " + viewModel.getCredentials().getEmail());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPasswordBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        binding.setViewModel(viewModel);

        validation = new PasswordValidation(binding);

        return binding.getRoot();
    }

    /**
     * Called when the Sign In button is clicked
     */
    public void onSignInClick(View view) {

        if (validation.validate()) {

            viewModel.login(result -> {

                switch (result) {

                    case AuthTaskListener.SIGNED_IN:
                        // Signed in
                        goToMainActivity();
                        break;

                    case AuthTaskListener.INCORRECT_PASSWORD:
                        // Show the error
                        binding.inputPassword.setError(
                                requireContext().getString(R.string.error_wrong_password));

                        default:

                }

                // Stop the loading
                viewModel.isLoading.set(false);
            });

        }

    }

    private void goToMainActivity() {

        NavHostFragment.findNavController(this)
                .navigate(PasswordFragmentDirections.actionMainApp());

        requireActivity().finish();

    }
}
