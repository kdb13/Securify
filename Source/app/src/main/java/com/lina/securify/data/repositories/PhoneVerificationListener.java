package com.lina.securify.data.repositories;

public interface PhoneVerificationListener {
    void onCodeSent(String verificationId);
}
