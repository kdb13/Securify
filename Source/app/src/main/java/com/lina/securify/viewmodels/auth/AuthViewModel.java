package com.lina.securify.viewmodels.auth;

import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.lina.securify.repositories.AuthRepository;

/**
 * The base view model for authentication.
 */
public abstract class AuthViewModel extends ViewModel {

    protected AuthRepository authRepository;

    // Model
    public final ObservableInt progressVisibility = new ObservableInt();
    public final ObservableBoolean isButtonEnabled = new ObservableBoolean();

    public AuthViewModel() {
        authRepository = AuthRepository.getInstance();

        initModels();
    }

    /**
     * Change the state of views involved in loading
     * @param isEnabled Whether the loading is enabled or not
     */
    public void toggleLoading(boolean isEnabled) {
        isButtonEnabled.set(!isEnabled);
        progressVisibility.set(isEnabled ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Initialize the models
     */
    private void initModels() {
        progressVisibility.set(View.GONE);
        isButtonEnabled.set(true);
    }
}
