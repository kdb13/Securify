package com.lina.securify.validation;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

/**
 * Validates an email TextInputLayout
 */
public class EmailValidator extends TextInputLayoutValidator {

    private String errorString;

    public EmailValidator(TextInputLayout inputEmail,
                          String errorInvalidEmail) {
        super(inputEmail);

        this.errorString = errorInvalidEmail;
    }

    @Override
    public boolean validate() {

        String email = Utils.getTextInside(textInputLayout);

        // Check if the email is of valid format
        isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();

        setError();

        return isValid;
    }

    @Override
    protected void setError() {

        if (isValid)
            textInputLayout.setError("");
        else
            textInputLayout.setError(errorString);

    }
}
