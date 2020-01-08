package com.lina.securify.viewmodels.auth;

import androidx.lifecycle.LiveData;

import com.lina.securify.data.repositories.AuthRepository;

/**
 * The ViewModel for EmailFragment
 */
public class EmailViewModel extends AuthViewModel {

    // TODO: Navigate to phone fragment if verification is not complete

    private static final String TAG = EmailViewModel.class.getSimpleName();

    private Model model;

    public Model getModel() {
        return model;
    }

    public EmailViewModel() {
        super();

        model = new Model();
    }

    public LiveData<AuthRepository.Result> checkEmailExists() {
        return authRepository.checkEmailExists(model.getEmail());
    }

    /**
     * Holds the UI data.
     */
    public static class Model {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
