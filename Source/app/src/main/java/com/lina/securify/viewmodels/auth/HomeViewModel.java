package com.lina.securify.viewmodels.auth;

import androidx.lifecycle.LiveData;

import com.lina.securify.data.repositories.AuthRepository;

public class HomeViewModel extends AuthViewModel {

    public LiveData<AuthRepository.Result> checkIfPhoneNoIsAdded() {
        return authRepository.checkIfPhoneNoIsAdded();
    }

}
