package com.lina.securify.views.viewmodels;

import android.util.Log;

import com.lina.securify.data.models.LoginCredentials;
import com.lina.securify.data.repositories.AuthTaskListener;

/**
 * The ViewModel for EmailFragment
 */
public class LoginViewModel extends AuthViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    private LoginCredentials credentials;

    public LoginViewModel() {
        credentials = new LoginCredentials();
        isLoading.set(false);
    }

    public LoginCredentials getCredentials() {
        return credentials;
    }

    public void checkEmailExists(AuthTaskListener listener) {

        // Start loading
        isLoading.set(true);

        repository.checkEmailExists(credentials.getEmail(), listener);

    }

    public void login(AuthTaskListener listener) {

        // Start loading
        isLoading.set(true);

        repository.login(credentials, listener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        Log.d(TAG, "onCleared: ");
    }
}
