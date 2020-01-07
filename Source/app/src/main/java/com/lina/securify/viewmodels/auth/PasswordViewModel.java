package com.lina.securify.viewmodels.auth;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;

import com.lina.securify.data.repositories.AuthRepository;

/**
 * The ViewModel for PasswordFragment.
 */
public class PasswordViewModel extends AuthViewModel {

    private Model model;

    /**
     * The error string ID for password TextInputLayout.
     */
    public final ObservableInt passwordErrorID = new ObservableInt();

    public PasswordViewModel() {
        super();

        model = new Model();
        passwordErrorID.set(-1);
    }

    public Model getModel() {
        return model;
    }

    public LiveData<AuthRepository.Result> signIn() {
        return authRepository.signIn(model.getEmail(), model.getPassword());
    }

    /**
     * Holds the UI data.
     */
    public static class Model {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
