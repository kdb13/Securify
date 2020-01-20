package com.lina.securify.views.auth;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
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

        // Check if the user is signed in
        if (isSignedIn())
            goToPinFragment();
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
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NavHostFragment
                                .findNavController(SplashScreenFragment.this)
                                .navigate(SplashScreenFragmentDirections.actionVerifyEmail());
                    }
                }, SPLASH_DELAY);

    }

    private void goToPinFragment() {

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NavHostFragment
                                .findNavController(SplashScreenFragment.this)
                                .navigate(SplashScreenFragmentDirections.actionVerifyPin());
                    }
                }, SPLASH_DELAY);

    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

}
