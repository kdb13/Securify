package com.lina.securify.viewmodels.auth;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;

import com.lina.securify.repositories.AuthRepository;

public class EmailViewModel extends AuthViewModel {

    private static final String TAG = EmailViewModel.class.getSimpleName();

    // Model
    private Model model;

    public Model getModel() {
        return model;
    }

    public EmailViewModel() {
        super();

        model = new Model();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public LiveData<AuthRepository.Result> checkEmailExists() {
        return authRepository.checkEmailExists(model.getEmail());
    }

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
