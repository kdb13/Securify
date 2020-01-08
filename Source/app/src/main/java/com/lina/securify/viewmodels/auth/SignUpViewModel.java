package com.lina.securify.viewmodels.auth;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;

import com.lina.securify.R;
import com.lina.securify.data.models.NewUser;
import com.lina.securify.data.repositories.AuthRepository;

public class SignUpViewModel extends AuthViewModel {

    // TODO: Check email exists

    // Represents the new user data
    private NewUser newUser;

    /**
     * String ID for email exists error.
     */
    public final ObservableInt emailExistsErrorID = new ObservableInt();

    /**
     * String ID for passwords mismatch error.
     */
    public final ObservableInt passwordsMismatchErrorID = new ObservableInt();

    public NewUser getNewUser() {
        return newUser;
    }

    public SignUpViewModel() {
        super();

        newUser = new NewUser();
        emailExistsErrorID.set(-1);
        passwordsMismatchErrorID.set(-1);
    }

    public LiveData<AuthRepository.Result> signUp() {
        return authRepository.signUp(newUser);
    }

    public LiveData<AuthRepository.Result> checkEmailExists() {
        return authRepository.checkEmailExists(newUser.getEmail());
    }

    public boolean checkIfPasswordsMatch() {

        if ( newUser.getPassword().equals(newUser.getConfPassword()) ) {
            passwordsMismatchErrorID.set(-1);
            return true;
        } else {
            passwordsMismatchErrorID.set(R.string.error_password_mismatch);
            return false;
        }

    }
}
