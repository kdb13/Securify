package com.lina.securify.viewmodels.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.lina.securify.data.repositories.AuthRepository;

public class PinViewModel extends AuthViewModel {

    private Model model;

    public PinViewModel() {
        super();

        model = new Model();
    }

    public Model getModel() {
        return model;
    }

    public LiveData<AuthRepository.Result> addNewPin() {
        return authRepository.addPin(model.getPin());
    }

    public LiveData<AuthRepository.Result> verifyPin() {
        return authRepository.verifyPin(model.getPin());
    }

    public static class Model {

        private String pin;
        private String confirmPin;

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public String getConfirmPin() {
            return confirmPin;
        }

        public void setConfirmPin(String confirmPin) {
            this.confirmPin = confirmPin;
        }

        public boolean doPinsMatch() {
            return pin.equals(confirmPin);
        }
    }

}
