package com.lina.securify.views.auth;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.AuthNavGraphDirections;
import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthRepository;


/**
 * The entry point towards authentication.
 */
public class SplashScreenFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is signed in
        if (isSignedIn()) {

            // Check if the user's phone no is verified
            if (isPhoneVerified())
                goToMainActivity();
            else
                goToPhoneFragment();

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

    private boolean isPhoneVerified() {

        return !TextUtils.isEmpty(
                FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .getPhoneNumber()
        );
    }
}
