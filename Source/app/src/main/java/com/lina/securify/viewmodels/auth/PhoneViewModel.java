package com.lina.securify.viewmodels.auth;

import androidx.lifecycle.LiveData;

import com.lina.securify.data.repositories.AuthRepository;

public class PhoneViewModel extends AuthViewModel {

    private Model model;

    public PhoneViewModel() {
        model = new Model();
    }

    public Model getModel() {
        return model;
    }

    public LiveData<String> sendVerificationCode() {
        return authRepository.sendVerificationCode(model.formatPhone());
    }

    public LiveData<AuthRepository.Result> verifySmsCode(String verificationId) {
        return authRepository.verifySmsCode(verificationId, model.getSmsCode());
    }

    public static class Model {

        private static final String COUNTRY_CODE = "+91";

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

        public String formatPhone() {
            return COUNTRY_CODE + phoneNo;
        }
    }
}
