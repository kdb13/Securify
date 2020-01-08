package com.lina.securify.views.auth;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.AuthNavGraphDirections;
import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthRepository;
import com.lina.securify.viewmodels.auth.HomeViewModel;


/**
 * The entry point towards authentication.
 */
public class SplashScreenFragment extends Fragment implements Observer<AuthRepository.Result> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        // Check if user is signed in
        if (isSignedIn()) {
            // Check if the user's phone no is verified
            viewModel.checkIfPhoneNoIsAdded().observe(this, this);
        } else {

            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            goToEmailFragment();
                        }
                    },
                    700
            );


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onChanged(AuthRepository.Result result) {

        switch (result) {

            case PHONE_EXISTS:
                // TODO: Go to MainActivity
                break;

            case PHONE_NOT_FOUND:
                goToPhoneFragment();
                break;

            case UNKNOWN_ERROR:
                // TODO: Handle this one

                break;
        }
    }

    private void goToPhoneFragment() {

        SplashScreenFragmentDirections.ActionVerifyPhone action =
                SplashScreenFragmentDirections.actionVerifyPhone(false);

        action.setAddPhoneNo(true);

        NavHostFragment
                .findNavController(this)
                .navigate(action);

    }

    private void goToMainActivity() {
        NavHostFragment
                .findNavController(this)
                .navigate(AuthNavGraphDirections.actionMainApp());
    }

    private void goToEmailFragment() {
        NavHostFragment
                .findNavController(this)
                .navigate(SplashScreenFragmentDirections.actionVerifyEmail());
    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
