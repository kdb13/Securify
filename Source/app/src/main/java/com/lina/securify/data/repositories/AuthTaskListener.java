package com.lina.securify.data.repositories;

public interface AuthTaskListener {

    int EXISTING_EMAIL = 0;
    int NEW_EMAIL = 1;
    int SIGNED_IN = 3;
    int INCORRECT_PASSWORD = 4;
    int SIGNED_UP = 5;
    int INCORRECT_OTP = 6;
    int EXISTING_PHONE = 7;

    void onComplete(int result);

}
