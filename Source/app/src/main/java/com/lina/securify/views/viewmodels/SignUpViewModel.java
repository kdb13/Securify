package com.lina.securify.views.viewmodels;

import androidx.databinding.ObservableBoolean;

import com.lina.securify.data.models.SignUpCredentials;
import com.lina.securify.data.repositories.AuthTaskListener;

public class SignUpViewModel extends AuthViewModel {

    private SignUpCredentials credentials;

    public ObservableBoolean showCodeUi = new ObservableBoolean();

    public SignUpViewModel() {
        credentials = new SignUpCredentials();
        showCodeUi.set(false);
    }

    public SignUpCredentials getCredentials() {
        return credentials;
    }

    public void checkEmailExists(AuthTaskListener listener ) {

        // Start loading
        isLoading.set(true);

        repository.checkEmailExists(credentials.getEmail(), listener);

    }

    public void verifyOtp(String verificationId, AuthTaskListener listener) {

        // Start loading
        isLoading.set(true);

        repository.verifyOtp(credentials, verificationId, listener);
    }

    public void checkPhoneExists(AuthTaskListener listener) {
        isLoading.set(true);

        repository.checkPhoneExists(credentials.getPhone(), listener);
    }
}
