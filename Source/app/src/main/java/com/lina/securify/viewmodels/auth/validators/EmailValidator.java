package com.lina.securify.viewmodels.auth.validators;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

public class EmailValidator extends TextInputValidator {

    private String errorString;

    public EmailValidator(TextInputLayout inputEmail,
                          String errorInvalidEmail) {
        super(inputEmail);

        this.errorString = errorInvalidEmail;
    }

    @Override
    public boolean validate() {

        // Check if the email is of valid format
        String email = Utils.getTextInside(inputLayout);

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = true;
        } else
            isValid = false;

        setError();

        return isValid;
    }

    @Override
    protected void setError() {

        if (isValid)
            inputLayout.setError("");
        else
            inputLayout.setError(errorString);

    }
}
