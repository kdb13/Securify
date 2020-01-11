package com.lina.securify.viewmodels.auth;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;

import com.lina.securify.R;
import com.lina.securify.data.models.NewUser;
import com.lina.securify.data.repositories.AuthRepository;

public class SignUpViewModel extends AuthViewModel {

    // Represents the new user's data
    private NewUser newUser;

    public NewUser getNewUser() {
        return newUser;
    }

    public SignUpViewModel() {
        super();

        newUser = new NewUser();
    }

    public LiveData<AuthRepository.Result> signUp() {
        return authRepository.signUp(newUser);
    }

    public LiveData<AuthRepository.Result> checkEmailExists() {
        return authRepository.checkEmailExists(newUser.getEmail());
    }

}
