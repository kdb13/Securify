package com.lina.securify.validators;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

/**
 * Validates an password TextInputLayout
 */
public class PasswordValidator extends TextInputValidator {

    private String errorString;

    public PasswordValidator(TextInputLayout inputPassword, String errorString) {
        super(inputPassword);

        this.errorString = errorString;
    }

    @Override
    public boolean validate() {

        // Check if password length is less than 6
        isValid = !(Utils.getTextInside(inputLayout).length() < 6);

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
