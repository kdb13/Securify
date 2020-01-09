package com.lina.securify.viewmodels.auth;

import android.database.Observable;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;

import com.lina.securify.R;
import com.lina.securify.data.repositories.AuthRepository;

public class PhoneViewModel extends AuthViewModel {

    private Model model;

    public ObservableBoolean smsUiVisibility = new ObservableBoolean();
    public ObservableInt buttonTextId = new ObservableInt();
    public ObservableInt invalidCodeErrorId = new ObservableInt();

    public PhoneViewModel() {
        model = new Model();

        smsUiVisibility.set(false);
        buttonTextId.set(R.string.button_send_code);
        invalidCodeErrorId.set(-1);
    }

    public Model getModel() {
        return model;
    }

    public LiveData<String> sendVerificationCode() {
        return authRepository.sendVerificationCode("+91" + model.getPhoneNo());
    }

    public LiveData<AuthRepository.Result> verifySmsCode(String verificationId) {
        return authRepository.verifySmsCode(verificationId, model.getSmsCode());
    }

    public static class Model {

        private String phoneNo;
        private String smsCode;

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setSmsCode(String smsCode) {
            this.smsCode = smsCode;
        }

        public String getSmsCode() {
            return smsCode;
        }
    }
}
