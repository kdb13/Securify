package com.lina.securify.views.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;

/**
 * The entry point towards authentication.
 */
public class SplashScreenFragment extends Fragment {

    private static final long SPLASH_DELAY = 500;

    @Override
    public void onStart() {
        super.onStart();

        if (isSignedIn())
            goToMainActivity();
        else
            goToEmailFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    private void goToEmailFragment() {

        new Handler()
                .postDelayed(() -> {

                    NavHostFragment
                            .findNavController(this)
                            .navigate(SplashScreenFragmentDirections.actionCheckEmail());

                }, SPLASH_DELAY);

    }

    private void goToMainActivity() {

        new Handler()
                .postDelayed(() -> {

                    NavHostFragment.findNavController(this)
                            .navigate(SplashScreenFragmentDirections.actionMainApp());

                    requireActivity().finish();

                }, SPLASH_DELAY);


    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

}