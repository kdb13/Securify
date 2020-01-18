package com.lina.securify.viewmodels.auth;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.lina.securify.data.repositories.AuthRepository;

/**
 * The base view model for authentication.
 */
public abstract class AuthViewModel extends ViewModel {

    protected AuthRepository authRepository;

    // Data for progress loading
    public final ObservableInt progressVisibility = new ObservableInt();
    public final ObservableBoolean isButtonEnabled = new ObservableBoolean();

    public AuthViewModel() {
        authRepository = AuthRepository.getInstance();

        initProgressLoading();
    }

    /**
     * Change the state of views involved in progress loading
     * @param isEnabled Whether the loading is enabled or not
     */
    public void toggleLoading(boolean isEnabled) {
        isButtonEnabled.set(!isEnabled);
        progressVisibility.set(isEnabled ? View.VISIBLE : View.GONE);
    }

    /**
     * Initialize the models
     */
    private void initProgressLoading() {
        progressVisibility.set(View.GONE);
        isButtonEnabled.set(true);
    }
}
