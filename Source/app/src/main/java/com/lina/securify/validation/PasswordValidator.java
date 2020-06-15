package com.lina.securify.validation;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.utils.Utils;

/**
 * Validates an password TextInputLayout
 */
public class PasswordValidator extends TextInputLayoutValidator {

    private String errorString;

    public PasswordValidator(TextInputLayout inputPassword, String errorString) {
        super(inputPassword);

        this.errorString = errorString;
    }

    @Override
    public boolean validate() {

        // Check if password length is less than 6
        isValid = !(Utils.getTextInside(textInputLayout).length() < 6);

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
